package com.mike.data.repository.datasource;

import com.mike.data.cache.FeedCache;
import com.mike.data.entity.DeletedEntity;
import com.mike.data.entity.FeedChangedInfoEntity;
import com.mike.data.entity.FeedEntity;
import com.mike.data.entity.WrittenEntity;

import rx.Observable;

/**
 * Created by MinhNguyen on 8/24/16.
 *
 * This class is not be used
 */
public class FeedDataSourceDisk implements FeedDataSource{
    FeedCache feedCache;

    public FeedDataSourceDisk(FeedCache feedCache) {
        this.feedCache = feedCache;
    }

    @Override
    public Observable<FeedEntity> feedEntityByIndex(int index) {
        return feedCache.userEntityByIndex(index);
    }

    @Override
    public Observable<FeedChangedInfoEntity> registerFeedChangedEvent() {
        return feedCache.registerFeedChangedEvent();
    }

    @Override
    public Observable<WrittenEntity> writeFeed(FeedEntity entity) {
        return null;
    }

    @Override
    public Observable<DeletedEntity> deleteFeed(String key) {
        return null;
    }
}
