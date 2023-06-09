package com.example.d2j.ir.ts;

import com.example.d2j.ir.IrMethod;
import com.example.d2j.ir.Trap;
import com.example.d2j.ir.expr.Local;
import com.example.d2j.ir.expr.PhiExpr;
import com.example.d2j.ir.expr.Value;
import com.example.d2j.ir.stmt.AssignStmt;
import com.example.d2j.ir.stmt.LabelStmt;
import com.example.d2j.ir.stmt.Stmt;

import java.util.*;

public class DeadCodeTransformer implements Transformer {

    @Override
    public void transform(IrMethod method) {
        Cfg.createCFG(method);
        Cfg.dfsVisit(method, null);
        if (method.traps != null) {
            for (Iterator<Trap> it = method.traps.iterator(); it.hasNext(); ) {
                Trap t = it.next();
                boolean allNotThrow = true;
                for (Stmt p = t.start; p != t.end; p = p.getNext()) {
                    if (p.visited && Cfg.isThrow(p)) {
                        allNotThrow = false;
                        break;
                    }
                }
                if (allNotThrow) {
                    it.remove();
                    continue;
                }

                boolean allNotVisited = true;
                boolean allVisited = true;
                for (LabelStmt labelStmt : t.handlers) {
                    if (labelStmt.visited) {
                        allNotVisited = false;
                    } else {
                        allVisited = false;
                    }
                }
                if (allNotVisited) {
                    it.remove();
                } else {
                    // keep start and end
                    t.start.visited = true;
                    t.end.visited = true;
                    if (!allVisited) { // part visited
                        List<String> types = new ArrayList<>(t.handlers.length);
                        List<LabelStmt> labelStmts = new ArrayList<>(t.handlers.length);
                        for (int i = 0; i < t.handlers.length; i++) {
                            labelStmts.add(t.handlers[i]);
                            types.add(t.types[i]);
                        }
                        t.handlers = labelStmts.toArray(new LabelStmt[labelStmts.size()]);
                        t.types = types.toArray(new String[types.size()]);
                    }
                }
            }
        }
        Set<Local> definedLocals = new HashSet<>();
        for (Iterator<Stmt> it = method.stmts.iterator(); it.hasNext(); ) {
            Stmt p = it.next();
            if (!p.visited) {
                it.remove();
                continue;
            }
            if (p.st == Stmt.ST.ASSIGN || p.st == Stmt.ST.IDENTITY) {
                if (p.getOp1().vt == Value.VT.LOCAL) {
                    definedLocals.add((Local) p.getOp1());
                }
            }
        }
        if (method.phiLabels != null) {
            for (Iterator<LabelStmt> it = method.phiLabels.iterator(); it.hasNext(); ) {
                LabelStmt labelStmt = it.next();
                if (!labelStmt.visited) {
                    it.remove();
                    continue;
                }
                if (labelStmt.phis != null) {
                    for (AssignStmt phi : labelStmt.phis) {
                        definedLocals.add((Local) phi.getOp1());
                    }
                }
            }
        }

        method.locals.clear();
        method.locals.addAll(definedLocals);
        Set<Value> tmp = new HashSet<>();
        if (method.phiLabels != null) {
            for (Iterator<LabelStmt> it = method.phiLabels.iterator(); it.hasNext(); ) {
                LabelStmt labelStmt = it.next();
                if (labelStmt.phis != null) {
                    for (AssignStmt phi : labelStmt.phis) {
                        PhiExpr phiExpr = (PhiExpr) phi.getOp2();
                        boolean needRebuild = false;
                        for (Value v : phiExpr.getOps()) {
                            if (!definedLocals.contains(v)) {
                                needRebuild = true;
                                break;
                            }
                        }
                        if (needRebuild) {
                            for (Value v : phiExpr.getOps()) {
                                if (definedLocals.contains(v)) {
                                    tmp.add(v);
                                }
                            }
                            phiExpr.setOps(tmp.toArray(new Value[tmp.size()]));
                            tmp.clear();
                        }
                    }
                }
            }
        }
    }
}
