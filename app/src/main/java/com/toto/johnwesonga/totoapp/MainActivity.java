package com.toto.johnwesonga.totoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.toto.johnwesonga.totoapp.adapters.SourceAdapter;
import com.toto.johnwesonga.totoapp.api.NewsApi;
import com.toto.johnwesonga.totoapp.api.SourceResponse;
import com.toto.johnwesonga.totoapp.model.Source;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements SourceAdapter.ListItemClickListener {
    //@Inject
    //Retrofit retrofit;
    @Inject
    NewsApi newsApi;

    @Inject Retrofit retrofit;

    @BindView(R.id.tv_result) TextView mTextView;
    @BindView(R.id.pb_preloader) ProgressBar mProgressBar;
    @BindView(R.id.m_sources_recycles_view) RecyclerView sourceRecycleView;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    String language = "en";
    String API_KEY = BuildConfig.API_KEY;
    public List<Source> results;

    private Toast mToast;

    @NonNull
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // inject
        ((MyApplication) getApplication()).getAppComponent().inject(this);

        sourceRecycleView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        sourceRecycleView.setLayoutManager(mLayoutManager);

        loadNewsSources();
       // loadNewsSourcesRx();

    }

    // load news sources directly using retrofit
    void loadNewsSources(){
        Call<SourceResponse> sources = newsApi.getSources(language);
        sources.enqueue(new Callback<SourceResponse>() {
            @Override
            public void onResponse(Call<SourceResponse> call, Response<SourceResponse> response) {
                if (response.isSuccessful()) {
                    results = response.body().sources;
                    if(mAdapter == null) {
                        mAdapter = new SourceAdapter(results, MainActivity.this);
                        sourceRecycleView.setAdapter(mAdapter);
                    }else {


                    }
                    Timber.d(Integer.toString(results.size()) + " sources retrieved");
                }

            }

            @Override
            public void onFailure(Call<SourceResponse> call, Throwable t) {
                Timber.e(t);
                mTextView.setText(t.toString());
            }


        });


    }

    // load using observables
    private void loadNewsSourcesRx() {
        mCompositeDisposable.add(newsApi.getSourcez(language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Timber.e(throwable);
                        mTextView.setText(throwable.toString());
                    }
                })
                .map(new Function<SourceResponse, List<Source>>() {

                    @Override
                    public List<Source> apply(@NonNull SourceResponse sourceResponse) throws Exception {
                        return sourceResponse.sources;
                    }
                }).subscribe(new Consumer<List<Source>>() {

                    @Override
                    public void accept(@NonNull List<Source> sources) throws Exception {
                        if (mAdapter == null) {
                            mAdapter = new SourceAdapter(sources, MainActivity.this);
                            sourceRecycleView.setAdapter(mAdapter);
                        }

                    }
                })
        );

    }

    @Override
    protected void onDestroy() {
        mCompositeDisposable.clear();
        super.onDestroy();

    }


    @Override
    public void onListItemClick(int clickedItemIndex, String source) {
        Intent articleIntent = new Intent(this, ArticlesActivity.class);
        articleIntent.putExtra("NEWS_SOURCE", source);
        startActivity(articleIntent);

    }


}
