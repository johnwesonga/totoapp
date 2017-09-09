package com.toto.johnwesonga.totoapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.toto.johnwesonga.totoapp.model.Article;
import com.toto.johnwesonga.totoapp.model.Source;

import java.util.Collections;
import java.util.List;

import retrofit2.Callback;

/**
 * Created by johnwesonga on 9/1/17.
 */

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {
    private List<Article> mArticles = Collections.emptyList();
    private LayoutInflater mInflater;

    public ArticlesAdapter(Callback<Article> callback, List<Article> data){
        mArticles= data;
    }

    @Override
    public ArticlesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        mInflater = LayoutInflater.from(context);
        return null;
    }

    @Override
    public void onBindViewHolder(ArticlesAdapter.ViewHolder viewHolder, int position) {
        Article article = mArticles.get(position);
        String title = article.getTitle();
        viewHolder.mArticleTitle.setText(title);
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mArticleTitle;
        private ImageView mSourceImageView;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
