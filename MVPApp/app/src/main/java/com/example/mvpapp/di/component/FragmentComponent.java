package com.example.mvpapp.di.component;

import android.app.Activity;

import com.example.mvpapp.di.module.FragmentModule;
import com.example.mvpapp.di.scope.FragmentScope;
import dagger.Component;

/**
 * Created by hjq
 */

@FragmentScope
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    Activity getActivity();


}
