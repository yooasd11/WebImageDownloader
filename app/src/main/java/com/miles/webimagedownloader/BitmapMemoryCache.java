package com.miles.webimagedownloader;

import android.graphics.Bitmap;
import android.util.LruCache;

class BitmapMemoryCache {
    private final static BitmapMemoryCache INSTANCE = new BitmapMemoryCache();
    private BitmapMemoryCache() {}

    public static BitmapMemoryCache get() {
        return INSTANCE;
    }
    
    // 최대 런타임 메모리(킬로바이트)
    private final int MAX_MEMORY = (int) (Runtime.getRuntime().maxMemory() / 1024);
    // 캐시에 사용할 메모리(킬로바이트)
    private final int CACHE_SIZE = MAX_MEMORY / 16;
    private final LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(CACHE_SIZE) {
        @Override
        protected int sizeOf(String key, Bitmap value) {
            // 킬로바이트 단위로 캐시 사이즈 측
            return value.getByteCount() / 1024;
        }
    };

    public void put(String key, Bitmap bitmap) {
        if (get(key) == null) {
            lruCache.put(key, bitmap);
        }
    }

    public Bitmap get(String key) {
        return lruCache.get(key);
    }
}
