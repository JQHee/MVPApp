package com.example.testmvpapp.util.filter;

import android.text.InputFilter;
import android.text.Spanned;

public class NameFilter implements InputFilter {

    private int mMaxLen = 16;

    public NameFilter() {
    }

    public NameFilter(int maxLen) {
        this.mMaxLen = maxLen;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        int dindex = 0;
        int count = 0; // 判断是否到达最大长度

        while (count <= mMaxLen && dindex < dest.length()) {
            char c = dest.charAt(dindex++);
            if (c < 128) {// 按ASCII码表0-127算
                count = count + 1;
            } else {
                count = count + 2;
            }
        }

        if (count > mMaxLen) {
            return dest.subSequence(0, dindex - 1);
        }

        int sindex = 0;
        while (count <= mMaxLen && sindex < source.length()) {
            char c = source.charAt(sindex++);
            if (c < 128) {
                count = count + 1;
            } else {
                count = count + 2;
            }
        }

        if (count > mMaxLen) {
            sindex--;
        }
        return source.subSequence(0, sindex);
    }
}
