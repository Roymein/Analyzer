package com.example.d2j.ir.expr;

import com.example.d2j.ir.LabelAndLocalMapper;

/**
 * Represent a non-static Field expression.
 *
 * @see VT#FIELD
 */
public class FieldExpr extends Value.E1Expr {

    /**
     * Field name
     */
    public String name;
    /**
     * Field owner type descriptor
     */
    public String owner;
    /**
     * Field type descriptor
     */
    public String type;

    public FieldExpr(Value object, String ownerType, String fieldName, String fieldType) {
        super(VT.FIELD, object);
        this.type = fieldType;
        this.name = fieldName;
        this.owner = ownerType;
    }

    @Override
    protected void releaseMemory() {
        name = null;
        owner = type = null;
        super.releaseMemory();
    }

    @Override
    public Value clone() {
        return new FieldExpr(op.trim().clone(), owner, name, type);
    }

    @Override
    public Value clone(LabelAndLocalMapper mapper) {
        return new FieldExpr(op.clone(mapper), owner, name, type);
    }

    @Override
    public String toString0() {
        return op + "." + name;
    }

}
