package com.azazellj.githubwatcher;

import android.app.Application;

import com.azazellj.githubwatcher.injection.component.AppComponent;
import com.azazellj.githubwatcher.injection.component.DaggerAppComponent;
import com.azazellj.githubwatcher.injection.module.AppModule;

/**
 * Created by azazellj on 10/3/17.
 */

public class BaseApp
        extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = buildAppComponent();
    }

    private static AppComponent appComponent;

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    public static void setAppComponent(AppComponent component) {
        appComponent = component;
    }

    protected AppComponent buildAppComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule())
                .build();
    }
}
