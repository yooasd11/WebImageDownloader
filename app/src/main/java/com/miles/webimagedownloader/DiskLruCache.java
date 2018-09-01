package com.miles.webimagedownloader;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class DiskLruCache {
    private final File directory;
    private final File data;
    private final int maxCount;
    private final long maxSize;
    private final HashMap<String, Entry> hashMap = new HashMap<>();
    private boolean corrunted = false;
    private long totalSize = 0;
    private int count = 0;

    private DiskLruCache(File directory, File data, int maxCount, long maxSize) {
        this.directory = directory;
        this.data = data;
        this.maxCount = maxCount;
        this.maxSize = maxSize;
    }

    public static class Builder {
        private static Builder INSTANCE = new Builder();
        private String directoryName;
        private String fileName;
        private int maxCount;
        private long maxSize;

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

        public Builder setMaxCount(int maxCount) {
            this.maxCount = maxCount;
            return this;
        }

        public Builder setMaxSize(int maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        public DiskLruCache create() throws Exception {
            File directory = new File(directoryName);
            File file = new File(directory, fileName);

            if (directory.exists() && !directory.isDirectory()) {
                throw new Exception(directoryName + " already exists.");
            } else if (!directory.exists() && !directory.mkdirs()) {
                throw new Exception(directoryName + " creation failed.");
            }

            if (file.exists() && file.isDirectory()) {
                throw new Exception(fileName + " already exists and is directory.");
            } else if (!file.exists() && !file.createNewFile()) {
                throw new Exception(fileName + " creation failed.");
            }

            if (maxCount <= 0) {
                throw new IllegalArgumentException("maxCount <= 0");
            }

            return new DiskLruCache(directory, file, maxCount, maxSize);
        }
    }

    public void open() {
        FileInputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = new FileInputStream(data);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String[] rawHeader = reader.readLine().split("#");
            this.count = Integer.parseInt(rawHeader[0]);
            this.totalSize = Long.parseLong(rawHeader[1]);
            for (int i = 0; i < count; i++) {
                String[] rawItem = reader.readLine().split("@");
                File file = new File(directory, rawItem[1]);
                if (file.exists() && file.isFile()) {
                    Entry newEntry = new Entry(rawItem[0], file);
                    hashMap.put(rawItem[0], newEntry);
                } else {
                    this.corrunted = true;
                }
            }
        } catch (Exception e) {
            init();
        } finally {
            close(inputStream);
            close(reader);
        }
    }

    private void close(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized private void init() {
        try {
            if (!data.createNewFile()) {
                throw new Exception("Cache data file creation failed");
            }
            commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized public void put(String key, File file) {
        try {
            hashMap.put(key, new Entry(key, file));
            totalSize += file.length() / 1024;
            commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File get(String key) {
        Entry entry = hashMap.get(key);
        if (entry != null) {
            return entry.getFile();
        }
        return null;
    }

    synchronized private void commit() throws Exception {
        if (directory == null || !directory.exists() || !directory.isDirectory() || data == null || !data.exists() || !data.isFile()) {
            throw new FileNotFoundException();
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(hashMap.size())
                .append("#")
                .append(totalSize)
                .append(System.lineSeparator());

        for (String key : hashMap.keySet()) {
            Entry entry = hashMap.get(key);
            if (entry != null) {
                stringBuilder.append(key)
                        .append("@")
                        .append(entry.getFile().getName())
                        .append(System.lineSeparator());
            }
        }

        FileWriter writer = new FileWriter(data);
        writer.write(stringBuilder.toString());
        close(writer);
    }

    private class Entry {
        private String url;
        private File file;

        private Entry(String url, File file) {
            this.url = url;
            this.file = file;
        }

        public String getUrl() {
            return url;
        }

        public File getFile() {
            return file;
        }
    }
}
