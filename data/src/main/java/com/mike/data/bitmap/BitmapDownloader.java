package com.mike.data.bitmap;

import android.graphics.Bitmap;

import com.mike.data.cache.FileCache;
import com.mike.data.util.Util;
import com.mike.utility.BitmapUtil;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by MinhNguyen on 8/27/16.
 */
public class BitmapDownloader {

    private static final int REQUEST_TIME_OUT = 30000;
    private static final String HTTP_GET = "GET";

    FileCache mFileCache;

    @Inject
    public BitmapDownloader(FileCache fileCache) {
        this.mFileCache = fileCache;
    }



    public Observable<Bitmap> downloadBitmap(String url, final int reqWidth, final int reqHeight) {
        return this.downloadBitmap(url).map(new Func1<String, Bitmap>() {
            @Override
            public Bitmap call(String s) {
                File file = new File(s);
                return BitmapUtil.loadBitMapFromFile(file, reqWidth, reqHeight);
            }
        });
    }

    /**
     * This is a simple method to download the image file with URLConnection
     * In a real app, we can use many other framwork which support token, session... For example retrofit, ..
     * @param strUrl
     * @return
     */
    private Observable<String> downloadBitmap(final String strUrl) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                BufferedOutputStream bufferedOutputStream = null;
                DataInputStream inputStream = null;
                OutputStream connectOutputStream = null;

                try {

                    try {
                        URL url = URI.create(strUrl).toURL();

                        URLConnection conn = url.openConnection();

                        if (conn instanceof HttpsURLConnection) {
                            HttpsURLConnection urlConnection = (HttpsURLConnection) conn;
                            // Not recommend to support all https certificate
                            urlConnection.setHostnameVerifier(new AllowAllHostnameVerifier());
                            urlConnection.setRequestMethod(HTTP_GET);
                        } else if (conn instanceof HttpURLConnection) {
                            ((HttpURLConnection) conn).setRequestMethod(HTTP_GET);
                        }

                        conn.setDoInput(true);
                        conn.setConnectTimeout(REQUEST_TIME_OUT);
                        conn.setReadTimeout(REQUEST_TIME_OUT);
                        conn.connect();
                        inputStream = new DataInputStream(conn.getInputStream());
                        File cacheFile = mFileCache.getFile(strUrl);
                        bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(cacheFile));

                        Util.copyStream(inputStream, bufferedOutputStream);
                    } finally {
                        if (connectOutputStream != null) {
                            connectOutputStream.close();
                        }

                        //release resource
                        if (bufferedOutputStream != null) {
                            bufferedOutputStream.close();
                        }

                        if (inputStream != null)
                            inputStream.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }



}
