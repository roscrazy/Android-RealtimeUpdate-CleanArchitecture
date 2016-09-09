package com.mike.feed.mapper;

import com.mike.feed.domain.Deleted;
import com.mike.feed.domain.Feed;
import com.mike.feed.domain.FeedChangedInfo;
import com.mike.feed.domain.Written;
import com.mike.feed.model.DeletedModel;
import com.mike.feed.model.FeedChangedInfoModel;
import com.mike.feed.model.FeedModel;
import com.mike.feed.model.WrittenModel;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by MinhNguyen on 8/24/16.
 */
@Singleton
public class FeedModelMapper {


    @Inject
    public FeedModelMapper(){

    }

    public FeedModel transform(Feed entity){
        FeedModel feed = new FeedModel();

        feed.setImage(entity.getImage());
        feed.setBody(entity.getBody());
        feed.setTitle(entity.getTitle());
        feed.setIndex(entity.getIndex());

        return feed;
    }


    public FeedChangedInfoModel transform(FeedChangedInfo entity){
        FeedChangedInfoModel result = new FeedChangedInfoModel();
        result.setKey(entity.getKey());
        result.setPreviousChildKey(entity.getPreviousChildKey());
        result.setFeed(this.transform(entity.getFeed()));

        switch (entity.getType()){
            case Added:
                result.setType(FeedChangedInfoModel.EventType.Added);
                break;
            case Changed:
                result.setType(FeedChangedInfoModel.EventType.Changed);
                break;
            case Moved:
                result.setType(FeedChangedInfoModel.EventType.Moved);
                break;

            case Removed:
                result.setType(FeedChangedInfoModel.EventType.Removed);
                break;
        }

        return result;
    }

    public WrittenModel transform(Written entity){
        WrittenModel result = new WrittenModel();
        // maybe add data later
        return result;
    }

    public DeletedModel transform(Deleted entity){
        DeletedModel result = new DeletedModel();
        // maybe add data later
        return result;
    }

}
