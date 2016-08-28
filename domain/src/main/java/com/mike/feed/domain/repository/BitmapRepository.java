package com.mike.feed.domain.repository;

import com.mike.feed.domain.BitmapItem;

import rx.Observable;

/**
 * Created by MinhNguyen on 8/27/16.
 */
public interface BitmapRepository {

    Observable<BitmapItem> loadBitmap(String url, int width, int heigth);

}
