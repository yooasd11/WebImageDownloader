package com.miles.webimagedownloader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

class WebImageDownloadClient {
    public File downloadFromUrl(String stringUrl, String destination) throws Exception {
        URL url = new URL(stringUrl);
        URLConnection urlConnection = url.openConnection();
        File file = new File(destination);
        InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        byte[] buf = new byte[1024];
        int read;
        while ((read = inputStream.read(buf)) > 0) {
            fileOutputStream.write(buf, 0, read);
        }
        inputStream.close();
        fileOutputStream.close();
        return file;
    }
}
