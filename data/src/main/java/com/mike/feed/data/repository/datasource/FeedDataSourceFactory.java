package com.mike.feed.data.repository.datasource;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mike.feed.data.cache.FeedCache;
import com.mike.feed.data.firebase.Firebase;
import com.mike.feed.data.firebase.FirebaseImpl;
import com.mike.utility.OSUtil;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by MinhNguyen on 8/24/16.
 */
@Singleton
public class FeedDataSourceFactory {
    private final Context context;
    private final FeedCache cache;

    FeedDataSourceCloud feedDataSourceCloud;

    @Inject
    public FeedDataSourceFactory(@NonNull Context context, @NonNull FeedCache cache) {
        this.context = context.getApplicationContext();
        this.cache = cache;
    }


    public FeedDataSource create() {
        if (OSUtil.isNetworkAvailable(context)) {
            return createCloudDataSource();
        } else {
            return createDiskDataSource();
        }

    }

    public FeedDataSource createDiskDataSource() {
        return new FeedDataSourceDisk(cache);
    }

    public FeedDataSource createCloudDataSource() {
        if(feedDataSourceCloud == null) {
            DatabaseReference databse = FirebaseDatabase.getInstance().getReference();
            Firebase fireBase = new FirebaseImpl(databse);
            feedDataSourceCloud = new FeedDataSourceCloud(fireBase);
        }

        return feedDataSourceCloud;
    }


}
