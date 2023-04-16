package com.example.d2j.visitors;

import com.example.d2j.Visibility;

public class DexFieldVisitor implements DexAnnotationAble {
    protected DexFieldVisitor visitor;

    public DexFieldVisitor(DexFieldVisitor visitor) {
        super();
        this.visitor = visitor;
    }

    public DexFieldVisitor() {
    }

    public void visitEnd() {
        if (visitor == null) {
            return;
        }
        visitor.visitEnd();
    }

    public DexAnnotationVisitor visitAnnotation(String name, Visibility visibility) {
        if (visitor == null) {
            return null;
        }
        return visitor.visitAnnotation(name, visibility);
    }
}
