package com.example.d2j.writer.item;

import com.example.d2j.writer.ann.Idx;
import com.example.d2j.writer.ann.Off;
import com.example.d2j.writer.ev.EncodedValue;
import com.example.d2j.writer.io.DataOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ClassDataItem extends BaseItem {
    public final List<EncodedField> staticFields = new ArrayList<>(5);
    public final List<EncodedField> instanceFields = new ArrayList<>(5);
    public final List<EncodedMethod> directMethods = new ArrayList<>(5);
    public final List<EncodedMethod> virtualMethods = new ArrayList<>(5);

    @Override
    public int place(int offset) {
        offset += lengthOfUleb128(staticFields.size());
        offset += lengthOfUleb128(instanceFields.size());
        offset += lengthOfUleb128(directMethods.size());
        offset += lengthOfUleb128(virtualMethods.size());
        offset = placeField(offset, staticFields);
        offset = placeField(offset, instanceFields);
        offset = placeMethod(offset, directMethods);
        offset = placeMethod(offset, virtualMethods);
        return offset;
    }

    private int placeMethod(int offset, List<EncodedMethod> methods) {
        if (methods.size() == 0) {
            return offset;
        }
        int lastIdx = 0;
        for (EncodedMethod f : methods) {
            offset += lengthOfUleb128(f.method.index - lastIdx);
            offset += lengthOfUleb128(f.accessFlags);
            offset += lengthOfUleb128(f.code == null ? 0 : f.code.offset);
            lastIdx = f.method.index;
        }
        return offset;
    }

    private int placeField(int offset, List<EncodedField> fields) {
        if (fields.size() == 0) {
            return offset;
        }
        int lastIdx = 0;
        for (EncodedField f : fields) {
            offset += lengthOfUleb128(f.field.index - lastIdx);
            offset += lengthOfUleb128(f.accessFlags);

            lastIdx = f.field.index;
        }
        return offset;
    }

    @Override
    public void write(DataOut out) {
        out.uleb128("static_fields_size", staticFields.size());
        out.uleb128("instance_fields_size", instanceFields.size());
        out.uleb128("ditect_methods_size", directMethods.size());
        out.uleb128("virtual_methods_size", virtualMethods.size());
        writeField(out, staticFields);
        writeField(out, instanceFields);
        writeMethod(out, directMethods);
        writeMethod(out, virtualMethods);
    }

    private void writeMethod(DataOut out, List<EncodedMethod> methods) {
        if (methods == null || methods.size() == 0) {
            return;
        }
        int lastIdx = 0;
        for (EncodedMethod f : methods) {
            out.uleb128("method_idx_diff", f.method.index - lastIdx);
            out.uleb128("access_flags", f.accessFlags);
            out.uleb128("code_off", f.code == null ? 0 : f.code.offset);
            lastIdx = f.method.index;
        }
    }

    private void writeField(DataOut out, List<EncodedField> fields) {
        if (fields == null || fields.size() == 0) {
            return;
        }
        int lastIdx = 0;
        for (EncodedField f : fields) {
            out.uleb128("field_idx_diff", f.field.index - lastIdx);
            out.uleb128("access_flags", f.accessFlags);
            lastIdx = f.field.index;
        }
    }

    public int getMemberSize() {
        return instanceFields.size() + staticFields.size() + directMethods.size() + virtualMethods.size();
    }

    public void prepare(ConstPool cp) {
        Comparator<EncodedField> fc = new Comparator<EncodedField>() {

            @Override
            public int compare(EncodedField arg0, EncodedField arg1) {
                return arg0.field.compareTo(arg1.field);
            }
        };
        Comparator<EncodedMethod> mc = new

                Comparator<EncodedMethod>() {

                    @Override
                    public int compare(EncodedMethod arg0, EncodedMethod arg1) {
                        return arg0.method.compareTo(arg1.method);
                    }
                };
        Collections.sort(instanceFields, fc);
        Collections.sort(staticFields, fc);
        Collections.sort(directMethods, mc);
        Collections.sort(virtualMethods, mc);

    }

    public static class EncodedField {
        public int accessFlags;
        @Idx
        public FieldIdItem field;
        public EncodedValue staticValue;
        public AnnotationSetItem annotationSetItem;
    }

    public static class EncodedMethod {
        public int accessFlags;
        @Idx
        public MethodIdItem method;
        @Off
        public CodeItem code;
        //
        public AnnotationSetItem annotationSetItem;
        public AnnotationSetRefListItem parameterAnnotation;
    }
}
