package com.mike.data.firebase;

import com.mike.data.entity.DeletedEntity;
import com.mike.data.entity.FeedChangedInfoEntity;
import com.mike.data.entity.FeedEntity;
import com.mike.data.entity.WrittenEntity;

import rx.Observable;

/**
 * Created by MinhNguyen on 8/24/16.
 */
public interface Firebase {
    public static String BASE_QUERY = "https://mysquar-dc8fb.firebaseio.com/";

    // query path to get all feeds
    String QUERY_FEEDS = "feeds";

    /**
     * Retrieves an {@link rx.Observable} which will emit a {@link FeedEntity}.
     *
     * @param index The feed index used to get feed data.
     */
    Observable<FeedEntity> feedEntityByIndex(final int index);

    Observable<FeedChangedInfoEntity> registerFeedChangedEvent();

    Observable<WrittenEntity> writeFeed(FeedEntity entity);

    Observable<DeletedEntity> deleteFeed(String key);


}
