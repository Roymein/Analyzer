package com.example.d2j.ir.expr;

import com.example.d2j.ir.LabelAndLocalMapper;

public class Local extends Value.E0Expr {
    public int _ls_index;
    public String signature;
    public String debugName;

    public Local(String debugName) {
        super(Value.VT.LOCAL);
        this.debugName = debugName;
    }

    public Local(int index, String debugName) {
        super(Value.VT.LOCAL);
        this.debugName = debugName;
        this._ls_index = index;
    }

    public Local() {
        super(Value.VT.LOCAL);
    }

    public Local(int index) {
        super(Value.VT.LOCAL);
        this._ls_index = index;
    }

    @Override
    public Value clone() {
        Local clone = new Local(_ls_index);
        clone.debugName = debugName;
        clone.signature = this.signature;
        clone.valueType = this.valueType;
        return clone;
    }

    @Override
    public Value clone(LabelAndLocalMapper mapper) {
        return mapper.map(this);
    }

    @Override
    public String toString0() {
        if (debugName == null) {
            return "a" + _ls_index;
        } else {
            return debugName + "_" + _ls_index;
        }
    }
}
