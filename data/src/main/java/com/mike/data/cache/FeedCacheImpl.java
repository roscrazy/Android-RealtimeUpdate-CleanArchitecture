package com.mike.data.cache;

import com.mike.data.entity.FeedChangedInfoEntity;
import com.mike.data.entity.FeedEntity;
import com.mike.data.entity.PageEntity;

import rx.Observable;

/**
 * Created by MinhNguyen on 8/24/16.
 */
public class FeedCacheImpl implements FeedCache {


    public FeedCacheImpl(){}

    @Override
    public Observable<PageEntity> page(int lastIndex, int itemPerPage) {
        return null;
    }

    @Override
    public Observable<FeedEntity> userEntityByIndex(int index) {
        return null;
    }

    @Override
    public Observable<FeedChangedInfoEntity> registerFeedChangedEvent() {
        return null;
    }
}
