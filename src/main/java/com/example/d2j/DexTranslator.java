package com.example.d2j;

import java.io.File;
import java.util.Objects;

@Syntax(cmd = "d2j-DexTranslator", syntax = "[options] <file0> [file1 ... fileN]", desc = "convert dex to jar")
public class DexTranslator extends BaseCmd {

    private final String[] mArgs;

    public DexTranslator(String[] args) {
        mArgs = args;
    }

    public void translate() {
        try {
            Arg arg = parseCmdArgs(mArgs);
            String inputJar = arg.inputJar;
            String outputJar = arg.getOutputJar();
            Dex2jarCmd.main(inputJar, "--output", outputJar, "--force");

            File apiFile = arg.getApiFile();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private Arg parseCmdArgs(String[] args) {
        String inputJar = null;
        File apiFile = null;
        for (int index = 0; index < args.length; index++) {
            String arg = args[index];
            if (Objects.equals(arg, "--input")) {
                inputJar = args[++index];
            } else if (Objects.equals(arg, "--api")) {
                apiFile = new File(args[++index]);
            }
        }
        return new Arg(inputJar, apiFile);
    }

    private static class Arg {
        private final String inputJar;
        private final File apiFile;

        public Arg(String inputJar, File apiFile) {
            this.inputJar = inputJar;
            this.apiFile = apiFile;
        }

        public String getOutputJar() {
            return inputJar.substring(0, inputJar.indexOf(".")) + "-dex.jar";
        }

        public File getApiFile() {
            return apiFile;
        }

        @Override
        public String toString() {
            return "Arg{" +
                    "inputJar='" + inputJar + '\'' +
                    '}';
        }
    }

    public static void main(String[] args) {
        DexTranslator dexTranslator = new DexTranslator(args);
        dexTranslator.translate();
    }

    @Override
    protected void doCommandLine() throws Exception {

    }
}