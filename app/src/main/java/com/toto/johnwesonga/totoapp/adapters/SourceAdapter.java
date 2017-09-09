package com.toto.johnwesonga.totoapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.toto.johnwesonga.totoapp.MainActivity;
import com.toto.johnwesonga.totoapp.R;
import com.toto.johnwesonga.totoapp.model.Source;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Callback;
import timber.log.Timber;

/**
 * Created by johnwesonga on 8/29/17.
 */

public class SourceAdapter extends RecyclerView.Adapter<SourceAdapter.ViewHolder> {


    private List<Source> mSources = Collections.emptyList();
    private LayoutInflater mInflater;

    final private ListItemClickListener mOnClickListener;

    /**
     * The interface that receives onClick messages.
     */
    public interface ListItemClickListener {
        // refactor and pass row value
        void onListItemClick(int clickedItemIndex);
    }

    public SourceAdapter(List<Source> data, ListItemClickListener listener){
        //this.mInflater = LayoutInflater.from(context);
        this.mSources = data;
        mOnClickListener = listener;
    }

    @Override
    public SourceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyclerview_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final SourceAdapter.ViewHolder viewHolder, final int position) {
        Source newsSource = mSources.get(position);
        String name = newsSource.getName();
        //String imageUri = newsSource.getUrlsToLogos().getSmall();
        //String imageName = mSources.get(position).getUrlsToLogos().getSmall();
        viewHolder.mTextView.setText(name);
        //viewHolder.mSourceImageView
        //Picasso.with(viewHolder.mSourceImageView.getContext()).
         //load(newsSource.getUrlsToLogos().getSmall()).
         //error(android.R.drawable.stat_notify_error).fit().into(viewHolder.mSourceImageView);


    }

    @Override
    public int getItemCount() {
        return mSources.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextView;
        private ImageView mSourceImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.tv_source_name);
            mSourceImageView = (ImageView) itemView.findViewById(R.id.iv_source_image);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            Log.i("positon-of-clicked-item", String.valueOf(mTextView.getText()));
            //String clickedSource = mTextView.getText().toString();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }


}
