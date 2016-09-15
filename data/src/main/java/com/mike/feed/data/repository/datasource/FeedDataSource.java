package com.mike.feed.data.repository.datasource;

import com.mike.feed.data.entity.DeletedEntity;
import com.mike.feed.data.entity.FeedChangedInfoEntity;
import com.mike.feed.data.entity.FeedEntity;
import com.mike.feed.data.entity.WrittenEntity;

import rx.Observable;

/**
 * Created by MinhNguyen on 8/24/16.
 */
public interface FeedDataSource {

    Observable<FeedChangedInfoEntity> registerFeedChangedEvent();


    Observable<WrittenEntity> writeFeed(FeedEntity entity);


    Observable<DeletedEntity> deleteFeed(String key);
}
