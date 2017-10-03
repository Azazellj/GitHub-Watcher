package com.azazellj.githubwatcher.injection.module;

import com.azazellj.githubwatcher.data.ApiRestService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by azazellj on 10/3/17.
 */
@Module
public class AppModule {
    @Provides
    @Singleton
    ApiRestService provideApiRestService() {
        return ApiRestService.Creator.newRestService();
    }
}
