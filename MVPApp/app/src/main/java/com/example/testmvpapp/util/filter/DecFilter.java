package com.example.testmvpapp.util.filter;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * 两位小数的输入的自定义 InputFilter
 */
public class DecFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source,
                               int start, int end, Spanned dest, int dstart, int dend) {
        //不能在倒数第三位以前输入点
        if (".".equals(source.toString()) && dstart + 2 < dest.length()) return "";
        //控制小数位数为两位
        String[] splitArray = dest.toString().split("\\.");
        if (splitArray.length > 1) {
            //如果已经含有两位小数，判断插入的地方是否为小数位，是则过滤
            if (splitArray[1].length() == 2 && dstart + 2 >= dest.length()) {
                return "";
            } else {
                return source;
            }
        }
        return source;
    }
}
