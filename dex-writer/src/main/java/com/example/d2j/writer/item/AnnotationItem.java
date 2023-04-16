package com.example.d2j.writer.item;

import com.example.d2j.writer.ev.EncodedAnnotation;
import com.example.d2j.writer.io.DataOut;
import com.example.d2j.Visibility;

public class AnnotationItem extends BaseItem {
    final public Visibility visibility;
    final public EncodedAnnotation annotation = new EncodedAnnotation();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnnotationItem that = (AnnotationItem) o;

        if (!annotation.equals(that.annotation)) return false;
        if (visibility != that.visibility) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = visibility.hashCode();
        result = 31 * result + annotation.hashCode();
        return result;
    }

    public AnnotationItem(TypeIdItem type, Visibility visibility) {
        this.visibility = visibility;
        annotation.type = type;
    }

    @Override
    public int place(int offset) {
        offset += 1;
        return annotation.place(offset);
    }

    @Override
    public void write(DataOut out) {
        out.ubyte("visibility", visibility.value);
        annotation.write(out);
    }
}
