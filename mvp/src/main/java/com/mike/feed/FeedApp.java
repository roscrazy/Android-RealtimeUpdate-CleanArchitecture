package com.mike.feed;

import android.app.Application;
import android.content.Context;

import com.mike.feed.dependency.injection.AppComponent;
import com.mike.feed.dependency.injection.AppModule;
import com.mike.feed.dependency.injection.DaggerAppComponent;

import timber.log.Timber;

/**
 * Created by MinhNguyen on 8/23/16.
 */
public class FeedApp extends Application{

    public static FeedApp get(Context context){
        return (FeedApp) context.getApplicationContext();
    }


    AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this)).build();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
