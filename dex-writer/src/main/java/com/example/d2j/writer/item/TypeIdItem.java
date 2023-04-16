package com.example.d2j.writer.item;

import com.example.d2j.writer.ann.Alignment;
import com.example.d2j.writer.ann.Idx;
import com.example.d2j.writer.io.DataOut;

@Alignment(4)
public class TypeIdItem extends BaseItem implements Comparable<TypeIdItem> {
    public TypeIdItem(StringIdItem stringIdItem) {
        super();
        this.descriptor = stringIdItem;
    }

    @Idx
    public final StringIdItem descriptor;

    @Override
    public int place(int offset) {
        return offset + 0x04;
    }

    @Override
    public String toString() {
        return "TypeIdItem [descriptor=" + descriptor + "]";
    }

    @Override
    public void write(DataOut out) {
        out.uint("descriptor_idx", this.descriptor.index);
    }

    @Override
    public int compareTo(TypeIdItem o) {
        return descriptor.compareTo(o.descriptor);
    }
}
