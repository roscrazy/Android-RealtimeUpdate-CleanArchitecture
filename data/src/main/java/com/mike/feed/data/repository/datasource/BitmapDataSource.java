package com.mike.feed.data.repository.datasource;

import com.mike.feed.data.entity.BitmapItemEntity;

import rx.Observable;

/**
 * Created by MinhNguyen on 8/27/16.
 */
public interface BitmapDataSource {

    Observable<BitmapItemEntity> loadBitmap(String url, int width, int heigth);

}
