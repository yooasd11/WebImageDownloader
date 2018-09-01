package com.miles.webimagedownloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;

class CachedBitmapCrawler {
    private final BitmapMemoryCache bitmapMemoryCache = BitmapMemoryCache.get();
    private final BitmapDiskCache bitmapDiskCache;
    private final WebImageDownloadClient webImageDownloadClient = new WebImageDownloadClient();
    private final String TAG = this.getClass().getName();

    public CachedBitmapCrawler(Context context) throws Exception {
        bitmapDiskCache = BitmapDiskCache.Builder.get()
                .setDirectoryName(context.getFilesDir().getAbsolutePath())
                .setFileName("cache")
                .setMaxSize(30)
                .create();
    }

    public CachedBitmapCrawler(String dishCachDirectory) throws Exception {
        bitmapDiskCache = BitmapDiskCache.Builder.get()
                .setDirectoryName(dishCachDirectory)
                .setFileName("cache")
                .setMaxSize(30)
                .create();
    }

    public Bitmap getBitmapWithUrl(String url, String destination) throws Exception {
        Bitmap bitmap = bitmapMemoryCache.get(url);
        if (bitmap != null) {
            Log.i(TAG, "Bitmap memory cache hit");
            return bitmap;
        }

        bitmap = bitmapDiskCache.get(url);
        if (bitmap != null) {
            Log.i(TAG, "Bitmap disk cache hit");
            bitmapMemoryCache.put(url, bitmap);
            return bitmap;
        }

        File imageFile = webImageDownloadClient.downloadFromUrl(url, destination);
        bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        bitmapMemoryCache.put(url, bitmap);
        bitmapDiskCache.put(url, imageFile);
        return bitmap;
    }
}
