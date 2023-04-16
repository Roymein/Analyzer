package com.example.d2j.ir.stmt;

public interface JumpStmt {

    LabelStmt getTarget();

    void setTarget(LabelStmt labelStmt);
}
