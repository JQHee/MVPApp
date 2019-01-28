package com.example.testmvpapp.di.module;

import com.example.testmvpapp.app.MyApplication;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

/**
 * @desc AppModule
 * @author hjq
 * @date 2018/10/9
 */
@Module
public class AppModule {
    private final MyApplication application;

    public AppModule(MyApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    MyApplication provideApplicationContext() {
        return application;
    }

}
