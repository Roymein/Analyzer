package com.example.apk;

import com.example.apk.visitor.ClassScannerVisitor;
import com.example.d2j.BaseCmd;
import com.example.d2j.Opt;
import com.example.d2j.Syntax;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

@Syntax(cmd = "ApkDexAnalyzer", syntax = "[options] <file0> [file1 ... fileN]", desc = "convert dex to jar")
public class ApkDexAnalyzer extends BaseCmd {
    private static final String DEFAULT_OUT_DIR_NAME = "out";

    @Opt(opt = "f", longOpt = "apk-file", required = true, description = "detail apk file, which provided apk file", argName = "file")
    private Path apkFile;

    @Opt(opt = "o", longOpt = "out", description = "root out dir", argName = "file")
    private Path out;

    public static void main(String[] args) {
        new ApkDexAnalyzer().doMain(args);
    }

    @Override
    protected void doCommandLine() throws Exception {
        File toolJar = toolJar();
        File outputDir = outputDir(toolJar);
        byte[] bytes = Files.readAllBytes(apkFile);
        ApkReader apkReader = new ApkReader(apkFile.toFile().getName(), bytes, outputDir);
        apkReader.accept(new ClassScannerVisitor());
    }

    private File toolJar() {
        URI location;
        try {
            location = ApkDexAnalyzer.class.getProtectionDomain().getCodeSource().getLocation().toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        if (!location.getScheme().equals("file")) {
            throw new RuntimeException(String.format("Cannot determine classpath for wrapper Jar from codebase '%s'.", location));
        }
        try {
            return Path.of(location).toFile();
        } catch (NoClassDefFoundError e) {
            return new File(location.getPath());
        }
    }

    private File outputDir(File toolJar) {
        if (out == null) {
            return new File(toolJar.getParent(), DEFAULT_OUT_DIR_NAME);
        }
        return out.toFile();
    }
}