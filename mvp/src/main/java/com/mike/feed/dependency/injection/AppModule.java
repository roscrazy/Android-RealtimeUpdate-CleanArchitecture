package com.mike.feed.dependency.injection;

import android.content.Context;

import com.mike.feed.data.cache.FeedCache;
import com.mike.feed.data.cache.FeedCacheImpl;
import com.mike.feed.data.executor.JobExecutor;
import com.mike.feed.data.repository.BitmapDataRepository;
import com.mike.feed.data.repository.FeedDataRepository;
import com.mike.feed.domain.executor.PostExecutionThread;
import com.mike.feed.domain.executor.ThreadExecutor;
import com.mike.feed.domain.repository.BitmapRepository;
import com.mike.feed.domain.repository.FeedRepository;
import com.mike.feed.FeedApp;
import com.mike.feed.UIThread;
import com.mike.feed.util.C;
import com.mike.utility.cache.DiskCache;
import com.mike.utility.cache.MemoryCache;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;



@Module
public class AppModule {

    FeedApp mApp;


    public AppModule(FeedApp application){
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
    DiskCache provideFileCache(){
        try {
            return DiskCache.open(mApp.getApplicationContext().getCacheDir(), 1, C.DISK_CACHE_MAX_SIZE);
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Provides
    @Singleton
    MemoryCache provideMemoryCache(){
        return new MemoryCache();
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
