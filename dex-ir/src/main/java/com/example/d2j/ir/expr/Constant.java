package com.example.d2j.ir.expr;

import com.example.d2j.ir.LabelAndLocalMapper;
import com.example.d2j.ir.Util;
import com.example.d2j.DexType;

import java.lang.reflect.Array;

/**
 * Represent a constant, number/string/type
 */
public class Constant extends Value.E0Expr {

    public static final Object Null = new Object();

    public Object value;

    public Constant(Object value) {
        super(VT.CONSTANT);
        this.value = value;
    }

    @Override
    public Value clone() {
        return new Constant(value);
    }

    @Override
    public Value clone(LabelAndLocalMapper mapper) {
        return new Constant(value);
    }

    @Override
    public String toString0() {
        if (Null == value) {
            return "null";
        } else if (value == null) {
            return "NULL";
        } else if (value instanceof Number) {
            if (value instanceof Float) {
                return value.toString() + "F";
            }
            if (value instanceof Long) {
                return value.toString() + "L";
            }
            return value.toString();
        } else if (value instanceof String) {
            StringBuffer buf = new StringBuffer();
            Util.appendString(buf, (String) value);
            return buf.toString();
        } else if (value instanceof DexType) {
            return Util.toShortClassName(((DexType) value).desc) + ".class";
        } else if (value.getClass().isArray()) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            int size = Array.getLength(value);
            for (int i = 0; i < size; i++) {
                sb.append(Array.get(value, i)).append(",");
            }
            if (size > 0) {
                sb.setLength(sb.length() - 1);
            }
            sb.append("]");
            return sb.toString();
        }
        return "" + value;
    }
}
