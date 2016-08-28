package com.mike.feed.data.entity;

/**
 * Created by MinhNguyen on 8/24/16.
 */
public class FeedChangedInfoEntity {
    private EventType type;
    private String previousChildKey;
    private String key;
    private FeedEntity feedEntity;

    public FeedChangedInfoEntity(EventType type, String previousChildKey, String key, FeedEntity feedEntity) {
        this.type = type;
        this.previousChildKey = previousChildKey;
        this.key = key;
        this.feedEntity = feedEntity;
    }

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

    public FeedEntity getFeedEntity() {
        return feedEntity;
    }

    public void setFeedEntity(FeedEntity feedEntity) {
        this.feedEntity = feedEntity;
    }
}
