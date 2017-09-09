package com.toto.johnwesonga.totoapp.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by johnwesonga on 8/27/17.
 */

@Module
public class AppModule {
    private Context context;

    public AppModule(Application app){
        this.context = app;
    }

    @Provides
    Context providesContext() {
        return context;
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }
}
