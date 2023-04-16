package com.example.d2j.writer.ev;

import com.example.d2j.writer.io.DataOut;
import com.example.d2j.writer.item.BaseItem;

import java.util.ArrayList;
import java.util.List;

public class EncodedArray {

    public List<EncodedValue> values = new ArrayList<>(5);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EncodedArray that = (EncodedArray) o;

        if (!values.equals(that.values)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }

    public int place(int offset) {
        offset += BaseItem.lengthOfUleb128(values.size());
        for (EncodedValue ev : values) {
            offset = ev.place(offset);
        }
        return offset;
    }

    public void write(DataOut out) {
        out.uleb128("size", values.size());
        for (EncodedValue ev : values) {
            ev.write(out);
        }
    }
}
