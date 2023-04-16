package com.example.d2j.writer.item;

import com.example.d2j.writer.io.DataOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AnnotationSetItem extends BaseItem {
    public List<AnnotationItem> annotations = new ArrayList<>(3);
    private static final Comparator<AnnotationItem> cmp = new Comparator<AnnotationItem>() {
        @Override
        public int compare(AnnotationItem o1, AnnotationItem o2) {
            return o1.annotation.type.compareTo(o2.annotation.type);
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnnotationSetItem that = (AnnotationSetItem) o;

        if (!annotations.equals(that.annotations)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return annotations.hashCode();
    }

    @Override
    public int place(int offset) {
        return offset + 4 + annotations.size() * 4;
    }

    @Override
    public void write(DataOut out) {
        Collections.sort(annotations, cmp);
        out.uint("size", annotations.size());
        for (AnnotationItem item : annotations) {
            out.uint("annotation_off", item.offset);
        }
    }
}
