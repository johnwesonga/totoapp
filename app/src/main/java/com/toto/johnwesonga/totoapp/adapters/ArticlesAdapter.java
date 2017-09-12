package com.toto.johnwesonga.totoapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.toto.johnwesonga.totoapp.R;
import com.toto.johnwesonga.totoapp.model.Article;
import com.toto.johnwesonga.totoapp.model.Source;

import java.util.Collections;
import java.util.List;

import retrofit2.Callback;
import timber.log.Timber;

/**
 * Created by johnwesonga on 9/1/17.
 */

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {
    private List<Article> mArticles = Collections.emptyList();
    private LayoutInflater mInflater;

    public ArticlesAdapter(List<Article> data){
        mArticles= data;
    }

    @Override
    public ArticlesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.news_article_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ArticlesAdapter.ViewHolder viewHolder, int position) {
        final Article article = mArticles.get(position);

        String title = article.getTitle();
        String description = article.getDescription();
        Uri uri = Uri.parse(article.getUrlToImage());
        viewHolder.mArticleTitle.setText(title);
        viewHolder.mArticleDescription.setText(description);

        Picasso.with(viewHolder.mSourceImageView.getContext())
            .load(uri)
            .error(android.R.drawable.stat_notify_error).fit()
                .centerCrop()
                .into(viewHolder.mSourceImageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        viewHolder.mPreloader.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });

        viewHolder.mArticleCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = article.getUrl();
                Timber.d("clicked " + url);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mArticleTitle;
        private TextView mArticleDescription;
        private ImageView mSourceImageView;
        private ProgressBar mPreloader;
        private CardView mArticleCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            mArticleTitle = (TextView) itemView.findViewById(R.id.tv_article_title);
            mArticleDescription = (TextView) itemView.findViewById(R.id.tv_article_desc);
            mSourceImageView = (ImageView) itemView.findViewById(R.id.iv_article_image);
            mPreloader = (ProgressBar) itemView.findViewById(R.id.pb_preloader);
            mArticleCardView = (CardView) itemView.findViewById(R.id.article_card_view);
        }
    }

}
