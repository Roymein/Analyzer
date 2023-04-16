package com.example.d2j;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD})
public @interface Opt {
    String argName() default "";

    String description() default "";

    boolean hasArg() default true;

    String longOpt() default "";

    String opt() default "";

    boolean required() default false;
}
