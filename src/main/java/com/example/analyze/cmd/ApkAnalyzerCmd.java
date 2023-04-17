package com.example.analyze.cmd;

import com.example.d2j.BaseCmd;
import com.example.d2j.Opt;
import com.example.d2j.Syntax;

import java.nio.file.Path;

@Syntax(cmd = "ApkAnalyzer", syntax = "[options] <file0> [file1 ... fileN]", desc = "convert dex to jar")
public class ApkAnalyzerCmd extends BaseCmd {

    @Opt(opt = "apk", longOpt = "apk-file", description = "detail apk file, which provided apk file", argName = "file")
    private Path apkFile;

    @Opt(opt = "e", longOpt = "exception-file", description = "detail exception file, default is $current_dir/[file-name]-error.zip", argName = "file")
    private Path exceptionFile;

    public static void main(String[] args) {
        new ApkAnalyzerCmd().doMain(args);
    }

    @Override
    protected void doCommandLine() throws Exception {
//        Dex2jarCmd.main(inputJar, "--output", outputJar, "--force");
    }
}