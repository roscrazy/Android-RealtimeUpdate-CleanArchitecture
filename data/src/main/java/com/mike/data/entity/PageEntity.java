package com.mike.data.entity;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * Created by MinhNguyen on 8/24/16.
 */
@IgnoreExtraProperties
public class PageEntity {
    private int total;
    private int lastIndex;

    // Consider to use an sparearrays for saving memory
    private List<FeedEntity> feeds;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getLastIndex() {
        return lastIndex;
    }

    public void setLastIndex(int lastIndex) {
        this.lastIndex = lastIndex;
    }

    public List<FeedEntity> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<FeedEntity> feeds) {
        this.feeds = feeds;
    }
}
