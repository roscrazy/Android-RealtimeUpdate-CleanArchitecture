package com.mike.data.entity.mapper;

import com.mike.data.entity.BitmapItemEntity;
import com.mike.domain.BitmapItem;

import javax.inject.Inject;

/**
 * Created by MinhNguyen on 8/27/16.
 */
public class BitmapDataMapper {

    @Inject
    public BitmapDataMapper() {
    }

    public BitmapItem transform(BitmapItemEntity entity){
        BitmapItem item = new BitmapItem();
        item.setBitmap(entity.getBitmap());
        return item;
    }
}
