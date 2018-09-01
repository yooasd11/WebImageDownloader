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
    private DiskLruCache diskLruCache;
    private final String directory = "C:/Users/yooas/Desktop";

    @Before
    public void setup() {
        try {
            this.diskLruCache = DiskLruCache.Builder.get()
                    .setDirectory(directory)
                    .setFileName("cache")
                    .setMaxCount(30)
                    .create();
        } catch (Exception e) {
            e.printStackTrace();
        }
        diskLruCache.open();
    }

    @Test
    public void insertLine() {
        try {
            /*diskLruCache.writeHeader(1, 30);
            diskLruCache.writeHeader(2, 300);
            diskLruCache.writeHeader(30, 123901874);
            diskLruCache.appendLine("hello");
            diskLruCache.appendLine("zzz");
            diskLruCache.appendLine("test");
            diskLruCache.appendLine("writing line a lot");*/
            File file1 = new File(directory, "file1");
            file1.createNewFile();
            File file2 = new File(directory, "file2");
            file2.createNewFile();
            File file3 = new File(directory, "file3");
            file3.createNewFile();

            diskLruCache.put("key1" , file1);
            diskLruCache.put("key2" , file2);
            diskLruCache.put("key3" , file3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}