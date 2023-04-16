package com.example.d2j.writer.item;

import com.example.d2j.writer.io.DataOut;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MapListItem extends BaseItem {
    final public List<SectionItem<?>> items = new ArrayList<>();

    public int getSize() {
        return 4 + items.size() * 12;
    }

    public void writeMapItem(DataOut out, int type, int size, int offset) {
        out.begin("map_item");
        out.ushort("type", type);
        out.ushort("unused", 0);
        out.uint("size", size);
        out.uint("offset", offset);
        out.end();
    }

    public void cleanZeroSizeEntry() {
        for (Iterator<SectionItem<?>> it = items.iterator(); it.hasNext(); ) {
            SectionItem<?> i = it.next();
            if (i == null || i.items.size() < 1) {
                it.remove();
            }
        }
    }

    public void write(DataOut out) {
        out.begin("map_list");
        out.uint("size", items.size());
        for (SectionItem<?> t : items) {
            writeMapItem(out, t.sectionType.code, t.items.size(), t.offset);
        }
        out.end();
        items.clear();
    }

    @Override
    public int place(int offset) {
        return offset + 4 + items.size() * 12;
    }
}
