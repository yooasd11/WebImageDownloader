package com.miles.webimagedownloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

public class BitmapDiskCache {
    private final DiskLruCache diskLruCache;
    private BitmapDiskCache(String directoryName, String fileName, int maxCount, long kilobytes) throws Exception {
        diskLruCache = DiskLruCache.Builder.get()
                .setDirectory(directoryName)
                .setFileName(fileName)
                .setMaxCount(maxCount)
                .setMaxSize(kilobytes)
                .create();
        diskLruCache.open();
    }

    public static class Builder {
        private String directoryName;
        private String fileName;
        private int maxCount;
        private long maxSize;
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

        public Builder setMaxCount(int maxCount) {
            this.maxCount = maxCount;
            return this;
        }

        public Builder setMaxSize(long kilobytes) {
            this.maxSize = kilobytes;
            return this;
        }

        public BitmapDiskCache create() throws Exception {
            return new BitmapDiskCache(directoryName, fileName, maxCount, maxSize);
        }
    }

    public void put(String key, File file) {
        diskLruCache.put(key, file);
    }

    public Bitmap get(String key) {
        File bitmapFile = diskLruCache.get(key);
        if (bitmapFile != null && bitmapFile.exists() && bitmapFile.isFile()) {
            return BitmapFactory.decodeFile(bitmapFile.getAbsolutePath());
        }
        return null;
    }
}
