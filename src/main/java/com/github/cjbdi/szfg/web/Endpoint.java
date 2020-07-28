package com.github.cjbdi.szfg.web;

import java.lang.annotation.*;

/**
 * @author Boning Liang
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Endpoint {

    String value() default "";

    String path() default "";

    String produces() default "";

}
