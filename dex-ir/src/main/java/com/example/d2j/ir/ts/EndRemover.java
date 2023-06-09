package com.example.d2j.ir.ts;

import com.example.d2j.ir.IrMethod;
import com.example.d2j.ir.LabelAndLocalMapper;
import com.example.d2j.ir.Trap;
import com.example.d2j.ir.expr.Local;
import com.example.d2j.ir.stmt.*;

import java.util.ArrayList;

/**
 * Try to clean following between a {@link Trap}
 * <ol>
 * <li>Move {@link Stmt}s outside a {@link Trap} if {@link Stmt}s are not throw</li>
 * <li>Remove {@link Trap} if all {@link Stmt}s are not throw</li>
 * <li>...;GOTO L2; ... ; L2: ; return; => ...;return ; ... ; L2: ; return;</li>
 * </ol>
 */
public class EndRemover implements Transformer {

    private static final LabelAndLocalMapper keepLocal = new LabelAndLocalMapper() {
        @Override
        public Local map(Local local) {
            return local;
        }
    };

    @Override
    public void transform(IrMethod irMethod) {
        for (Trap trap : new ArrayList<Trap>(irMethod.traps)) {// copy the list and we can remove one from original list
            LabelStmt start = null;
            boolean removeTrap = true;
            for (Stmt p = trap.start.getNext(); p != null && p != trap.end; ) {
                boolean notThrow = Cfg.notThrow(p);
                if (!notThrow) {
                    start = null;
                    p = p.getNext();
                    removeTrap = false;
                    continue;
                }
                switch (p.st) {
                    case LABEL:
                        if (start != null) {
                            move4Label(irMethod.stmts, start, p.getPre(), (LabelStmt) p);
                        }
                        start = (LabelStmt) p;
                        p = p.getNext();

                        break;
                    case GOTO:
                    case RETURN:
                    case RETURN_VOID:
                        if (start != null) {
                            Stmt tmp = p.getNext();
                            move4End(irMethod.stmts, start, p);
                            start = null;
                            p = tmp;
                        } else {
                            p = p.getNext();
                        }
                        break;
                    default:
                        p = p.getNext();
                }
            }
            if (removeTrap) {
                irMethod.traps.remove(trap);
            }
        }
        StmtList stmts = irMethod.stmts;
        for (Stmt p = stmts.getFirst(); p != null; p = p.getNext()) {
            if (p.st == Stmt.ST.GOTO) {
                LabelStmt target = ((GotoStmt) p).target;
                Stmt next = target.getNext();
                if (next != null && (next.st == Stmt.ST.RETURN || next.st == Stmt.ST.RETURN_VOID)) {
                    Stmt nnext = next.clone(keepLocal);
                    stmts.insertAfter(p, nnext);
                    stmts.remove(p);
                    p = nnext;
                }
            }
        }

    }

    private void move4Label(StmtList stmts, LabelStmt start, Stmt end, LabelStmt label) {
        move4End(stmts, start, end);
        stmts.insertAfter(end, Stmts.nGoto(label));
    }

    private void move4End(StmtList stmts, LabelStmt start, Stmt end) {
        Stmt g1 = Stmts.nGoto(start);
        stmts.insertBefore(start, g1);
        Stmt last = stmts.getLast();
        while (last.st == Stmt.ST.GOTO && ((GotoStmt) last).target == start) {
            stmts.remove(last);
            last = stmts.getLast();
        }
        stmts.move(start, end, last);

    }
}
