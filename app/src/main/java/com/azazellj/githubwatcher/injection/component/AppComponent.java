package com.azazellj.githubwatcher.injection.component;

import com.azazellj.githubwatcher.data.ApiRestService;
import com.azazellj.githubwatcher.injection.module.AppModule;
import com.azazellj.githubwatcher.ui.main.MainActivity;
import com.azazellj.githubwatcher.ui.repository.detail.RepositoryDetailActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by azazellj on 10/3/17.
 */
@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {
    void inject(MainActivity activity);
    void inject(RepositoryDetailActivity activity);

    ApiRestService getRestService();
}
