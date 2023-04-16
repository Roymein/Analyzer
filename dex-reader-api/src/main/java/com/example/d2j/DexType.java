package com.example.d2j;

/**
 * a lightweight version of org.objectweb.asm.Type
 */
public class DexType {
    public DexType(String desc) {
        this.desc = desc;
    }

    /**
     * type descriptor, in TypeDescriptor format
     */
    final public String desc;

    @Override
    public String toString() {
        return desc;
    }
}