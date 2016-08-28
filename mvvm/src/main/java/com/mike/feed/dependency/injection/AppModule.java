package com.mike.feed.dependency.injection;

import android.content.Context;

import com.mike.feed.SquarApp;
import com.mike.feed.UIThread;
import com.mike.feed.data.cache.FeedCache;
import com.mike.feed.data.cache.FeedCacheImpl;
import com.mike.feed.data.executor.JobExecutor;
import com.mike.feed.data.repository.BitmapDataRepository;
import com.mike.feed.data.repository.FeedDataRepository;
import com.mike.feed.domain.executor.PostExecutionThread;
import com.mike.feed.domain.executor.ThreadExecutor;
import com.mike.feed.domain.repository.BitmapRepository;
import com.mike.feed.domain.repository.FeedRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class AppModule {

    SquarApp mApp;


    public AppModule(SquarApp application){
        this.mApp = application;
    }

    @Provides
    @Singleton
    Context provideContext(){
        return mApp;
    }


    @Provides
    @Singleton
    FeedCache provideFeedCache(){
        return new FeedCacheImpl();
    }



    @Provides
    @Singleton
    FeedRepository provideFeedRepository(FeedDataRepository feedDataRepository){
        return feedDataRepository;
    }


    @Provides
    @Singleton
    BitmapRepository provideBitmapRepository(BitmapDataRepository bitmapDataRepository){
        return bitmapDataRepository;
    }


    @Provides
    @Singleton
    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
        return jobExecutor;
    }

    @Provides @Singleton
    PostExecutionThread providePostExecutionThread(UIThread uiThread) {
        return uiThread;
    }
}
