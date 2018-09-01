package com.miles.webimagedownloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;

class CachedBitmapCrawler {
    private final BitmapMemoryCache bitmapMemoryCache = BitmapMemoryCache.get();
    private final BitmapDiskCache bitmapDiskCache;
    private final String destination;
    private final String TAG = this.getClass().getName();

    public CachedBitmapCrawler(Context context) throws Exception {
        this.destination = context.getFilesDir().getAbsolutePath();
        bitmapDiskCache = BitmapDiskCache.Builder.get()
                .setDirectoryName(destination)
                .setFileName("cache")
                .setMaxCount(30)
                .create();
    }

    public Bitmap getBitmapWithUrl(String url) throws Exception {
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

        String destinationFilePath = destination + "/" + url.hashCode();
        File imageFile = WebImageDownloadClient.downloadFromUrl(url, destinationFilePath);
        bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        bitmapMemoryCache.put(url, bitmap);
        bitmapDiskCache.put(url, imageFile);
        return bitmap;
    }
}
