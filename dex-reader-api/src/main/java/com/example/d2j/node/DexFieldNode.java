package com.example.d2j.node;

import com.example.d2j.Field;
import com.example.d2j.Visibility;
import com.example.d2j.visitors.DexAnnotationVisitor;
import com.example.d2j.visitors.DexClassVisitor;
import com.example.d2j.visitors.DexFieldVisitor;

import java.util.ArrayList;
import java.util.List;

public class DexFieldNode extends DexFieldVisitor {
    public int access;
    public List<DexAnnotationNode> anns;
    public Object cst;
    public Field field;

    public DexFieldNode(DexFieldVisitor visitor, int access, Field field, Object cst) {
        super(visitor);
        this.access = access;
        this.field = field;
        this.cst = cst;
    }

    public DexFieldNode(int access, Field field, Object cst) {
        super();
        this.access = access;
        this.field = field;
        this.cst = cst;
    }

    public void accept(DexClassVisitor dcv) {
        DexFieldVisitor fv = dcv.visitField(access, field, cst);
        if (fv != null) {
            accept(fv);
            fv.visitEnd();
        }
    }

    public void accept(DexFieldVisitor fv) {
        if (anns != null) {
            for (DexAnnotationNode ann : anns) {
                ann.accept(fv);
            }
        }
    }

    @Override
    public DexAnnotationVisitor visitAnnotation(String name, Visibility visibility) {
        if (anns == null) {
            anns = new ArrayList<DexAnnotationNode>(5);
        }
        DexAnnotationNode annotation = new DexAnnotationNode(name, visibility);
        anns.add(annotation);
        return annotation;
    }
}
