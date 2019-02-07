package com.example.testmvpapp.component.rx.rxlife;

import rx.Observable;


public interface FragmentLifeOperator {
    <T> Observable.Transformer<T, T> bindUntilEvent(final FragmentEvent bindEvent);
}
