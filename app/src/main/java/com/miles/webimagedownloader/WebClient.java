package com.miles.webimagedownloader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
        InputStreamReader reader = new InputStreamReader(inputStream);
        File file = new File(destination);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        char[] cBuf =  new char[1024];
        int read;
        while ((read = reader.read(cBuf)) > 0) {
            fileOutputStream.write(read);
        }
        inputStream.close();
        reader.close();
        fileOutputStream.close();
        return file;
    }
}
