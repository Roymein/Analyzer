package com.example.apk;

import com.example.d2j.Dex2jarCmd;
import com.example.d2j.util.zip.ZipEntry;
import com.example.d2j.util.zip.ZipFile;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

import java.io.*;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

public class ApkReader {
    private static final String DIR_ROOT = "analyze";
    private static final String DIR_APK_ZIP_ENTRY = "gen";
    private static final String DIR_OUTPUT = "output";

    private static final String DEX_SUFFIX = ".dex";

    private final File apkDepressDir = new File(DIR_ROOT, DIR_APK_ZIP_ENTRY);
    private final File outDir = new File(DIR_ROOT, DIR_OUTPUT);

    private final byte[] mBytes;
    private final Map<File, File> fileTempMap = new TreeMap<>();
    private final Map<String, ClassReader> classes = new TreeMap<>();

    /**
     * byte[] bytes = Files.readAllBytes(apkFile.toPath());
     */
    public ApkReader(byte[] bytes) {
        this.mBytes = bytes;
    }

    /**
     * //todo modify ClassVisitor
     *
     * @param classVisitor
     */
    public void accept(final ClassVisitor classVisitor) {
        apkDecompress();
        analyzeCode(classVisitor);
    }

    private void analyzeCode(ClassVisitor classVisitor) {
        for (Map.Entry<File, File> fileFileEntry : fileTempMap.entrySet()) {
            File zipTempFile = fileFileEntry.getKey();
            File jarFile = fileFileEntry.getValue();
            if (zipTempFile.getName().endsWith(DEX_SUFFIX)) {
                try {
                    Dex2jarCmd.main(zipTempFile.getAbsolutePath(), "--output", jarFile.getAbsolutePath(), "--force");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Map<String, ClassReader> classReaderMap = JarAnalyzer.input(jarFile);
                for (Map.Entry<String, ClassReader> entry : classReaderMap.entrySet()) {
                    ClassReader classReader = entry.getValue();
                    classReader.accept(classVisitor, 0);
                }
            }
        }
    }

    private void apkDecompress() {
        Path currentDir = new File(".").toPath();
        try (ZipFile apkZipFile = new ZipFile(mBytes)) {
            for (ZipEntry apkEntry : apkZipFile.entries()) {
                String apkEntryName = apkEntry.getName();
                File entryFile = new File(apkDepressDir, apkEntryName);
                File targetFile = null;
                if (apkEntryName.endsWith(DEX_SUFFIX)) {
                    targetFile = currentDir.resolve(new File(apkDepressDir, getBaseName(apkEntryName) + "-dex2jar.jar").toPath()).toFile();
                } else {
                    targetFile = entryFile;
                }

                fileTempMap.put(entryFile, targetFile);

                File parentFile = entryFile.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                if (entryFile.createNewFile()) {
                    try (InputStream inputStream = apkZipFile.getInputStream(apkEntry);
                         FileOutputStream fileOutputStream = new FileOutputStream(entryFile)) {
                        int rc;
                        byte[] bytes = new byte[32768];
                        while ((rc = inputStream.read(bytes)) > 0) {
                            fileOutputStream.write(bytes, 0, rc);
                        }
                    } catch (FileNotFoundException fileNotFoundException) {
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getBaseName(String fn) {
        int x = fn.lastIndexOf('.');
        return x >= 0 ? fn.substring(0, x) : fn;
    }
}
