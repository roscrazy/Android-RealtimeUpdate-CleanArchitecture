package com.mike.feed.domain;

/**
 * Created by MinhNguyen on 8/24/16.
 */
public class FeedChangedInfo {
    public enum EventType { Added, Changed, Removed, Moved }

    private EventType type;
    private String previousChildKey;
    private String key;
    private Feed feed;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

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

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }
}
