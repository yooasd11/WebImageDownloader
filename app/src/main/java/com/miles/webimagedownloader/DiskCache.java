package com.miles.webimagedownloader;

import java.io.File;
import java.io.FileInputStream;

public class DiskCache {
    private final File directory;
    private final File data;
    private final int maxSize;

    private DiskCache(File directory, File data, int maxSize) {
        this.directory = directory;
        this.data = data;
        this.maxSize = maxSize;
    }

    public static class Builder {
        private static Builder INSTANCE = new Builder();
        private String directoryName;
        private String fileName;
        private int maxSize;

        public static Builder get() {
            return INSTANCE;
        }

        public Builder setDirectory(String directoryName) {
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

        public DiskCache create() throws Exception {
            File directory = new File(directoryName);
            File file = new File(directory, fileName);

            if (directory.exists() && !directory.isDirectory()) {
                throw new Exception(directoryName + " already exists.");
            } else if (!directory.exists() && !directory.mkdirs()) {
                throw new Exception(directoryName + " creation failed.");
            }

            if (file.exists() && file.isDirectory()) {
                throw new Exception(fileName + " already exists and is directory.");
            } else if (!file.exists() && file.createNewFile()) {
                throw new Exception(fileName + " creation failed.");
            }

            if (maxSize <= 0) {
                throw new IllegalArgumentException("maxSize <= 0");
            }

            return new DiskCache(directory, file, maxSize);
        }
    }

    public void open() {
        read();
    }

    private void read() {
        try {
            FileInputStream inputStream = new FileInputStream(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
