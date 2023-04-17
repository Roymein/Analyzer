package com.example.test;

import com.example.analyze.xml.XmlParser;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class XmlParserTest {

    @Test
    public void test_parse() throws IOException {
        InputStream inputStream = XmlParserTest.class.getClassLoader().getResourceAsStream("AndroidManifest.xml");
        XmlParser xmlParser = new XmlParser(inputStream);
        xmlParser.parse();
    }
}
