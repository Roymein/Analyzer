package com.example.d2j.ir.stmt;

import com.example.d2j.ir.expr.Value;

/**
 * Parent class of {@link LookupSwitchStmt} and {@link TableSwitchStmt}
 *
 * @author <a href="mailto:pxb1988@gmail.com">Panxiaobo</a>
 */
public abstract class BaseSwitchStmt extends Stmt.E1Stmt {
    public BaseSwitchStmt(ST type, Value op) {
        super(type, op);
    }

    public LabelStmt[] targets;
    public LabelStmt defaultTarget;
}
