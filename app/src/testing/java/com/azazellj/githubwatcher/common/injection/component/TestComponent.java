package com.azazellj.githubwatcher.common.injection.component;

import com.azazellj.githubwatcher.common.injection.module.AppTestModule;
import com.azazellj.githubwatcher.injection.component.AppComponent;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by azazellj on 10/5/17.
 */

@Singleton
@Component(modules = AppTestModule.class)
public interface TestComponent
        extends AppComponent {
}