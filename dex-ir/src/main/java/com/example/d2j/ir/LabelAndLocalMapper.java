package com.example.d2j.ir;

import com.example.d2j.ir.expr.Local;
import com.example.d2j.ir.stmt.LabelStmt;
import com.example.d2j.ir.stmt.Stmts;

import java.util.HashMap;
import java.util.Map;

public class LabelAndLocalMapper {
    Map<LabelStmt, LabelStmt> labels = new HashMap<>();
    Map<Local, Local> locals = new HashMap<>();

    public LabelStmt map(LabelStmt label) {
        LabelStmt nTarget = labels.get(label);
        if (nTarget == null) {
            nTarget = Stmts.nLabel();
            labels.put(label, nTarget);
        }
        return nTarget;
    }

    public Local map(Local local) {
        Local nTarget = locals.get(local);
        if (nTarget == null) {
            nTarget = (Local) local.clone();
            locals.put(local, nTarget);
        }
        return nTarget;
    }
}
