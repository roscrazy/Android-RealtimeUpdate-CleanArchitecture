package com.mike.domain.repository;

import com.mike.domain.Deleted;
import com.mike.domain.Feed;
import com.mike.domain.FeedChangedInfo;
import com.mike.domain.Written;

import rx.Observable;

/**
 * Created by MinhNguyen on 8/24/16.
 */
public interface FeedRepository {

    public Observable<Feed> getFeed(int index);

    public Observable<FeedChangedInfo> registerFeedChangedEvent();

    public Observable<Written> writeFeed(Feed feed);

    public Observable<Deleted> deleteFeed(String key);

}
