package com.mike.feed.domain.repository;

import com.mike.feed.domain.Deleted;
import com.mike.feed.domain.Feed;
import com.mike.feed.domain.FeedChangedInfo;
import com.mike.feed.domain.Written;

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
