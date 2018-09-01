package com.miles.webimagedownloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class DiskCache {
    private final File directory;
    private final File data;
    private final int maxSize;
    private final HashMap<String, Entry> hashMap = new HashMap<>();
    private boolean corrunted = false;
    private long totalSize = 0;
    private int count = 0;

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
            } else if (!file.exists() && !file.createNewFile()) {
                throw new Exception(fileName + " creation failed.");
            }

            if (maxSize <= 0) {
                throw new IllegalArgumentException("maxSize <= 0");
            }

            return new DiskCache(directory, file, maxSize);
        }
    }

    public void open() {
        try {
            readHeader();
        } catch (Exception e) {
            e.printStackTrace();
            init();
        }
    }

    private void readHeader() throws Exception {
        FileInputStream inputStream = new FileInputStream(data);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String[] rawHeader = reader.readLine().split("#");
        this.count = Integer.parseInt(rawHeader[0]);
        this.totalSize = Long.parseLong(rawHeader[1]);
        for (int i = 0; i < count; i++) {
            String[] rawItem = reader.readLine().split("@");
            File file = new File(rawItem[1]);
            if (file.exists() && file.isFile()) {
                Entry newEntry = new Entry(rawItem[0], file);
                hashMap.put(rawItem[0], newEntry);
            } else {
                this.corrunted = true;
            }
        }
    }

    private void init() {
        try {
            data.createNewFile();
            writeHeader(count, totalSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void put(String key, File file) {
        try {
            hashMap.put(key, new Entry(key, file));
            totalSize += file.length() / 1024;
            writeHeader(hashMap.size(), totalSize);
            appendLine(key + "@" + file.getAbsolutePath());
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

    private void writeHeader(int count, long totalSize) throws Exception {
        if (directory == null || !directory.exists() || !directory.isDirectory() || data == null || !data.exists() || !data.isFile()) {
            throw new FileNotFoundException();
        }

        FileWriter writer = new FileWriter(data, false);
        writer.write("" + count +"#" + totalSize);
        writer.write(System.lineSeparator());
        writer.close();
    }

    private void appendLine(String line) throws Exception {
        if (directory == null || !directory.exists() || !directory.isDirectory() || data == null || !data.exists() || !data.isFile()) {
            throw new FileNotFoundException();
        }

        FileWriter writer = new FileWriter(data, true);
        writer.append(line);
        writer.append(System.lineSeparator());
        writer.close();
    }

    private class Entry {
        private String url;
        private File file;

        public Entry(String url, File file) {
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
