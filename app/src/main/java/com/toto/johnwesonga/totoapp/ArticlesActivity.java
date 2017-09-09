package com.toto.johnwesonga.totoapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.toto.johnwesonga.totoapp.adapters.ArticlesAdapter;
import com.toto.johnwesonga.totoapp.api.NewsApi;
import com.toto.johnwesonga.totoapp.components.AppComponent;
import com.toto.johnwesonga.totoapp.model.Article;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;


/**
 * Created by johnwesonga on 8/29/17.
 */

public class ArticlesActivity extends AppCompatActivity {
    @Inject
    Retrofit retrofit;
    @Inject
    NewsApi newsApi;

    public List<Article> articlesResults;

    private RecyclerView articlesRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    String API_KEY = BuildConfig.API_KEY;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);

        articlesRecycleView = (RecyclerView) findViewById(R.id.m_articles_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        articlesRecycleView.setLayoutManager(mLayoutManager);

        loadNewsArticles();

    }



    void loadNewsArticles(){
        Map<String, String> data = new HashMap();
        data.put("source","");
        data.put("sortBy", "latest");
        data.put("apiKey", API_KEY);

        Call<Article> articles = newsApi.getArticles(data);
        articles.enqueue(new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                if (response.isSuccessful()) {
                    articlesResults = response.body().getArticles();
                    if(mAdapter == null) {
                         mAdapter = new ArticlesAdapter(this, articlesResults);
                        articlesRecycleView.setAdapter(mAdapter);
                    }else {


                    }
                }
            }

            @Override
            public void onFailure(Call<Article> call, Throwable t) {

            }
        });
    }
}
