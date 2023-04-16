package com.example.d2j.ir.stmt;

import com.example.d2j.ir.LabelAndLocalMapper;

/**
 * Represent a RETURN_VOID statement
 * @see ST#RETURN_VOID
 */
public class ReturnVoidStmt extends Stmt.E0Stmt {

    public ReturnVoidStmt() {
        super(ST.RETURN_VOID);
    }

    @Override
    public Stmt clone(LabelAndLocalMapper mapper) {
        return new ReturnVoidStmt();
    }

    @Override
    public String toString() {
        return "return";
    }
}
