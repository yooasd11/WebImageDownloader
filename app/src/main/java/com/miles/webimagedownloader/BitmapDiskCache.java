package com.miles.webimagedownloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

public class BitmapDiskCache {
    private final DiskCache diskCache;
    private BitmapDiskCache(String directoryName, String fileName, int maxSize) throws Exception {
        diskCache = DiskCache.Builder.get()
                .setDirectory(directoryName)
                .setFileName(fileName)
                .setMaxSize(maxSize)
                .create();
        diskCache.open();
    }

    public static class Builder {
        private String directoryName;
        private String fileName;
        private int maxSize;
        private Builder() {}

        public static Builder get() {
            return new Builder();
        }

        public Builder setDirectoryName(String directoryName) {
            this.directoryName = directoryName;
            return this;
        }

        public Builder setFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder setMaxSize(int maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        public BitmapDiskCache create() throws Exception {
            return new BitmapDiskCache(directoryName, fileName, maxSize);
        }
    }

    public void put(String key, File file) {
        diskCache.put(key, file);
    }

    public Bitmap get(String key) {
        File bitmapFile = diskCache.get(key);
        if (bitmapFile != null && bitmapFile.exists() && bitmapFile.isFile()) {
            return BitmapFactory.decodeFile(bitmapFile.getAbsolutePath());
        }
        return null;
    }
}
