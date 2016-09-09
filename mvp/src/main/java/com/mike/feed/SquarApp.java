package com.mike.feed;

import android.app.Application;
import android.content.Context;

import com.mike.feed.dependency.injection.AppComponent;
import com.mike.feed.dependency.injection.AppModule;
import com.mike.feed.dependency.injection.DaggerAppComponent;

/**
 * Created by MinhNguyen on 8/23/16.
 */
public class SquarApp extends Application{

    public static SquarApp get(Context context){
        return (SquarApp) context.getApplicationContext();
    }


    AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this)).build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
