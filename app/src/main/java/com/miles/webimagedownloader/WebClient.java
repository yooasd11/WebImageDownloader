package com.miles.webimagedownloader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

class WebClient {
    private URL url;
    private URLConnection urlConnection;

    WebClient() {
    }

    WebClient(String stringUrl) throws Exception {
        this.url = new URL(stringUrl);
        this.urlConnection = url.openConnection();
    }

    public void setUrl(String stringUrl) throws Exception {
        this.url = new URL(stringUrl);
        this.urlConnection = url.openConnection();
    }

    public File downloadFromUrl(String destination) throws Exception {
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
