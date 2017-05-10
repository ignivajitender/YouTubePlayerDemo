package com.igniva.youtubeplayer.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.igniva.youtubeplayer.R;
import com.igniva.youtubeplayer.model.DataGiphyPojo;

import java.util.ArrayList;

/**
 * Created by igniva-android-17 on 30/3/17.
 */

public class GifyAdapter extends RecyclerView.Adapter<GifyAdapter.ViewHolder> {

    public Context context;
    public ArrayList<DataGiphyPojo> dataGiphyPojos;

    public GifyAdapter(Context context, ArrayList<DataGiphyPojo> dataGiphyPojos) {
        this.context = context;
        this.dataGiphyPojos = dataGiphyPojos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_gif, parent, false);
        ViewHolder vhItem = new ViewHolder(v);
        return vhItem;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        DataGiphyPojo pojo = dataGiphyPojos.get(position);

        try {
            Glide.with(context).load(pojo.gif_url).asGif().into(holder.imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return dataGiphyPojos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.gifImageView);
        }
    };

}
