package com.example.apk;

import java.io.File;

public abstract class ApkVisitor {

    protected ApkVisitor apkVisitor;

    protected ApkVisitor(final ApkVisitor apkVisitor) {
        this.apkVisitor = apkVisitor;
    }

    /**
     * The apk visitor to which this visitor must delegate method calls. May be {@literal null}.
     *
     * @return the class visitor to which this visitor must delegate method calls, or {@literal null}.
     */
    public ApkVisitor getDelegate() {
        return apkVisitor;
    }

    public void visitAndroidManifest() {

    }

    public void visit() {

    }

    public void visitDex(File dexFile) {

    }

    public void visitResources() {

    }
}
