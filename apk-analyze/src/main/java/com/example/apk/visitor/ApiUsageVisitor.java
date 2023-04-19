package com.example.apk.visitor;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ApiUsageVisitor extends MethodVisitor {
    private final String ownerClassName;
    private final String ownerMethodName;

    public ApiUsageVisitor(String ownerClassName, String ownerMethodName) {
        super(Opcodes.ASM5);
        this.ownerClassName = ownerClassName;
        this.ownerMethodName = ownerMethodName;
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        super.visitMethodInsn(opcode, owner, name, desc, itf);
        String methodDesc = "L" + owner + ";->" + name + desc;
        String info = "ownerClass: " + ownerClassName + " ownerMethod " + ownerMethodName + " call: " + methodDesc;
        System.out.println(info);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        super.visitFieldInsn(opcode, owner, name, desc);
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        super.visitTryCatchBlock(start, end, handler, type);
    }

}
