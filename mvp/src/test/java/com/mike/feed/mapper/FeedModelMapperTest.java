package com.mike.feed.mapper;

import com.mike.feed.domain.Deleted;
import com.mike.feed.domain.Feed;
import com.mike.feed.domain.FeedChangedInfo;
import com.mike.feed.domain.Written;
import com.mike.feed.model.DeletedModel;
import com.mike.feed.model.FeedChangedInfoModel;
import com.mike.feed.model.FeedModel;
import com.mike.feed.model.WrittenModel;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
import static org.junit.Assert.*;

/**
 * Created by MinhNguyen on 9/12/16.
 */
public class FeedModelMapperTest {

    private static final String FAKE_BODY = "this is body";
    private static final String FAKE_IMAGE_URL = "this is url";
    private static final String FAKE_TITLE = "this is title";
    private static final String FAKE_KEY = "key";
    private static final String FAKE_PREVIOUS_KEY = "PREVIOUS_KEY";
    private static final FeedChangedInfo.EventType FAKE_TYPE = FeedChangedInfo.EventType.Added;

    FeedModelMapper mapper;


    @Before
    public void setUp(){
        mapper = new FeedModelMapper();
    }


    @Test
    public void transformShouldConvertFeedToModel() throws Exception {
        FeedModel feedModel = mapper.transform(createDummyFeed());
        assertThat(feedModel, is(instanceOf(FeedModel.class)));

        assertThat(feedModel.getBody(), is(FAKE_BODY));
        assertThat(feedModel.getTitle(), is(FAKE_TITLE));
        assertThat(feedModel.getImage(), is(FAKE_IMAGE_URL));

    }

    @Test
    public void transformShouldTransformDeleteToModel() throws Exception {
        DeletedModel deleted = mapper.transform(createDummyDeleted());
        assertThat(deleted, is(instanceOf(DeletedModel.class)));
    }

    @Test
    public void transformShouldTransformWrittenToModel() throws Exception {
        WrittenModel written = mapper.transform(createDummyWritten());
        assertThat(written, is(instanceOf(WrittenModel.class)));
    }

    @Test
    public void transformShouldTransformFeedChangedInfoToModel() throws Exception {
        FeedChangedInfoModel infoModel = mapper.transform(createFeedChangedInfoModel());
        assertThat(infoModel, is(instanceOf(FeedChangedInfoModel.class)));

        assertThat(infoModel.getKey(), is(FAKE_KEY));
        assertThat(infoModel.getPreviousChildKey(), is(FAKE_PREVIOUS_KEY));
        assertThat(infoModel.getType(), is(FeedChangedInfoModel.EventType.Added));

        assertThat(infoModel.getFeed().getBody(), is(FAKE_BODY));
        assertThat(infoModel.getFeed().getTitle(), is(FAKE_TITLE));
        assertThat(infoModel.getFeed().getImage(), is(FAKE_IMAGE_URL));
    }



    private FeedChangedInfo createFeedChangedInfoModel(){
        FeedChangedInfo result = new FeedChangedInfo();
        result.setFeed(createDummyFeed());
        result.setKey(FAKE_KEY);
        result.setPreviousChildKey(FAKE_PREVIOUS_KEY);
        result.setType(FAKE_TYPE);

        return result;
    }

    private Written createDummyWritten(){
        return new Written();
    }


    private Deleted createDummyDeleted(){
        return new Deleted();
    }

    private Feed createDummyFeed(){
        Feed feed = new Feed();
        feed.setBody(FAKE_BODY);
        feed.setTitle(FAKE_TITLE);
        feed.setImage(FAKE_IMAGE_URL);
        return feed;
    }
}