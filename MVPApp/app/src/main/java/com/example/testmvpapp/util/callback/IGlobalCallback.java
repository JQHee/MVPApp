package com.example.testmvpapp.util.callback;

import android.support.annotation.NonNull;


public interface IGlobalCallback<T> {

    void executeCallback(@NonNull T args);
}
