package com.mike.feed.data.repository.datasource;

import com.mike.feed.data.cache.FileCache;

import javax.inject.Inject;

/**
 * Created by MinhNguyen on 8/27/16.
 */

public class BitmapDataSourceFactory {

    FileCache mFileCache;
    BitmapDataSourceCloud mDataSourceCloud;
    BitmapDataSourceDisk mDataSourceDisk;


    @Inject
    public BitmapDataSourceFactory(FileCache mFileCache, BitmapDataSourceCloud mDataSourceCloud, BitmapDataSourceDisk mDataSourceDisk) {
        this.mFileCache = mFileCache;
        this.mDataSourceCloud = mDataSourceCloud;
        this.mDataSourceDisk = mDataSourceDisk;
    }

    public BitmapDataSource createDataSource(String url){
        if(mFileCache.hasFile(url)){
            return mDataSourceDisk;
        }else{
            return mDataSourceCloud;
        }
    }
}
