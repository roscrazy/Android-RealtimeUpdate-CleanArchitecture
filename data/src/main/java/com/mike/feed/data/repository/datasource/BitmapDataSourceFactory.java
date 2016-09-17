package com.mike.feed.data.repository.datasource;

import com.mike.utility.cache.DiskCache;

import java.io.IOException;

import javax.inject.Inject;

/**
 * Created by MinhNguyen on 8/27/16.
 */

public class BitmapDataSourceFactory {

    DiskCache mDiskCache;
    BitmapDataSourceCloud mDataSourceCloud;
    BitmapDataSourceDisk mDataSourceDisk;


    @Inject
    public BitmapDataSourceFactory(DiskCache mDiskCache, BitmapDataSourceCloud mDataSourceCloud, BitmapDataSourceDisk mDataSourceDisk) {
        this.mDiskCache = mDiskCache;
        this.mDataSourceCloud = mDataSourceCloud;
        this.mDataSourceDisk = mDataSourceDisk;
    }

    public BitmapDataSource createDataSource(String url){
        boolean contain = false;
        try{
            contain = mDiskCache.contains(url);
        }catch (IOException e){
            // we will ignore cache error, and download the file from cloud
            e.printStackTrace();
        }

        if(contain){
            return mDataSourceDisk;
        }else{
            return mDataSourceCloud;
        }
    }
}
