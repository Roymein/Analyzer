package com.example.d2j.ir.ts;

import com.example.d2j.ir.IrMethod;
import com.example.d2j.ir.LocalVar;
import com.example.d2j.ir.expr.Local;
import com.example.d2j.ir.expr.Value;
import com.example.d2j.ir.stmt.Stmts;

/**
 * the {@link LocalVar#reg} in {@link LocalVar} may be replace to a constant value in {@link ConstTransformer}. This
 * class try to insert a new local before {@link LocalVar#start}.
 *
 * <p>
 * before:
 * </p>
 *
 * <pre>
 *   ...
 * L0:
 *   return a0
 * L1:
 * ======
 * .var L0 ~ L1 1 -> test // int
 * </pre>
 * <p>
 * after:
 * </p>
 *
 * <pre>
 *   ...
 *   d1 = 1
 * L0:
 *   return a0
 * L1:
 * ======
 * .var L0 ~ L1 d1 -> test // int
 * </pre>
 *
 * @author Panxiaobo
 */
public class FixVar implements Transformer {

    @Override
    public void transform(IrMethod irMethod) {
        int i = 0;
        for (LocalVar var : irMethod.vars) {
            if (var.reg.trim().vt != Value.VT.LOCAL) {
                if (var.reg.trim().vt == Value.VT.CONSTANT) {
                    Local n = new Local(i++);
                    Value old = var.reg.trim();
                    irMethod.stmts.insertBefore(var.start, Stmts.nAssign(n, old));
                    var.reg = n;
                    irMethod.locals.add(n);
                } else {
                    // throw new DexExcpeption("not support");
                }
            }
        }
    }
}
