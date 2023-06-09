/*
 * Copyright (c) 2009-2012 Panxiaobo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.d2j.ir.stmt;

import com.example.d2j.ir.LabelAndLocalMapper;
import com.example.d2j.ir.expr.Value;

/**
 * Represent a IF statement
 *
 * @see ST#IF
 */
public class IfStmt extends Stmt.E1Stmt implements JumpStmt {

    public LabelStmt target;

    public LabelStmt getTarget() {
        return target;
    }

    public void setTarget(LabelStmt target) {
        this.target = target;
    }

    /**
     * IF
     *
     * @param type
     * @param condition
     * @param target
     */
    public IfStmt(ST type, Value condition, LabelStmt target) {
        super(type, condition);
        this.target = target;
    }

    @Override
    public Stmt clone(LabelAndLocalMapper mapper) {
        LabelStmt nTarget = mapper.map(target);
        return new IfStmt(st, op.clone(mapper), nTarget);
    }

    @Override
    public String toString() {
        return "if " + op + " GOTO " + target.getDisplayName();
    }
}
