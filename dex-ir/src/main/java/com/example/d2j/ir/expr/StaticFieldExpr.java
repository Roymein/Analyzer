package com.example.d2j.ir.expr;

import com.example.d2j.ir.LabelAndLocalMapper;
import com.example.d2j.ir.Util;

/**
 * Represent a StaticField expression
 *
 * @see VT#STATIC_FIELD
 */
public class StaticFieldExpr extends Value.E0Expr {

    /**
     * Field name
     */
    public String name;
    /**
     * Field owner type
     */
    public String owner;
    /**
     * Field type
     */
    public String type;

    @Override
    protected void releaseMemory() {
        name = null;
        owner = type = null;
        super.releaseMemory();
    }

    public StaticFieldExpr(String ownerType, String fieldName, String fieldType) {
        super(VT.STATIC_FIELD);
        this.type = fieldType;
        this.name = fieldName;
        this.owner = ownerType;
    }

    @Override
    public Value clone() {
        return new StaticFieldExpr(owner, name, type);
    }

    @Override
    public Value clone(LabelAndLocalMapper mapper) {
        return new StaticFieldExpr(owner, name, type);
    }

    @Override
    public String toString0() {
        return Util.toShortClassName(owner) + "." + name;
    }

}
