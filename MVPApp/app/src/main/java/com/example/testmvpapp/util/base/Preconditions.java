package com.example.testmvpapp.util.base;

import android.support.annotation.NonNull;

public class Preconditions {

    public static @NonNull
    <T> T checkNotNull(final T reference) {
        if (reference == null) {
            return null;
        }
        return reference;
    }
}
