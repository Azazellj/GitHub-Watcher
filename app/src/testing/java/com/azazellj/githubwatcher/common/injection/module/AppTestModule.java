package com.azazellj.githubwatcher.common.injection.module;

import com.azazellj.githubwatcher.data.ApiRestService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

/**
 * Created by azazellj on 10/5/17.
 */
@Module
public class AppTestModule {
    public AppTestModule() {
    }

    @Provides
    @Singleton
    ApiRestService provideApiRestService() {
        return mock(ApiRestService.class);
    }
}
