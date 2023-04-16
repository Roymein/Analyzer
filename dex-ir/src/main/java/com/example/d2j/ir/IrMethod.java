package com.example.d2j.ir;

import com.example.d2j.ir.expr.Local;
import com.example.d2j.ir.stmt.LabelStmt;
import com.example.d2j.ir.stmt.StmtList;

import java.util.ArrayList;
import java.util.List;

public class IrMethod {

    public boolean isStatic;
    public String[] args;
    public List<Local> locals = new ArrayList<>();
    public String name;

    public String owner;

    public String ret;

    public StmtList stmts = new StmtList();

    public List<Trap> traps = new ArrayList<>();
    public List<LocalVar> vars = new ArrayList<>();
    public List<LabelStmt> phiLabels;

    @Override
    public IrMethod clone() {
        IrMethod n = new IrMethod();
        LabelAndLocalMapper mapper = new LabelAndLocalMapper();
        n.name = name;
        n.args = args;
        n.isStatic = isStatic;
        n.owner = owner;
        n.ret = ret;
        n.stmts = stmts.clone(mapper);
        for (Trap trap : traps) {
            n.traps.add(trap.clone(mapper));
        }
        for (LocalVar var : vars) {
            n.vars.add(var.clone(mapper));
        }
        if (phiLabels != null) {
            List<LabelStmt> nPhiLabels = new ArrayList<>(phiLabels.size());
            for (LabelStmt labelStmt : phiLabels) {
                nPhiLabels.add(labelStmt.clone(mapper));
            }
            n.phiLabels = nPhiLabels;
        }
        for (Local local : locals) {
            n.locals.add((Local) local.clone(mapper));
        }
        return n;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("// ").append(this.owner).append("\n");
        if (isStatic) {
            sb.append(" static ");
        }
        sb.append(ret == null ? null : Util.toShortClassName(ret)).append(' ').append(this.name).append('(');
        if (args != null) {
            boolean first = true;
            for (String arg : args) {
                if (first) {
                    first = false;
                } else {
                    sb.append(',');
                }
                sb.append(Util.toShortClassName(arg));
            }
        }
        sb.append(") {\n\n").append(stmts).append("\n");
        if (traps.size() > 0 || vars.size() > 0) {
            sb.append("=============\n");
            for (Trap trap : traps) {
                sb.append(trap).append('\n');
            }
            for (LocalVar var : vars) {
                sb.append(var).append('\n');
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
