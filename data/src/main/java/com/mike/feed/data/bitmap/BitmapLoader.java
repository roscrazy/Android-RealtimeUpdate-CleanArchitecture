package com.mike.feed.data.bitmap;

import android.graphics.Bitmap;

import com.mike.utility.BitmapUtil;
import com.mike.utility.cache.DiskCache;

import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created by MinhNguyen on 8/27/16.
 */

@Singleton
public class BitmapLoader {
    DiskCache mDiskCache;

    @Inject
    public BitmapLoader(DiskCache mDiskCache) {
        this.mDiskCache = mDiskCache;
    }


    public Observable<Bitmap> loadBitmap(final String url, final int reqWidth, final int reqHeight) {

        return Observable.fromCallable(new Callable<Bitmap>() {
            @Override
            public Bitmap call() throws Exception {

                if (mDiskCache.contains(url)) {
                    return mDiskCache.getBitmap(url, reqWidth, reqHeight).getBitmap();
                };

                return null;
            }
        });

    }

}
