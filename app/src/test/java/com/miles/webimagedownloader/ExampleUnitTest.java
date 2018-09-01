package com.miles.webimagedownloader;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private DiskCache diskCache;
    private final String directory = "C:/1.Sources";

    @Before
    public void setup() {
        try {
            this.diskCache = DiskCache.Builder.get()
                    .setDirectory(directory)
                    .setFileName("cache")
                    .setMaxSize(30)
                    .create();
        } catch (Exception e) {
            e.printStackTrace();
        }
        diskCache.open();
    }

    @Test
    public void insertLine() {
        try {
            /*diskCache.writeHeader(1, 30);
            diskCache.writeHeader(2, 300);
            diskCache.writeHeader(30, 123901874);
            diskCache.appendLine("hello");
            diskCache.appendLine("zzz");
            diskCache.appendLine("test");
            diskCache.appendLine("writing line a lot");*/
            File file1 = new File(directory, "file1");
            file1.createNewFile();
            File file2 = new File(directory, "file2");
            file2.createNewFile();
            File file3 = new File(directory, "file3");
            file3.createNewFile();

            diskCache.put("key1" , file1);
            diskCache.put("key2" , file2);
            //diskCache.put("key3" , file3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}