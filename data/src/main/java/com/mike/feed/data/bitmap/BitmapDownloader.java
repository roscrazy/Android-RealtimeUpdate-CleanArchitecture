package com.mike.feed.data.bitmap;

import android.graphics.Bitmap;

import com.mike.feed.data.cache.FileCache;
import com.mike.feed.data.util.Util;
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
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;
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
        return this.downloadBitmap(url).map(new Func1<File, Bitmap>() {
            @Override
            public Bitmap call(File file) {
                return BitmapUtil.loadBitMapFromFile(file, reqWidth, reqHeight);
            }
        });
    }


    private Observable<File> downloadBitmap(final String strUrl) {
        return Observable.fromCallable(new Callable<File>() {
            @Override
            public File call() throws Exception {
                return download(strUrl);
            }
        });

    }

    /**
     * This is a simple method to download the image file with URLConnection
     * In a real app, we can use many other framwork which support token, session... For example retrofit, ..
     *
     * @param strUrl
     * @return
     */
    File download(final String strUrl) throws IOException {
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

                return cacheFile;
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
            throw e;
        }
    }


}
