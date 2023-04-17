package com.example.analyze.cmd;

import com.example.d2j.BaseCmd;
import com.example.d2j.Syntax;

@Syntax(cmd = "ManifestParse", syntax = "[options] <file0> [file1 ... fileN]", desc = "convert dex to jar")
public class ManifestParseCmd extends BaseCmd {

    public static void main(String[] args) {
        new ManifestParseCmd().doMain(args);
    }

    @Override
    protected void doCommandLine() throws Exception {

    }
}
