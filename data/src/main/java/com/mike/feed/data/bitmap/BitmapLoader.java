package com.mike.feed.data.bitmap;

import android.graphics.Bitmap;

import com.mike.feed.data.cache.FileCache;
import com.mike.utility.BitmapUtil;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by MinhNguyen on 8/27/16.
 */

@Singleton
public class BitmapLoader {
    FileCache mFileCache;

    @Inject
    public BitmapLoader(FileCache mFileCache) {
        this.mFileCache = mFileCache;
    }


    public Observable<Bitmap> loadBitmap(final String url, final int reqWidth, final int reqHeight) {
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                if (mFileCache.hasFile(url)) {
                    File image = mFileCache.getFile(url);

                    Bitmap bitmap = BitmapUtil.loadBitMapFromFile(image, reqWidth, reqHeight);
                    subscriber.onNext(bitmap);
                    subscriber.onCompleted();

                } else {
                    throw new RuntimeException("File does not exist in cache");
                }
            }
        });
    }

}
