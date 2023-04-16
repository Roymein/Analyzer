package com.example.d2j.ir;


import com.example.d2j.ir.expr.Value;
import com.example.d2j.ir.stmt.Stmt;

/**
 * The number of argument
 *
 * @see Stmt
 * @see Value
 */
public enum ET {

    /**
     * no argument
     */
    E0,
    /**
     * 1 argument
     */
    E1,
    /**
     * 2 argument
     */
    E2,
    /**
     * 3+ argument
     */
    En
}