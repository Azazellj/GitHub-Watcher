package com.azazellj.githubwatcher.common.injection.rule;

import com.azazellj.githubwatcher.BaseApp;
import com.azazellj.githubwatcher.common.injection.component.DaggerTestComponent;
import com.azazellj.githubwatcher.common.injection.component.TestComponent;
import com.azazellj.githubwatcher.common.injection.module.AppTestModule;
import com.azazellj.githubwatcher.data.ApiRestService;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * Created by azazellj on 10/5/17.
 */

public class TestComponentRule
        implements TestRule {

    private TestComponent mTestComponent;

    public ApiRestService getMockRestService() {
        return mTestComponent.getRestService();
    }

    private void setupDaggerTestComponentInApplication() {
        mTestComponent = DaggerTestComponent.builder()
                .appTestModule(new AppTestModule())
                .build();
        BaseApp.setAppComponent(mTestComponent);
    }

    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    setupDaggerTestComponentInApplication();
                    base.evaluate();
                } finally {
                    mTestComponent = null;
                }
            }
        };
    }
}