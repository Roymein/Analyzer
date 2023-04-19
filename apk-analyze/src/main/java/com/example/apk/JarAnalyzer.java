package com.example.apk;

import com.example.d2j.util.zip.ZipEntry;
import com.example.d2j.util.zip.ZipFile;
import org.objectweb.asm.ClassReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.TreeMap;

public class JarAnalyzer {

    public static Map<String, ClassReader> input(File jar) {
        Map<String, ClassReader> classes = new TreeMap<>();
        ZipFile zipFile = null;
        try {
            byte[] bytes = Files.readAllBytes(jar.toPath());
            zipFile = new ZipFile(bytes);
            for (ZipEntry classEntry : zipFile.entries()) {
                if (classEntry.getName().endsWith(".class")) {
                    ClassReader classReader = new ClassReader(zipFile.getInputStream(classEntry));
                    classes.put(classReaderNameToAsmName(classReader), classReader);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return classes;
    }

    private static String classReaderNameToAsmName(ClassReader classReader) {
        if (classReader == null) {
            return null;
        } else {
            return classReader.getClassName();
        }
    }
}
