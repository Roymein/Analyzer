package com.example.d2j.ir.stmt;

import com.example.d2j.ir.LabelAndLocalMapper;
import com.example.d2j.ir.expr.Value;

public class UnopStmt extends Stmt.E1Stmt {

    public UnopStmt(ST type, Value op) {
        super(type, op);
    }

    @Override
    public Stmt clone(LabelAndLocalMapper mapper) {
        return new UnopStmt(st, op.clone(mapper));
    }

    @Override
    public String toString() {
        switch (super.st) {
            case LOCK:
                return "lock " + op;
            case UNLOCK:
                return "unlock " + op;
            case THROW:
                return "throw " + op;
            case RETURN:
                return "return " + op;
            case LOCAL_END:
                return op + " ::END";
            default:
        }
        return super.toString();
    }
}
