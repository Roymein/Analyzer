package com.example.d2j.dex;

import com.example.d2j.DexException;
import com.example.d2j.node.DexMethodNode;
import org.objectweb.asm.AsmBridge;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

public class ExDex2Asm extends Dex2Asm {
    final protected DexExceptionHandler exceptionHandler;

    public ExDex2Asm(DexExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void convertCode(DexMethodNode methodNode, MethodVisitor mv) {
        MethodVisitor mw = AsmBridge.searchMethodWriter(mv);
        MethodNode mn = new MethodNode(Opcodes.ASM5, methodNode.access, methodNode.method.getName(),
                methodNode.method.getDesc(), null, null);
        try {
            super.convertCode(methodNode, mn);
        } catch (Exception ex) {
            if (exceptionHandler == null) {
                throw new DexException(ex, "fail convert code for %s", methodNode.method);
            } else {
                mn.instructions.clear();
                mn.tryCatchBlocks.clear();
                exceptionHandler.handleMethodTranslateException(methodNode.method, methodNode, mn, ex);
            }
        }
        // code convert ok, copy to MethodWriter and check for Size
        mn.accept(mv);
        if (mw != null) {
            try {
                AsmBridge.sizeOfMethodWriter(mw);
            } catch (Exception ex) {
                mn.instructions.clear();
                mn.tryCatchBlocks.clear();
                exceptionHandler.handleMethodTranslateException(methodNode.method, methodNode, mn, ex);
                AsmBridge.replaceMethodWriter(mw, mn);
            }
        }
    }
}
