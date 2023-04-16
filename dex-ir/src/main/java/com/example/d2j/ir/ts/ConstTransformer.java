package com.example.d2j.ir.ts;

import com.example.d2j.ir.IrMethod;
import com.example.d2j.ir.expr.*;
import com.example.d2j.ir.stmt.AssignStmt;
import com.example.d2j.ir.stmt.Stmt;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

/**
 * Replace must-be-constant local to constant
 * <p/>
 * Require a SSA form, usually run after {@link SSATransformer}
 */
public class ConstTransformer implements Transformer {
    @Override
    public void transform(IrMethod m) {

        // 1. init
        init(m);

        // 2. collect
        collect(m);

        // 3. mark constant
        markConstant(m);
        markReplacable(m);
        // 4. replace
        replace(m);

        // 5. clean
        clean(m);
    }

    private void clean(IrMethod m) {
        for (Local local : m.locals) {
            local.tag = null;
        }
    }

    private void replace(IrMethod m) {
        Cfg.travelMod(m.stmts, new Cfg.TravelCallBack() {

            @Override
            public Value onUse(Local v) {
                ConstAnalyzeValue cav = (ConstAnalyzeValue) v.tag;
                if (cav.replaceable) {
                    return Exprs.nConstant(cav.cst);
                }
                return v;
            }

            @Override
            public Value onAssign(Local v, AssignStmt as) {
                ConstAnalyzeValue cav = (ConstAnalyzeValue) v.tag;
                if (cav.replaceable) {
                    if (as.op2.trim().vt != Value.VT.CONSTANT) {
                        as.op2 = Exprs.nConstant(cav.cst);
                    }
                }
                return v;
            }

        }, true);
    }

    private void markReplacable(IrMethod m) {
        for (Local local : m.locals) {
            ConstAnalyzeValue cav = (ConstAnalyzeValue) local.tag;
            if (Boolean.TRUE.equals(cav.isConst)) {
                boolean allTosAreCst = true;
                for (ConstAnalyzeValue c : cav.assignTo) {
                    if (!Boolean.TRUE.equals(c.isConst)) {
                        allTosAreCst = false;
                        break;
                    }
                }
                if (allTosAreCst) {
                    cav.replaceable = true;
                }
            }
        }
    }

    private void markConstant(IrMethod m) {
        Queue<Local> queue = new UniqueQueue<>();
        queue.addAll(m.locals);
        while (!queue.isEmpty()) {
            ConstAnalyzeValue cav = (ConstAnalyzeValue) queue.poll().tag;

            Object cst = cav.cst;

            if (cav.isConst == null) {
                if (cst != null) {// we have a cst
                    boolean allCstEquals = true;
                    for (ConstAnalyzeValue p0 : cav.assignFrom) {
                        if (!cst.equals(p0.cst)) {
                            allCstEquals = false;
                            break;
                        }
                    }
                    if (allCstEquals) {
                        cav.isConst = true;

                    }
                }
            }

            if (cst != null || Boolean.TRUE.equals(cav.isConst)) {
                for (ConstAnalyzeValue p0 : cav.assignTo) {
                    if (p0.isConst == null) {
                        if (p0.cst == null) {
                            p0.cst = cst;
                        }
                        queue.add(p0.local);
                    }
                }
            }

            if (Boolean.FALSE.equals(cav.isConst)) {
                cav.cst = null;
                for (ConstAnalyzeValue c : cav.assignTo) {
                    if (!Boolean.FALSE.equals(c.isConst)) {
                        c.cst = null;
                        c.isConst = false;
                        queue.add(c.local);
                    }
                }
            }
        }
    }

    private void collect(IrMethod m) {
        for (Stmt p = m.stmts.getFirst(); p != null; p = p.getNext()) {
            if (p.st == Stmt.ST.ASSIGN || p.st == Stmt.ST.IDENTITY) {
                Stmt.E2Stmt e2 = (Stmt.E2Stmt) p;
                Value op1 = e2.op1.trim();
                Value op2 = e2.op2.trim();
                if (op1.vt == Value.VT.LOCAL) {
                    ConstAnalyzeValue cav = (ConstAnalyzeValue) ((Local) op1).tag;
                    if (op2.vt == Value.VT.CONSTANT) {
                        Constant c = (Constant) op2;
                        cav.isConst = true;
                        cav.cst = c.value;
                    } else if (op2.vt == Value.VT.LOCAL) {
                        Local local2 = (Local) op2;
                        ConstAnalyzeValue zaf2 = (ConstAnalyzeValue) local2.tag;
                        cav.assignFrom.add(zaf2);
                        zaf2.assignTo.add(cav);
                    } else if (op2.vt == Value.VT.PHI) {
                        PhiExpr pe = (PhiExpr) op2;
                        for (Value v : pe.ops) {
                            ConstAnalyzeValue zaf2 = (ConstAnalyzeValue) ((Local) v.trim()).tag;
                            cav.assignFrom.add(zaf2);
                            zaf2.assignTo.add(cav);
                        }
                    } else {
                        cav.isConst = Boolean.FALSE;
                    }
                }
            }
        }
    }

    private void init(IrMethod m) {
        for (Local local : m.locals) {
            local.tag = new ConstAnalyzeValue(local);
        }
    }

    static class ConstAnalyzeValue {
        private static final Integer ZERO = 0;
        public final Local local;
        public Boolean isConst = null;
        public boolean replaceable = false;
        public Object cst;
        public Set<ConstAnalyzeValue> assignFrom = new HashSet<>(3);
        public Set<ConstAnalyzeValue> assignTo = new HashSet<>(3);

        public ConstAnalyzeValue(Local local) {
            super();
            this.local = local;
        }

        public boolean isZero() {
            if (isConst == null) {
                return false;
            }
            return isConst && (ZERO.equals(cst));
        }
    }
}
