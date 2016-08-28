package com.mike.feed.data.cache;

import android.content.Context;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FileCache {

    private File cacheDir;

    @Inject
    public FileCache(Context context) {
        cacheDir = context.getCacheDir();

        if (!cacheDir.exists())
            cacheDir.mkdirs();
    }

    public File getFile(String url) {
        //I identify images by hashcode. Not a perfect solution, good for the demo.
        String filename = String.valueOf(url.hashCode());
        //Another possible solution (thanks to grantland)
        //String filename = URLEncoder.encode(url);
        File f = new File(cacheDir, filename);
        return f;

    }

    public String getPath(String url) {
        String filename = String.valueOf(url.hashCode());
        File f = new File(cacheDir, filename);
        return f.getAbsolutePath();
    }


    public Boolean hasFile(String url) {
        //I identify images by hashcode. Not a perfect solution, good for the demo.
        String filename = String.valueOf(url.hashCode());
        //Another possible solution (thanks to grantland)
        //String filename = URLEncoder.encode(url);
        File f = new File(cacheDir, filename);
        return f.exists() && f.length() > 0;

    }


    public void remove(String key) {
        File f = getFile(key);
        if (f.exists())
            f.delete();
    }

    public void clear() {
        File[] files = cacheDir.listFiles();
        if (files == null)
            return;
        for (File f : files)
            f.delete();
    }

}