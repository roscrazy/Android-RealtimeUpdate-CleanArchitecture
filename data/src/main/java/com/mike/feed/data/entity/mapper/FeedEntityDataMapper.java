package com.mike.feed.data.entity.mapper;

import com.mike.feed.data.entity.DeletedEntity;
import com.mike.feed.data.entity.FeedChangedInfoEntity;
import com.mike.feed.data.entity.FeedEntity;
import com.mike.feed.data.entity.WrittenEntity;
import com.mike.feed.domain.Deleted;
import com.mike.feed.domain.Feed;
import com.mike.feed.domain.FeedChangedInfo;
import com.mike.feed.domain.Written;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by MinhNguyen on 8/24/16.
 */
@Singleton
public class FeedEntityDataMapper {


    @Inject
    public FeedEntityDataMapper(){

    }

    public Feed transform(FeedEntity entity){
        Feed feed = new Feed();

        feed.setImage(entity.getImage());
        feed.setBody(entity.getBody());
        feed.setTitle(entity.getTitle());
        feed.setIndex(entity.getIndex());
        feed.setKey(entity.getKey());

        return feed;
    }


    public FeedChangedInfo transform(FeedChangedInfoEntity entity){
        FeedChangedInfo result = new FeedChangedInfo();
        result.setPreviousChildKey(entity.getPreviousChildKey());
        result.setFeed(transform(entity.getFeedEntity()));
        result.setKey(entity.getKey());

        switch (entity.getType()){
            case Added:
                result.setType(FeedChangedInfo.EventType.Added);
                break;
            case Changed:
                result.setType(FeedChangedInfo.EventType.Changed);
                break;
            case Moved:
                result.setType(FeedChangedInfo.EventType.Moved);
                break;

            case Removed:
                result.setType(FeedChangedInfo.EventType.Removed);
                break;
        }

        return result;
    }

    public Written transform(WrittenEntity entity){
        Written result = new Written();
        // maybe add data later
        return result;
    }


    public Deleted transform(DeletedEntity entity){
        Deleted result = new Deleted();
        // maybe add data later
        return result;
    }



    public FeedEntity transform(Feed feed){
        FeedEntity entity = new FeedEntity();

        entity.setTitle(feed.getTitle());
        entity.setImage(feed.getImage());
        entity.setBody(feed.getBody());
        return entity;
    }

}
