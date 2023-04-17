package test;

import com.example.apk.ApkReader;
import org.junit.Test;

import java.io.InputStream;

public class ApkReaderTest {

    @Test
    public void test() {
        InputStream inputStream = ApkReaderTest.class.getClassLoader().getResourceAsStream("CloudMusic-paidversion-debug.apk");
        if (inputStream == null) {
            System.err.println(" input stream is null");
            return;
        }
        ApkReader apkReader = new ApkReader(inputStream);

    }
}
