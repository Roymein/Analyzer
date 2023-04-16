package com.example.d2j.ir.expr;

import com.example.d2j.ir.LabelAndLocalMapper;
import com.example.d2j.ir.Util;

/**
 * Represent a Type expression
 *
 * @see VT#CHECK_CAST
 * @see VT#INSTANCE_OF
 * @see VT#NEW_ARRAY
 */
public class TypeExpr extends Value.E1Expr {

    public String type;

    @Override
    protected void releaseMemory() {
        type = null;
        super.releaseMemory();
    }

    public TypeExpr(VT vt, Value value, String desc) {
        super(vt, value);
        this.type = desc;

    }

    @Override
    public Value clone() {
        return new TypeExpr(vt, op.trim().clone(), type);
    }

    @Override
    public Value clone(LabelAndLocalMapper mapper) {
        return new TypeExpr(vt, op.clone(mapper), type);
    }


    @Override
    public String toString0() {
        switch (super.vt) {
            case CHECK_CAST:
                return "((" + Util.toShortClassName(type) + ")" + op + ")";
            case INSTANCE_OF:
                return "(" + op + " instanceof " + Util.toShortClassName(type) + ")";
            case NEW_ARRAY:
                if (type.charAt(0) == '[') {
                    int dimension = 1;
                    while (type.charAt(dimension) == '[') {
                        dimension++;
                    }
                    StringBuilder sb = new StringBuilder("new ")
                            .append(Util.toShortClassName(type.substring(dimension))).append("[").append(op)
                            .append("]");
                    for (int i = 0; i < dimension; i++) {
                        sb.append("[]");
                    }
                    return sb.toString();
                }
                return "new " + Util.toShortClassName(type) + "[" + op + "]";
            default:
        }
        return "UNKNOW";
    }
}
