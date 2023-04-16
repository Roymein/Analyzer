package com.example.d2j.ir.ts;

import com.example.d2j.ir.IrMethod;
import com.example.d2j.ir.expr.Local;
import com.example.d2j.ir.expr.Value;
import com.example.d2j.ir.stmt.Stmt;
import com.example.d2j.ir.stmt.Stmts;

/**
 * convert
 *
 * <pre>
 * a = b.get();
 * </pre>
 * <p>
 * to
 *
 * <pre>
 * b.get();
 * </pre>
 * <p>
 * if a is not used in other place.
 */
public class VoidInvokeTransformer extends StatedTransformer {
    @Override
    public boolean transformReportChanged(IrMethod method) {
        if (method.locals.size() == 0) {
            return false;
        }
        int[] reads = Cfg.countLocalReads(method);
        boolean changed = false;
        for (Stmt p = method.stmts.getFirst(); p != null; p = p.getNext()) {
            if (p.st == Stmt.ST.ASSIGN && p.getOp1().vt == Value.VT.LOCAL) {
                Local left = (Local) p.getOp1();
                if (reads[left._ls_index] == 0) {
                    switch (p.getOp2().vt) {
                        case INVOKE_INTERFACE:
                        case INVOKE_NEW:
                        case INVOKE_SPECIAL:
                        case INVOKE_STATIC:
                        case INVOKE_VIRTUAL:
                            method.locals.remove(left);
                            Stmt nVoidInvoke = Stmts.nVoidInvoke(p.getOp2());
                            method.stmts.replace(p, nVoidInvoke);
                            p = nVoidInvoke;
                            changed = true;
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return changed;
    }

    @Override
    public void transform(IrMethod method) {
        transformReportChanged(method);

    }
}
