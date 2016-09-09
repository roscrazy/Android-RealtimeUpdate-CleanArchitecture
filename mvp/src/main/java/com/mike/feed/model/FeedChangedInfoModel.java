package com.mike.feed.model;

/**
 * Created by MinhNguyen on 8/24/16.
 */
public class FeedChangedInfoModel {
    public enum EventType { Added, Changed, Removed, Moved }

    private EventType type;
    private String previousChildKey;
    private String key;
    private FeedModel feed;

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String getPreviousChildKey() {
        return previousChildKey;
    }

    public void setPreviousChildKey(String previousChildKey) {
        this.previousChildKey = previousChildKey;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public FeedModel getFeed() {
        return feed;
    }

    public void setFeed(FeedModel feed) {
        this.feed = feed;
    }
}
