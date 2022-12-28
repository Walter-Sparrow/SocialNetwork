package com.dataart.secondmonth.audit;

import com.dataart.secondmonth.audit.handler.AuditHandler;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.METHOD})
@Retention(RUNTIME)
@Documented
public @interface Audit {

    String title();

    String message();

    boolean logUserInfo() default false;

    boolean logParams() default false;

    Class<?>[] projections() default {};

    Class<? extends AuditHandler> handler();

}
