package com.toto.johnwesonga.totoapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.toto.johnwesonga.totoapp.adapters.SourceAdapter;
import com.toto.johnwesonga.totoapp.api.NewsApi;
import com.toto.johnwesonga.totoapp.api.SourceResponse;
import com.toto.johnwesonga.totoapp.components.AppComponent;
import com.toto.johnwesonga.totoapp.model.Article;
import com.toto.johnwesonga.totoapp.model.Source;
import com.toto.johnwesonga.totoapp.modules.NewsModule;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.Subject;
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

    private RecyclerView sourceRecycleView;
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

        sourceRecycleView = (RecyclerView) findViewById(R.id.m_sources_recycles_view);
        sourceRecycleView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        sourceRecycleView.setLayoutManager(mLayoutManager);

       // loadNewsSources();
        loadNewsSourcesRx();

    }

    // load news sources directly using retrofit
    void loadNewsSources(){
        Call<Source> sources = newsApi.getSources(language);
        sources.enqueue(new Callback<Source>() {
            @Override
            public void onResponse(Call<Source> call, Response<Source> response) {
                if (response.isSuccessful()) {
                    results = response.body().getSources();
                    if(mAdapter == null) {
                        mAdapter = new SourceAdapter(results, MainActivity.this);
                        sourceRecycleView.setAdapter(mAdapter);
                    }else {


                    }
                    Timber.d(Integer.toString(results.size()) + " sources retrieved");
                }

            }

            @Override
            public void onFailure(Call<Source> call, Throwable t) {
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
                .map(new Function<SourceResponse, List<Source>>() {

                    @Override
                    public List<Source> apply(@NonNull SourceResponse sourceResponse) throws Exception {
                        return sourceResponse.sources;
                    }
                }).subscribe(new Consumer<List<Source>>() {

                    @Override
                    public void accept(@NonNull List<Source> sources) throws Exception {
                        mAdapter = new SourceAdapter(sources, MainActivity.this);
                        sourceRecycleView.setAdapter(mAdapter);
                    }
                })
        );

    }


    @Override
    public void onListItemClick(int clickedItemIndex) {
        if (mToast != null) {
            mToast.cancel();
        }

        // COMPLETED (12) Show a Toast when an item is clicked, displaying that item number that was clicked
        /*
         * Create a Toast and store it in our Toast field.
         * The Toast that shows up will have a message similar to the following:
         *
         *                     Item #42 clicked.
         */
        String toastMessage = "Item #" + clickedItemIndex + " clicked.";
        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);

        mToast.show();
    }


}
