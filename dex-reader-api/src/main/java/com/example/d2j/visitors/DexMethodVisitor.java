package com.example.d2j.visitors;


import com.example.d2j.Visibility;

public class DexMethodVisitor implements DexAnnotationAble {
    protected DexMethodVisitor visitor;

    public DexMethodVisitor() {
        super();
    }

    /**
     * @param mv
     */
    public DexMethodVisitor(DexMethodVisitor mv) {
        super();
        this.visitor = mv;
    }

    public DexAnnotationVisitor visitAnnotation(String name, Visibility visibility) {
        if (visitor == null) {
            return null;
        }
        return visitor.visitAnnotation(name, visibility);
    }

    public DexCodeVisitor visitCode() {
        if (visitor == null) {
            return null;
        }
        return visitor.visitCode();
    }

    public void visitEnd() {
        if (visitor == null) {
            return;
        }
        visitor.visitEnd();
    }

    public DexAnnotationAble visitParameterAnnotation(int index) {
        if (visitor == null) {
            return null;
        }
        return visitor.visitParameterAnnotation(index);
    }

}
