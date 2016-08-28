package com.mike.feed.data.repository;

import com.mike.feed.data.entity.BitmapItemEntity;
import com.mike.feed.data.entity.mapper.BitmapDataMapper;
import com.mike.feed.data.repository.datasource.BitmapDataSourceFactory;
import com.mike.feed.domain.BitmapItem;
import com.mike.feed.domain.repository.BitmapRepository;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by MinhNguyen on 8/27/16.
 */
public class BitmapDataRepository implements BitmapRepository {

    BitmapDataSourceFactory mFactory;
    BitmapDataMapper mMapper;

    @Inject
    public BitmapDataRepository(BitmapDataSourceFactory mFactory, BitmapDataMapper mMapper) {
        this.mFactory = mFactory;
        this.mMapper = mMapper;
    }

    @Override
    public Observable<BitmapItem> loadBitmap(String url, int width, int heigth) {
        return mFactory.createDataSource(url).loadBitmap(url, width, heigth).map(new Func1<BitmapItemEntity, BitmapItem>() {
            @Override
            public BitmapItem call(BitmapItemEntity entity) {
                BitmapItem bitmapItem = new BitmapItem();
                bitmapItem.setBitmap(entity.getBitmap());

                return bitmapItem;
            }
        });
    }
}
