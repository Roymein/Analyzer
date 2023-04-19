package com.example.d2j;

/**
 * a lightweight version of org.objectweb.asm.Label
 */
public class DexLabel {
    public String displayName;
    private int offset = -1;

    /**
     * @param offset the offset of the label
     */
    public DexLabel(int offset) {
        super();
        this.offset = offset;
    }

    public DexLabel() {
        super();
    }

    @Override
    public String toString() {
        if (displayName != null) {
            return displayName;
        }
        if (offset >= 0) {
            return String.format("L%04x", offset);
        }
        return String.format("L%08x", this.hashCode());
    }
}
