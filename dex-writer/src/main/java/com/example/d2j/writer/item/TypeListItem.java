package com.example.d2j.writer.item;

import com.example.d2j.writer.ann.Alignment;
import com.example.d2j.writer.ann.Off;
import com.example.d2j.writer.io.DataOut;

import java.util.List;

@Alignment(4)
public class TypeListItem extends BaseItem implements Comparable<TypeListItem> {
    public TypeListItem(List<TypeIdItem> items) {
        super();
        this.items = items;
    }

    @Off
    public final List<TypeIdItem> items;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((items == null) ? 0 : items.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TypeListItem other = (TypeListItem) obj;
        if (items == null) {
            if (other.items != null)
                return false;
        } else if (!items.equals(other.items))
            return false;
        return true;
    }

    @Override
    public int place(int offset) {
        return offset + 4 + items.size() * 2;
    }

    @Override
    public void write(DataOut out) {
        out.uint("size", items.size());
        for (TypeIdItem idItem : items) {
            out.ushort("type_idx", idItem.index);
        }
    }

    @Override
    public int compareTo(TypeListItem o) {
        int min = Math.min(items.size(), o.items.size());
        for (int i = 0; i < min; i++) {
            int x = items.get(i).compareTo(o.items.get(i));
            if (x != 0) {
                return x;
            }
        }
        return (items.size() == o.items.size() ? 0 : (items.size() < o.items.size() ? -1 : 1));
    }
}
