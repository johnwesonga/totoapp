package com.toto.johnwesonga.totoapp.api;

import com.toto.johnwesonga.totoapp.model.Article;
import com.toto.johnwesonga.totoapp.model.Source;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


/**
 * Created by johnwesonga on 8/27/17.
 */

public interface NewsApi {

    @GET("articles")
    Observable <ArticlesResponse> getArticles(@QueryMap Map<String, String> options);

    @GET("articles")
    Observable <Response<ArticlesResponse>> getNewsArticles(@QueryMap Map<String, String> options);

    @GET("sources")
    Call <SourceResponse> getSources(@Query("language") String language);

    @GET("sources")
    Single<SourceResponse> getSourcez(@Query("language") String language);



}
