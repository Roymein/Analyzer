package com.example.apk.visitor;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ClassAnalyzerVisitor extends ClassVisitor {
    private String className;

    public ClassAnalyzerVisitor() {
        super(Opcodes.ASM5);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        className = name;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        return new ApiUsageVisitor(className, getName(name, descriptor, signature));
    }

    private String getName(String name, String descriptor, String signature) {
        if (signature == null) {
            return name + descriptor;
        } else {
            return name + signature;
        }
    }
}
