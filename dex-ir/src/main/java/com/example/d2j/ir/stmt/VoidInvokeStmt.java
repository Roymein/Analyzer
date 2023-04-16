package com.example.d2j.ir.stmt;

import com.example.d2j.ir.LabelAndLocalMapper;
import com.example.d2j.ir.expr.Value;

/**
 * Represent a void-return Invoke
 *
 * @see ST#VOID_INVOKE
 */
public class VoidInvokeStmt extends Stmt.E1Stmt {

    public VoidInvokeStmt(Value op) {
        super(ST.VOID_INVOKE, op);
    }

    @Override
    public Stmt clone(LabelAndLocalMapper mapper) {
        return new VoidInvokeStmt(op.clone(mapper));
    }

    @Override
    public String toString() {
        return "void " + op;
    }

}
