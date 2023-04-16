package com.example.d2j.writer.item;

import com.example.d2j.writer.ev.EncodedArray;
import com.example.d2j.writer.io.DataOut;

public class EncodedArrayItem extends BaseItem {
    public EncodedArray value = new EncodedArray();

    @Override
    public int place(int offset) {
        return value.place(offset);
    }

    @Override
    public void write(DataOut out) {
        value.write(out);
    }
}
