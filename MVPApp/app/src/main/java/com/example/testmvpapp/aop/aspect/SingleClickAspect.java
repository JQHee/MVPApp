package com.example.testmvpapp.aop.aspect;

import android.view.View;


import com.example.testmvpapp.R;
import com.example.testmvpapp.util.log.LatteLogger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Calendar;

/**
 * Created by HJQ.
 * 防止View被连续点击,间隔时间600ms
 */


@Aspect
public class SingleClickAspect {

    private static Long sLastclick = 0L;
    private static final Long FILTER_TIMEM = 600L;

    /*
    @Pointcut("execution(@com.example.testmvpapp.aop.annotation.SingleClick * *(..))")//方法切入点
    public void methodAnnotated() {
    }

    @Around("methodAnnotated()")//在连接点进行方法替换
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        if (System.currentTimeMillis() - sLastclick >= FILTER_TIMEM) {
            sLastclick = System.currentTimeMillis();
            try {
                joinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } else {
            LatteLogger.e("ClickFilterHook", "重复点击,已过滤");
        }
    }
    */

    @Around("execution(* android.view.View.OnClickListener.onClick(..))")
    public void clickFilterHook(ProceedingJoinPoint joinPoint) {
        if (System.currentTimeMillis() - sLastclick >= FILTER_TIMEM) {
            sLastclick = System.currentTimeMillis();
            try {
                joinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } else {
            LatteLogger.e("ClickFilterHook", "重复点击,已过滤");
        }
    }
}
