package com.miles.webimagedownloader;

import android.graphics.Bitmap;

public interface BitmapCache {
    void put(String key, Bitmap bitmap);
    Bitmap get(String key);
}
