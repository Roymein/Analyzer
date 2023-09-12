package test;

import com.example.apk.ApkDexAnalyzer;
import com.example.apk.ApkReader;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ApkReaderTest {

    @Test
    public void test() {
        InputStream inputStream = ApkReaderTest.class.getClassLoader().getResourceAsStream("CloudMusic-paidversion-debug.apk");
        if (inputStream == null) {
            System.err.println(" input stream is null");
            return;
        }
        try {
            ApkReader apkReader = new ApkReader("CloudMusic-paidversion-debug", inputStream.readAllBytes(), new File("D:\\out"));
            apkReader.accept(null);
        } catch (IOException ignore) {
        }
    }

    @Test
    public void testArg() throws Exception {
        long start = System.currentTimeMillis();
        URL url = ApkReaderTest.class.getClassLoader().getResource("CloudMusic-paidversion-debug.apk");
        if (url == null) {
            System.err.println(" input stream is null");
            return;
        }

        ApkDexAnalyzer.main("-f", url.getFile(), "-o", new File("D:\\out").toString());
        long end = System.currentTimeMillis();

        System.err.println("consume time: " + (end - start));
    }
}
