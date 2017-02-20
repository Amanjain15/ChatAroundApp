package com.fame.plumbum.chataround.pollution.provider;

import com.fame.plumbum.chataround.helper.Constants;
import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.pollution.OnAirPollutionReceived;
import com.fame.plumbum.chataround.pollution.api.AirPollutionRequestApi;
import com.fame.plumbum.chataround.pollution.model.air_model.AirPollutionDetails;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by meghal on 21/2/17.
 */

public class RetrofitPollutionProvider implements PollutionProvider {


    @Override
    public void requestAirPollution(double latitude, double longitude, final OnAirPollutionReceived onAirPollutionReceived) {

        String BASE_URL_POLLUTION_API = "https://api.waqi.info/feed/geo:" + String.valueOf(latitude)
                + ";" + String.valueOf(longitude) +"/";

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_POLLUTION_API)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AirPollutionRequestApi airPollutionRequestApi = retrofit.create(AirPollutionRequestApi.class);

        Call<AirPollutionDetails> call = airPollutionRequestApi.requestAirPollution(Constants.AIR_POLLUTION_TOKEN);

        call.enqueue(new Callback<AirPollutionDetails>() {
            @Override
            public void onResponse(Call<AirPollutionDetails> call, Response<AirPollutionDetails> response) {

                onAirPollutionReceived.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<AirPollutionDetails> call, Throwable t) {

                onAirPollutionReceived.onFailed("Failed to Connect To Servers");
            }
        });

    }
}
