package com.example.d2j;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.TYPE })
public @interface Syntax {

    String cmd();

    String desc() default "";

    String onlineHelp() default "";

    String syntax() default "";

}
