package com.mike.feed.mapper;

import android.graphics.Bitmap;

import com.mike.feed.domain.BitmapItem;
import com.mike.feed.model.BitmapModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

/**
 * Created by MinhNguyen on 9/12/16.
 */
public class BitmapModelMapperTest {
    BitmapModelMapper bitmapModelMapper;

    @Mock
    private Bitmap bitmap;

    @Before
    public void setUp(){
        bitmapModelMapper = new BitmapModelMapper();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void transformShouldConvertEntityToModel(){
        BitmapModel bitmapModel = bitmapModelMapper.transform(createDummyBitmapItem());
        assertThat(bitmapModel, is(instanceOf(BitmapModel.class)));
        Assert.assertThat(bitmap, is(bitmapModel.getBitmap()));
    }


    public BitmapItem createDummyBitmapItem(){
        BitmapItem bitmapItem = new BitmapItem();
        bitmapItem.setBitmap(bitmap);

        return bitmapItem;
    }


}