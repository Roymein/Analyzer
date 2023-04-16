package com.example.d2j.ir.ts;

import com.example.d2j.ir.IrMethod;
import com.example.d2j.ir.LocalVar;
import com.example.d2j.ir.Trap;
import com.example.d2j.ir.stmt.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Clean unused {@link LabelStmt}
 */
public class CleanLabel implements Transformer {

    @Override
    public void transform(IrMethod irMethod) {
        Set<LabelStmt> uselabels = new HashSet<LabelStmt>();
        addTrap(irMethod.traps, uselabels);
        addVars(irMethod.vars, uselabels);
        addStmt(irMethod.stmts, uselabels);
        if (irMethod.phiLabels != null) {
            uselabels.addAll(irMethod.phiLabels);
        }
        rmUnused(irMethod.stmts, uselabels);
    }

    private void addVars(List<LocalVar> vars, Set<LabelStmt> uselabels) {
        if (vars != null) {
            for (LocalVar var : vars) {
                uselabels.add(var.start);
                uselabels.add(var.end);
            }
        }

    }

    private void rmUnused(StmtList stmts, Set<LabelStmt> uselabels) {
        for (Stmt p = stmts.getFirst(); p != null; ) {
            if (p.st == Stmt.ST.LABEL) {
                if (!uselabels.contains(p)) {
                    Stmt q = p.getNext();
                    stmts.remove(p);
                    p = q;
                    continue;
                }
            }
            p = p.getNext();
        }
    }

    private void addStmt(StmtList stmts, Set<LabelStmt> labels) {
        for (Stmt p = stmts.getFirst(); p != null; p = p.getNext()) {
            if (p instanceof JumpStmt) {
                labels.add(((JumpStmt) p).getTarget());
            } else if (p instanceof BaseSwitchStmt) {
                BaseSwitchStmt stmt = (BaseSwitchStmt) p;
                labels.add(stmt.defaultTarget);
                for (LabelStmt t : stmt.targets) {
                    labels.add(t);
                }
            }
        }
    }

    private void addTrap(List<Trap> traps, Set<LabelStmt> labels) {
        if (traps != null) {
            for (Trap trap : traps) {
                labels.add(trap.start);
                labels.add(trap.end);
                for (LabelStmt h : trap.handlers) {
                    labels.add(h);
                }
            }
        }
    }

}
