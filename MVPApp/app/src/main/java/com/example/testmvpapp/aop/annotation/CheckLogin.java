package com.example.testmvpapp.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by baixiaokang on 16/10/20.
 * 检查用户是否登陆注解，通过aop切片的方式在编译期间织入源代码中
 * 功能：检查用户是否登陆，未登录提示登录，不会执行下面的逻辑
 **/
/*
@Target(ElementType.METHOD)：表示该注解只能注解在方法上。如果想类和方法都可以用，那可以这么写：@Target({ElementType.METHOD,ElementType.TYPE})，依此类推。
@Retention(RetentionPolicy.RUNTIME)：表示该注解在程序运行时是可见的（还有SOURCE、CLASS分别指定注解对于那个级别是可见的，一般都是用RUNTIME）。
        其中的value和type是自己拓展的属性，方便存储一些额外的信息
*/

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface CheckLogin {
}
