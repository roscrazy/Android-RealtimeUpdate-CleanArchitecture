package com.mike.utility.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

public class MemoryCache {

    private static final String TAG = "MemoryCache";
    private final LruCache<String, Bitmap> mMemoryCache;


    public MemoryCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    MemoryCache(LruCache cache){
        this.mMemoryCache = cache;
    }


    public Bitmap get(String id) {
        return mMemoryCache.get(id);
    }

    public void put(String id, Bitmap bitmap) {
        mMemoryCache.put(id, bitmap);
    }
}