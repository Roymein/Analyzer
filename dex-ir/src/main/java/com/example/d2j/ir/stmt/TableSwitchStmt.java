package com.example.d2j.ir.stmt;

import com.example.d2j.ir.LabelAndLocalMapper;
import com.example.d2j.ir.expr.Value;

/**
 * Represent a TABLE_SWITCH statement
 *
 * @see ST#TABLE_SWITCH
 */
public class TableSwitchStmt extends BaseSwitchStmt {

    public int lowIndex;

    public TableSwitchStmt() {
        super(ST.TABLE_SWITCH, null);
    }

    public TableSwitchStmt(Value key, int lowIndex, LabelStmt[] targets, LabelStmt defaultTarget) {
        super(ST.TABLE_SWITCH, key);
        this.lowIndex = lowIndex;
        this.targets = targets;
        this.defaultTarget = defaultTarget;
    }

    @Override
    public Stmt clone(LabelAndLocalMapper mapper) {
        LabelStmt[] nTargets = new LabelStmt[targets.length];
        for (int i = 0; i < nTargets.length; i++) {
            nTargets[i] = mapper.map(targets[i]);
        }
        return new TableSwitchStmt(op.clone(mapper), lowIndex, nTargets, mapper.map(defaultTarget));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("switch(").append(op).append(") {");

        for (int i = 0; i < targets.length; i++) {
            sb.append("\n case ").append(lowIndex + i).append(": GOTO ").append(targets[i].getDisplayName())
                    .append(";");
        }
        sb.append("\n default : GOTO ").append(defaultTarget.getDisplayName()).append(";");
        sb.append("\n}");
        return sb.toString();
    }

}
