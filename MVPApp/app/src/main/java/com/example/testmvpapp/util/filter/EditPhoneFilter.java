package com.example.testmvpapp.util.filter;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

public class EditPhoneFilter implements InputFilter {


    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        // 验证删除等按键
        if (TextUtils.isEmpty(source)) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        sb.append(dest.subSequence(0, dstart))
                .append(source)
                .append(dest.subSequence(dend, dest.length()));
        if (!FilterUtils.isInputPhoneFormat(sb.toString())) {
            return  "";
        }
        return source;
    }
}
