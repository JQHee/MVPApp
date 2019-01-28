package com.example.testmvpapp.di.component;

import android.app.Activity;

import com.example.testmvpapp.di.module.FragmentModule;
import com.example.testmvpapp.di.scope.FragmentScope;
import dagger.Component;

/**
 * Created by hjq
 */

@FragmentScope
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    Activity getActivity();


}
