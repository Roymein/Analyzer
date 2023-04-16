package com.example.d2j.node;

import com.example.d2j.DexConstants;
import com.example.d2j.visitors.DexClassVisitor;
import com.example.d2j.visitors.DexFileVisitor;

import java.util.ArrayList;
import java.util.List;

public class DexFileNode extends DexFileVisitor {
    public List<DexClassNode> clzs = new ArrayList<>();
    public int dexVersion = DexConstants.DEX_035;

    @Override
    public void visitDexFileVersion(int version) {
        this.dexVersion = version;
        super.visitDexFileVersion(version);
    }

    @Override
    public DexClassVisitor visit(int access_flags, String className, String superClass, String[] interfaceNames) {
        DexClassNode cn = new DexClassNode(access_flags, className, superClass, interfaceNames);
        clzs.add(cn);
        return cn;
    }

    public void accept(DexClassVisitor dcv) {
        for (DexClassNode cn : clzs) {
            cn.accept(dcv);
        }
    }

    public void accept(DexFileVisitor dfv) {
        for (DexClassNode cn : clzs) {
            cn.accept(dfv);
        }
    }
}
