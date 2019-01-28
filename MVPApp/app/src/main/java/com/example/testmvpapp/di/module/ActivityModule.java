package com.example.testmvpapp.di.module;

import android.app.Activity;
import com.example.testmvpapp.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;

/**
 * @desc ActivityModule
 * @author hjq
 * @date 2018/10/9
 */

@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    @ActivityScope
    public Activity provideActivity() {
        return mActivity;
    }


}
