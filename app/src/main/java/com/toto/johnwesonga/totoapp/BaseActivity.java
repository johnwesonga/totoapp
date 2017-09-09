package com.toto.johnwesonga.totoapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.toto.johnwesonga.totoapp.components.AppComponent;

/**
 * Created by johnwesonga on 9/6/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected abstract void injectDependencies(AppComponent appComponent);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApplication) getApplication()).getAppComponent();

    }

    protected abstract void inject(AppComponent component);
}
