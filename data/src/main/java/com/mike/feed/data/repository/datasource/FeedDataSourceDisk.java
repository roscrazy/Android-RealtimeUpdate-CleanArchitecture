package com.mike.feed.data.repository.datasource;

import com.mike.feed.data.cache.FeedCache;
import com.mike.feed.data.entity.DeletedEntity;
import com.mike.feed.data.entity.FeedChangedInfoEntity;
import com.mike.feed.data.entity.FeedEntity;
import com.mike.feed.data.entity.WrittenEntity;

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
