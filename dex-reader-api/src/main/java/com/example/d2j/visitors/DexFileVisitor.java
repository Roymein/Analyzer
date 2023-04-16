package com.example.d2j.visitors;


public class DexFileVisitor {
    protected DexFileVisitor visitor;

    public DexFileVisitor() {
        super();
    }

    public DexFileVisitor(DexFileVisitor visitor) {
        super();
        this.visitor = visitor;
    }

    public void visitDexFileVersion(int version) {
        if (visitor != null) {
            visitor.visitDexFileVersion(version);
        }
    }

    public DexClassVisitor visit(int access_flags, String className, String superClass, String[] interfaceNames) {
        if (visitor == null) {
            return null;
        }
        return visitor.visit(access_flags, className, superClass, interfaceNames);
    }

    public void visitEnd() {
        if (visitor == null) {
            return;
        }
        visitor.visitEnd();
    }

}
