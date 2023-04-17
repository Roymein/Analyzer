package com.example.apk;

import com.example.d2j.util.zip.ZipEntry;
import com.example.d2j.util.zip.ZipFile;
import com.example.xml.XmlParser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class ApkReader {
    private static final String ANDROID_MANIFEST = "AndroidManifest.xml";
    private InputStream androidManifestXmlStream;

    public ApkReader(File apkFile) {
        try {
            byte[] bytes = Files.readAllBytes(apkFile.toPath());
            try (ZipFile zipFile = new ZipFile(bytes)) {
                androidManifestXmlStream = findAndroidManifest(zipFile);

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public ApkReader(InputStream inputStream) {
        try {
            byte[] bytes = inputStream.readAllBytes();
            try (ZipFile zipFile = new ZipFile(bytes)) {
                androidManifestXmlStream = findAndroidManifest(zipFile);
                XmlParser xmlParser = new XmlParser(androidManifestXmlStream);
                xmlParser.parse();
            }
        } catch (IOException ioException) {

        }
    }

    public void accept(final ApkVisitor apkVisitor) {

    }

    private InputStream findAndroidManifest(ZipFile zipFile) {
        ZipEntry entry = zipFile.findFirstEntry(ANDROID_MANIFEST);
        return entry == null ? null : zipFile.getInputStream(entry);
    }
}
