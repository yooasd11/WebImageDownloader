package com.miles.webimagedownloader;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
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

    public void downloadFromUrl() throws Exception {
        InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
        readStream(inputStream);
    }

    private void readStream(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    }
}
