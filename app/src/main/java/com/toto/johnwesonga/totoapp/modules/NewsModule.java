package com.toto.johnwesonga.totoapp.modules;

import com.toto.johnwesonga.totoapp.api.NewsApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by johnwesonga on 8/27/17.
 */

@Module
public class NewsModule {

    @Provides
    @Singleton
    NewsApi provideRetrofit(Retrofit retrofit) {
        return retrofit.create(NewsApi.class);
    }
}
