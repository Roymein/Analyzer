package com.example.analyze;

import com.example.analyze.cmd.ApkAnalyzerCmd;
import com.example.analyze.cmd.ManifestParseCmd;
import com.example.d2j.BaseCmd;
import com.example.d2j.Dex2jarCmd;
import com.example.d2j.Syntax;

import java.lang.reflect.Method;
import java.util.*;

public class CmdHandler {
    private static final String METHOD_NAME = "main";
    private final List<Class<? extends BaseCmd>> commands = new ArrayList<>(Arrays.asList(
            Dex2jarCmd.class,
            ApkAnalyzerCmd.class,
            ManifestParseCmd.class
    ));

    private final Map<String, Class<? extends BaseCmd>> commandMap = new HashMap<>();

    public void doMain(String... args) {
        initCmd();
        parseRunCommand(args);
    }

    private void initCmd() {
        for (Class<? extends BaseCmd> cmdClass : commands) {
            Syntax syntax = cmdClass.getAnnotation(Syntax.class);
            if (syntax == null) {
                throw new RuntimeException();
            }
            String cmdName = syntax.cmd();
            checkConflict(cmdName, cmdClass);
        }
    }

    private void checkConflict(String key, Class<? extends BaseCmd> command) {
        if (commandMap.containsKey(key)) {
            throw new RuntimeException(key + " conflict");
        }
        commandMap.put(key, command);
    }

    private void parseRunCommand(String[] args) {
        if (args.length <= 1) {
            throw new RuntimeException("args should more than one");
        }
        String cmdName = args[0];
        Class<? extends BaseCmd> command = commandMap.get(cmdName);
        if (command == null) {
            throw new RuntimeException("command should not null");
        }
        int argsLength = args.length - 1;
        final String[] destArgs = new String[argsLength];
        System.arraycopy(args, 1, destArgs, 0, argsLength);
        try {
            Method method = command.getMethod(METHOD_NAME, String[].class);
            method.invoke(null, (Object) destArgs);
        } catch (ReflectiveOperationException exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new CmdHandler().doMain(args);
    }
}
