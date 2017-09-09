package com.toto.johnwesonga.totoapp.components;

import com.toto.johnwesonga.totoapp.ArticlesActivity;
import com.toto.johnwesonga.totoapp.BaseActivity;
import com.toto.johnwesonga.totoapp.MainActivity;
import com.toto.johnwesonga.totoapp.modules.AppModule;
import com.toto.johnwesonga.totoapp.modules.NetworkModule;
import com.toto.johnwesonga.totoapp.modules.NewsModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by johnwesonga on 8/27/17.
 */

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class, NewsModule.class})
public interface AppComponent {
    void inject(MainActivity activity);
    void inject(ArticlesActivity activity);

}
