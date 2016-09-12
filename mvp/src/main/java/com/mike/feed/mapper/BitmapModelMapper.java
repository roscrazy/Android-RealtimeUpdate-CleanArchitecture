package com.mike.feed.mapper;

import com.mike.feed.domain.BitmapItem;
import com.mike.feed.model.BitmapModel;

import javax.inject.Inject;

/**
 * Created by MinhNguyen on 8/27/16.
 */
public class BitmapModelMapper {

    @Inject
    public BitmapModelMapper() {
    }

    public BitmapModel transform(BitmapItem bitmapItem){
        BitmapModel bitmapModel = new BitmapModel();
        bitmapModel.setBitmap(bitmapItem.getBitmap());
        return bitmapModel;
    }


}
