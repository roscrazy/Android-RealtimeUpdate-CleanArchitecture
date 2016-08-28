package com.mike.feed.data.repository.datasource;

import android.graphics.Bitmap;

import com.mike.feed.data.bitmap.BitmapLoader;
import com.mike.feed.data.entity.BitmapItemEntity;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by MinhNguyen on 8/27/16.
 */
public class BitmapDataSourceDisk implements BitmapDataSource{

    final BitmapLoader mBitmapLoader;

    @Inject
    public BitmapDataSourceDisk(BitmapLoader mBitmapLoader) {
        this.mBitmapLoader = mBitmapLoader;
    }

    @Override
    public Observable<BitmapItemEntity> loadBitmap(String url, int width, int heigth) {
        return mBitmapLoader.loadBitmap(url, width, heigth).map(new Func1<Bitmap, BitmapItemEntity>() {
            @Override
            public BitmapItemEntity call(Bitmap bitmap) {
                BitmapItemEntity bitmapItem = new BitmapItemEntity();
                bitmapItem.setBitmap(bitmap);
                return bitmapItem;
            }
        });

    }


}
