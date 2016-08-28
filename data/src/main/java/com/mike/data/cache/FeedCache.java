package com.mike.data.cache;

import com.mike.data.entity.FeedChangedInfoEntity;
import com.mike.data.entity.FeedEntity;
import com.mike.data.entity.PageEntity;

import rx.Observable;

/**
 * Created by MinhNguyen on 8/24/16.
 */
public interface FeedCache {
    /**
     * Retrieves an {@link rx.Observable} which will emit a {@link PageEntity}.
     */
    Observable<PageEntity> page(int lastIndex, int itemPerPage);

    /**
     * Retrieves an {@link rx.Observable} which will emit a {@link FeedEntity}.
     *
     * @param index The feed index used to get feed data.
     */
    Observable<FeedEntity> userEntityByIndex(final int index);



    Observable<FeedChangedInfoEntity> registerFeedChangedEvent();
}
