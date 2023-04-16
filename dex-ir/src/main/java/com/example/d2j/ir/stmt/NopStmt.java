package com.example.d2j.ir.stmt;

import com.example.d2j.ir.LabelAndLocalMapper;

/**
 * Represent a NOP statement
 *
 * @see ST#NOP
 */
public class NopStmt extends Stmt.E0Stmt {

    public NopStmt() {
        super(ST.NOP);
    }

    @Override
    public Stmt clone(LabelAndLocalMapper mapper) {
        return new NopStmt();
    }

    @Override
    public String toString() {
        return "NOP";
    }
}
