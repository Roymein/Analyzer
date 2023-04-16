package com.example.d2j.visitors;

import com.example.d2j.Visibility;

/**
 * for access annotation
 */
public interface DexAnnotationAble {

    /**
     * access annotation
     *
     * @param name       annotation name
     * @param visibility is annotation runtime visibility
     * @return
     */
    DexAnnotationVisitor visitAnnotation(String name, Visibility visibility);
}
