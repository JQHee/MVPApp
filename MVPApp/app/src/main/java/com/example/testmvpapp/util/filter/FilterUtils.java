package com.example.testmvpapp.util.filter;

import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.text.TextUtils;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilterUtils {

    /**
     * 设置过滤器
     */
    public static void setFilter(Object editText, InputFilter filter) {
        InputFilter[] filters = {filter};
        if (editText instanceof EditText) {
            ((EditText)editText).setFilters(filters);
        }

        if (editText instanceof AppCompatEditText) {
            ((AppCompatEditText)editText).setFilters(filters);
        }

        if (editText instanceof TextInputEditText) {
            ((TextInputEditText)editText).setFilters(filters);
        }
    }

    private static  boolean match(String regex, String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();

    }

    /**
     * 电话号码格式
     */
    public static  boolean isInputPhoneFormat(String phoneNumber) {
        String regular = "^1[0-9]{0, 10}$";
        return  match(regular, phoneNumber);

    }

    /**
     * 输入的是字母和数组组合
     */
    public static  boolean isInputOnlyLatterOrNumberFormat(String content) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("^").append("[a-zA-Z0-9]{0,20}").append("$");
        String regular = stringBuffer.toString();
        return  match(regular, content);

    }


}
