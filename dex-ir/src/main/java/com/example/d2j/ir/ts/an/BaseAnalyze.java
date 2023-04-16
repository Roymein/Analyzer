package com.example.d2j.ir.ts.an;

import com.example.d2j.ir.IrMethod;
import com.example.d2j.ir.expr.Local;
import com.example.d2j.ir.expr.Value;
import com.example.d2j.ir.stmt.AssignStmt;
import com.example.d2j.ir.stmt.LabelStmt;
import com.example.d2j.ir.stmt.Stmt;
import com.example.d2j.ir.ts.Cfg;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseAnalyze<T extends AnalyzeValue> implements Cfg.FrameVisitor<T[]>, Cfg.TravelCallBack {
    protected static final boolean DEBUG = false;

    public List<T> aValues = new ArrayList<T>();
    private boolean reindexLocal;
    private T[] currentFrame;

    protected int localSize;

    protected IrMethod method;

    private T[] tmpFrame;

    public BaseAnalyze(IrMethod method) {
        this(method, true);
    }

    public BaseAnalyze(IrMethod method, boolean reindexLocal) {
        super();
        this.method = method;
        if (!reindexLocal) {
            // override the localSize value to the max local index+1
            int maxReg = -1;
            for (Local local : method.locals) {
                if (local._ls_index > maxReg) {
                    maxReg = local._ls_index;
                }
            }
            this.localSize = maxReg + 1;
        } else {
            this.localSize = method.locals.size();
        }
        this.reindexLocal = reindexLocal;
    }

    public void analyze() {
        init();
        analyze0();
        analyzeValue();
    }

    protected void analyze0() {
        tmpFrame = newFrame(localSize);
        Cfg.dfs(method.stmts, this);
        tmpFrame = null;
    }

    protected void analyzeValue() {
    }

    protected void afterExec(T[] frame, Stmt stmt) {

    }

    @Override
    public T[] exec(T[] frame, Stmt stmt) {
        this.currentFrame = frame;
        try {
            Cfg.travel(stmt, this, false);
        } catch (Exception ex) {
            throw new RuntimeException("fail exe " + stmt, ex);
        }
        frame = this.currentFrame;
        this.currentFrame = null;
        afterExec(frame, stmt);
        return frame;
    }

    protected T getFromFrame(int idx) {
        return (T) currentFrame[idx];
    }

    protected T[] getFrame(Stmt stmt) {
        return (T[]) stmt.frame;
    }

    protected void setFrame(Stmt stmt, T[] frame) {
        stmt.frame = frame;
    }

    protected void init() {
        if (reindexLocal) {
            int index = 0;
            for (Local local : method.locals) {
                local._ls_index = index;
                index++;
            }
        }
        if (DEBUG) {
            int idx = 0;
            for (Stmt s : method.stmts) {
                if (s.st == Stmt.ST.LABEL) {
                    LabelStmt label = (LabelStmt) s;
                    label.displayName = "L" + idx++;
                }
            }
        }
        initCFG();
    }

    protected void initCFG() {
        Cfg.createCFG(method);
    }

    protected T[] newFrame() {
        return newFrame(localSize);
    }

    @Override
    public T[] initFirstFrame(Stmt first) {
        return newFrame(localSize);
    }

    protected abstract T[] newFrame(int size);

    protected abstract T newValue();

    @Override
    public Local onAssign(Local local, AssignStmt as) {
        System.arraycopy(currentFrame, 0, tmpFrame, 0, localSize);
        currentFrame = tmpFrame;
        T aValue = onAssignLocal(local, as.op2);
        aValues.add(aValue);
        currentFrame[local._ls_index] = aValue;
        return local;
    }

    protected T onAssignLocal(Local local, Value value) {
        return newValue();
    }

    @Override
    public Local onUse(Local local) {
        T aValue = (T) currentFrame[local._ls_index];
        onUseLocal(aValue, local);
        return local;
    }

    protected void onUseLocal(T aValue, Local local) {
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Stmt stmt = method.stmts.getFirst(); stmt != null; stmt = stmt.getNext()) {
            T[] frame = (T[]) stmt.frame;
            if (frame != null) {
                for (T p : frame) {
                    if (p == null) {
                        sb.append('.');
                    } else {
                        sb.append(p.toRsp());
                    }
                }
                sb.append(" | ");
            }
            sb.append(stmt.toString()).append('\n');
        }
        return sb.toString();
    }
}
