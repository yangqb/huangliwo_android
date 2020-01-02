package com.feitianzhu.huangliwo.utils.doubleclick;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package name: com.feitianzhu.fu700
 * user: yangqinbo
 * date: 2019/12/16
 * time: 20:45
 * email: 694125155@qq.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SingleClick {
    /* 点击间隔时间 */
    long value() default 1000;
}
