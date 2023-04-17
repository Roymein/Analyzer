package com.example.test;

import com.example.analyze.apk.ApkReader;
import org.junit.Test;

import java.io.InputStream;

public class ApkReaderTest {

    @Test
    public void test() {
        InputStream inputStream = XmlParserTest.class.getClassLoader().getResourceAsStream("CloudMusic-paidversion-debug.apk");
        if (inputStream == null) {
            System.err.println(" input stream is null");
            return;
        }
        ApkReader apkReader = new ApkReader(inputStream);

    }
}
