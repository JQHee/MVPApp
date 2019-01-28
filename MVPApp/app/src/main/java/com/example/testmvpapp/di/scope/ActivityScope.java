package com.example.testmvpapp.di.scope;

import java.lang.annotation.Retention;
import javax.inject.Scope;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by hjq
 */

@Scope
@Retention(RUNTIME)
public @interface ActivityScope {
}
