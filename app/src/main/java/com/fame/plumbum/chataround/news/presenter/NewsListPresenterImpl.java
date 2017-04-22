package com.fame.plumbum.chataround.news.presenter;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.fame.plumbum.chataround.helper.Keys;
import com.fame.plumbum.chataround.news.NewsFeedRequestCallback;
import com.fame.plumbum.chataround.news.model.NewsListData;
import com.fame.plumbum.chataround.news.provider.NewsListProvider;
import com.fame.plumbum.chataround.news.view.NewsListFragment;
import com.fame.plumbum.chataround.news.view.NewsListView;

/**
 * Created by ramya on 10/3/17.
 */

public class NewsListPresenterImpl implements NewsListPresenter {
    private NewsListView newsListView;
    private NewsListProvider newsListProvider;

    public NewsListPresenterImpl(NewsListFragment newsListView, NewsListProvider newsListProvider) {
        this.newsListView = newsListView;
        this.newsListProvider = newsListProvider;
    }


    @Override
    public void getNews(boolean cache, final String userId, final String city, final String state, final String country) {
        newsListView.showProgressBar(true);
        newsListProvider.getNewsList(cache, userId, city, state, country, new NewsFeedRequestCallback() {
            @Override
            public void onSuccess(NewsListData newsListData) {
                newsListView.showProgressBar(false);
                if (newsListData.isSuccess()) {
                    newsListView.setNewsList(newsListData.getNewsListDataDetailsList());
                    Answers.getInstance().logCustom(new CustomEvent("News Module Loading Successful")
                            .putCustomAttribute(Keys.USER_EMAIL, userId)
                            .putCustomAttribute(Keys.KEY_CITY,city)
                            .putCustomAttribute(Keys.KEY_COUNTRY,country)
                            .putCustomAttribute(Keys.KEY_STATE,state)

                    );

                } else {
                    newsListView.showMessage(newsListData.getMessage());
                    Answers.getInstance().logCustom(new CustomEvent("News Module Loading Failed")
                            .putCustomAttribute(Keys.USER_EMAIL, userId)
                            .putCustomAttribute(Keys.KEY_CITY,city)
                            .putCustomAttribute(Keys.KEY_COUNTRY,country)
                            .putCustomAttribute(Keys.KEY_STATE,state)

                    );

                }
            }

            @Override
            public void OnFailure(String message) {

                newsListView.showProgressBar(false);
                newsListView.showMessage(message);
            }
        });

    }

    @Override
    public void onDestroy() {
        newsListProvider.onDestroy();
    }
}
