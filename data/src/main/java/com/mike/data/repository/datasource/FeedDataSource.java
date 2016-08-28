package com.mike.data.repository.datasource;

import com.mike.data.entity.DeletedEntity;
import com.mike.data.entity.FeedChangedInfoEntity;
import com.mike.data.entity.FeedEntity;
import com.mike.data.entity.WrittenEntity;

import rx.Observable;

/**
 * Created by MinhNguyen on 8/24/16.
 */
public interface FeedDataSource {

    Observable<FeedEntity> feedEntityByIndex(final int index);


    Observable<FeedChangedInfoEntity> registerFeedChangedEvent();


    Observable<WrittenEntity> writeFeed(FeedEntity entity);


    Observable<DeletedEntity> deleteFeed(String key);
}
