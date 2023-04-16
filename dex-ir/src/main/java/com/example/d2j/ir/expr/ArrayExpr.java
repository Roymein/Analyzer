package com.example.d2j.ir.expr;

import com.example.d2j.ir.LabelAndLocalMapper;

/**
 * Represent an Array expression
 * @see VT#ARRAY
 */
public class ArrayExpr extends Value.E2Expr {

    public ArrayExpr() {
        super(VT.ARRAY, null, null);
    }

    public String elementType;

    public ArrayExpr(Value base, Value index, String elementType) {
        super(VT.ARRAY, base, index);
        this.elementType = elementType;
    }

    @Override
    public Value clone() {
        return new ArrayExpr(op1.trim().clone(), op2.trim().clone(), this.elementType);
    }

    @Override
    public Value clone(LabelAndLocalMapper mapper) {
        return new ArrayExpr(op1.clone(mapper), op2.clone(mapper), this.elementType);
    }

    @Override
    public String toString0() {
        return op1 + "[" + op2 + "]";
    }
}
