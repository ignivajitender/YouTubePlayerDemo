package com.igniva.youtubeplayer.ui.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.igniva.youtubeplayer.model.DataGiphyPojo;

import java.util.ArrayList;

/**
 * Created by igniva-android-17 on 30/3/17.
 */

public class TempAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<DataGiphyPojo> dataGiphyPojos;
    private LayoutInflater inflater;

    public TempAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<DataGiphyPojo> objects) {
        this.context = context;
        this.dataGiphyPojos = objects;
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 25;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
