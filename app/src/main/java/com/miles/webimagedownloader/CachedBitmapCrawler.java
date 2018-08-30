package com.miles.webimagedownloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;

class CachedBitmapCrawler {
    private final BitmapCache bitmapCache = BitmapCache.BITMAP_CACHE;
    private final WebImageDownloadClient webImageDownloadClient = new WebImageDownloadClient();
    private final String TAG = this.getClass().getName();

    public Bitmap getBitmapWithUrl(String url, String destination) throws Exception {
        Bitmap bitmap = bitmapCache.getBitmapFromCache(url);
        if (bitmap != null) {
            Log.i(TAG, "Bitmap memory cache hit");
            return bitmap;
        }

        File imageFile = webImageDownloadClient.downloadFromUrl(url, destination);
        bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        bitmapCache.putBitmapToCache(url, bitmap);
        return bitmap;
    }
}
