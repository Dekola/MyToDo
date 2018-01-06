package com.kola.mytodo.baas;

import android.app.Application;

/**
 * Created by ribads on 1/6/18.
 */

public class BaseApplication extends Application {

    static BaseApplication application;

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;
    }

    static public BaseApplication getInstance(){
        return application;
    }
}
