package com.toto.johnwesonga.totoapp;

import android.app.Application;
import android.support.annotation.Nullable;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.toto.johnwesonga.totoapp.components.AppComponent;
import com.toto.johnwesonga.totoapp.components.DaggerAppComponent;
import com.toto.johnwesonga.totoapp.modules.AppModule;
import com.toto.johnwesonga.totoapp.modules.NetworkModule;
import com.toto.johnwesonga.totoapp.modules.NewsModule;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

/**
 * Created by johnwesonga on 8/27/17.
 */

public class MyApplication extends Application {
    private AppComponent myAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        myAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule())
                .newsModule(new NewsModule())
                .build();

        Timber.plant(BuildConfig.REPORT_CRASHES
                ? new Timber.DebugTree()
                : new CrashReportingTree());

        Fabric.with(this, new Crashlytics());


    }

    public AppComponent getAppComponent(){
        return myAppComponent;
    }

    private class CrashReportingTree extends Timber.Tree {
        private static final String CRASHLYTICS_KEY_PRIORITY = "priority";
        private static final String CRASHLYTICS_KEY_TAG = "tag";
        private static final String CRASHLYTICS_KEY_MESSAGE = "message";

        @Override
        protected void log(int priority, String tag, String message, Throwable throwable) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

            Throwable t = throwable != null
                    ? throwable
                    : new Exception(message);

            // Crashlytics
            Crashlytics.setInt(CRASHLYTICS_KEY_PRIORITY, priority);
            Crashlytics.setString(CRASHLYTICS_KEY_TAG, tag);
            Crashlytics.setString(CRASHLYTICS_KEY_MESSAGE, message);
            Crashlytics.logException(t);


        }
    }

}

