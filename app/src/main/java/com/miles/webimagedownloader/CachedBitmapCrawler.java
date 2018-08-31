package com.miles.webimagedownloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;

class CachedBitmapCrawler {
    private final BitmapMemoryCache bitmapMemoryCache = BitmapMemoryCache.get();
    private final WebImageDownloadClient webImageDownloadClient = new WebImageDownloadClient();
    private final String TAG = this.getClass().getName();

    public Bitmap getBitmapWithUrl(String url, String destination) throws Exception {
        Bitmap bitmap = bitmapMemoryCache.get(url);
        if (bitmap != null) {
            Log.i(TAG, "Bitmap memory cache hit");
            return bitmap;
        }

        File imageFile = webImageDownloadClient.downloadFromUrl(url, destination);
        bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        bitmapMemoryCache.put(url, bitmap);
        return bitmap;
    }
}
