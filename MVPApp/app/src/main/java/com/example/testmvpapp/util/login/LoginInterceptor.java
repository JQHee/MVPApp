package com.example.testmvpapp.util.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.testmvpapp.sections.sign.SignInActivity;

/**
 * 登录判断类
 */
public class LoginInterceptor {

    // 这里获取登录状态
    private static boolean getLogin() {
        return LoginConfig.isLogin();
    }

    /**
     * @param ctx 当前activity的上下文
     * @param target 目标activity
     * @param bundle 需要传递的参数
     * @param intent
     */
    public static void interceptor(Context ctx, String target, Bundle bundle, Intent intent) {
        if (target != null && target.length() > 0) {
            LoginCarrier invoker = new LoginCarrier(target, bundle);
            if (getLogin()) {
                invoker.invoke(ctx);
            } else {
                if (intent == null) {
                    intent = new Intent(ctx, SignInActivity.class);
                    intent.putExtra(LoginConfig.LOGIN_INTERCEPTOR_INVOKE, invoker);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ctx.startActivity(intent);
            }
        }
    }

    public static void interceptor(Context ctx, String target, Bundle bundle) {
        interceptor(ctx, target, bundle, null);
    }
}
