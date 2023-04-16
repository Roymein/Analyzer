package com.example.d2j.writer;

import com.example.d2j.writer.item.AnnotationItem;
import com.example.d2j.writer.item.AnnotationSetItem;
import com.example.d2j.writer.item.ClassDataItem;
import com.example.d2j.writer.item.ConstPool;
import com.example.d2j.Visibility;
import com.example.d2j.visitors.DexAnnotationVisitor;
import com.example.d2j.visitors.DexFieldVisitor;

class FieldWriter extends DexFieldVisitor {
    final public ConstPool cp;
    private final ClassDataItem.EncodedField encodedField;

    public FieldWriter(ClassDataItem.EncodedField encodedField, ConstPool cp) {
        this.encodedField = encodedField;
        this.cp = cp;
    }

    @Override
    public DexAnnotationVisitor visitAnnotation(String name,
                                                Visibility visibility) {
        final AnnotationItem annItem = new AnnotationItem(cp.uniqType(name),
                visibility);
        AnnotationSetItem asi = encodedField.annotationSetItem;
        if (asi == null) {
            asi = new AnnotationSetItem();
            encodedField.annotationSetItem = asi;
        }
        asi.annotations.add(annItem);
        return new AnnotationWriter(annItem.annotation.elements, cp);
    }
}
