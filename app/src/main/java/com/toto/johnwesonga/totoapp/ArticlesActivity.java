package com.toto.johnwesonga.totoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.webkit.WebView;
import android.widget.Toast;

import com.toto.johnwesonga.totoapp.adapters.ArticlesAdapter;
import com.toto.johnwesonga.totoapp.api.ArticlesResponse;
import com.toto.johnwesonga.totoapp.api.NewsApi;
import com.toto.johnwesonga.totoapp.model.Article;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
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
    @NonNull
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private Disposable searchDisposable;

    private RecyclerView articlesRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    String API_KEY = BuildConfig.API_KEY;
    String newsSource;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);

        articlesRecycleView = (RecyclerView) findViewById(R.id.m_articles_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        articlesRecycleView.setLayoutManager(mLayoutManager);

        // inject
        ((MyApplication) getApplication()).getAppComponent().inject(this);

        Intent intent = getIntent();
        Bundle activitiesBundle = intent.getExtras();

        if (activitiesBundle!= null){
            newsSource = activitiesBundle.getString("NEWS_SOURCE");
            Timber.i(newsSource);
        }

        //loadNewsArticles(newsSource);
        loadArticles(newsSource);
    }

    void loadArticles(String newsSource){
        Map<String, String> data = new HashMap();
        data.put("source",newsSource);
        // data.put("sortBy", "latest");
        data.put("apiKey", API_KEY);
        mCompositeDisposable.add(newsApi.getNewsArticles(data)
            .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<ArticlesResponse>>() {
                    @Override
                    public void accept(@NonNull Response<ArticlesResponse> articlesResponse) throws Exception {
                        if (articlesResponse.isSuccessful()){
                            if (mAdapter == null) {
                                mAdapter = new ArticlesAdapter(articlesResponse.body().articles);
                                articlesRecycleView.setAdapter(mAdapter);
                            }
                        }else{

                            Timber.d("Error code " + articlesResponse.code());
                            Toast errorToast = Toast.makeText(ArticlesActivity.this, "Error loading news articles", Toast.LENGTH_LONG);
                            errorToast.show();
                            finish();
                        }
                    }
                })
                
        );
    }

    void loadNewsArticles(String newsSource){
        Map<String, String> data = new HashMap();
        data.put("source",newsSource);
        // data.put("sortBy", "latest");
        data.put("apiKey", API_KEY);

        mCompositeDisposable.add(newsApi.getArticles(data)
            .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<ArticlesResponse, List<Article>>() {
                    @Override
                    public List<Article> apply(@NonNull ArticlesResponse articlesResponse) throws Exception {
                        return articlesResponse.articles;
                    }
                }).subscribe(new Consumer<List<Article>>() {
                    @Override
                    public void accept(@NonNull List<Article> articles) throws Exception {
                        if (mAdapter == null) {
                            mAdapter = new ArticlesAdapter(articles);
                            articlesRecycleView.setAdapter(mAdapter);
                        }
                    }
                })
        );


    }
}
