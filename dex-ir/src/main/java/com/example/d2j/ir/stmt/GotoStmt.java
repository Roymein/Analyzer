package com.example.d2j.ir.stmt;

import com.example.d2j.ir.LabelAndLocalMapper;

/**
 * Represent a GOTO statement
 *
 * @see ST#GOTO
 */
public class GotoStmt extends Stmt.E0Stmt implements JumpStmt {
    public LabelStmt target;

    public LabelStmt getTarget() {
        return target;
    }

    public void setTarget(LabelStmt target) {
        this.target = target;
    }

    public GotoStmt(LabelStmt target) {
        super(ST.GOTO);
        this.target = target;
    }

    @Override
    public Stmt clone(LabelAndLocalMapper mapper) {
        LabelStmt nTarget = mapper.map(target);
        return new GotoStmt(nTarget);
    }

    @Override
    public String toString() {
        return "GOTO " + target.getDisplayName();
    }
}
