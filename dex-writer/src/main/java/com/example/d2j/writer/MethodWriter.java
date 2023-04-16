package com.example.d2j.writer;

import com.example.d2j.writer.item.*;
import com.example.d2j.Method;
import com.example.d2j.Visibility;
import com.example.d2j.visitors.DexAnnotationAble;
import com.example.d2j.visitors.DexAnnotationVisitor;
import com.example.d2j.visitors.DexCodeVisitor;
import com.example.d2j.visitors.DexMethodVisitor;

class MethodWriter extends DexMethodVisitor {
    final public ConstPool cp;
    private final ClassDataItem.EncodedMethod encodedMethod;
    final boolean isStatic;
    final Method method;
    private final int parameterSize;

    public MethodWriter(ClassDataItem.EncodedMethod encodedMethod, Method m,
                        boolean isStatic, ConstPool cp) {
        this.encodedMethod = encodedMethod;
        this.parameterSize = m.getParameterTypes().length;
        this.cp = cp;
        this.method = m;
        this.isStatic = isStatic;
    }

    @Override
    public DexAnnotationVisitor visitAnnotation(String name,
                                                Visibility visibility) {
        final AnnotationItem annItem = new AnnotationItem(cp.uniqType(name),
                visibility);
        AnnotationSetItem asi = encodedMethod.annotationSetItem;
        if (asi == null) {
            asi = new AnnotationSetItem();
            encodedMethod.annotationSetItem = asi;
        }
        asi.annotations.add(annItem);
        return new AnnotationWriter(annItem.annotation.elements, cp);
    }

    @Override
    public DexCodeVisitor visitCode() {
        encodedMethod.code = new CodeItem();
        return new CodeWriter(encodedMethod, encodedMethod.code, method, isStatic, cp);
    }

    @Override
    public DexAnnotationAble visitParameterAnnotation(final int index) {
        return new DexAnnotationAble() {
            @Override
            public DexAnnotationVisitor visitAnnotation(String name,
                                                        Visibility visibility) {
                AnnotationSetRefListItem asrl = encodedMethod.parameterAnnotation;
                if (asrl == null) {
                    asrl = new AnnotationSetRefListItem(parameterSize);
                    encodedMethod.parameterAnnotation = asrl;
                }
                AnnotationSetItem asi = asrl.annotationSets[index];
                if (asi == null) {
                    asi = new AnnotationSetItem();
                    asrl.annotationSets[index] = asi;
                }
                final AnnotationItem annItem = new AnnotationItem(
                        cp.uniqType(name), visibility);
                asi.annotations.add(annItem);
                return new AnnotationWriter(annItem.annotation.elements, cp);
            }
        };
    }
}
