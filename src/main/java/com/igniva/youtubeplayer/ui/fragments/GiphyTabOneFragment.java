package com.igniva.youtubeplayer.ui.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.igniva.youtubeplayer.R;
import com.igniva.youtubeplayer.model.DataGiphyPojo;
import com.igniva.youtubeplayer.ui.adapters.GifyAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by igniva-android-17 on 30/3/17.
 */

public class GiphyTabOneFragment extends BaseFragment {

    private View view;

    private RecyclerView gifLV;
    private ArrayList<DataGiphyPojo> dataGiphyPojos = new ArrayList<>();
    private GifyAdapter gifyAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_giphy_tab_one, container, false);

            new RetrieveFeedTask().execute("http://api.giphy.com/v1/gifs/search?q=funny+dog&api_key=dc6zaTOxFJmzC");

        }

        return view;
    }

    @Override
    public void setUpLayouts() {
        gifLV = (RecyclerView) view.findViewById(R.id.gifLV);
        if (gifyAdapter == null) {
            gifyAdapter = new GifyAdapter(getActivity(), dataGiphyPojos);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            gifLV.setLayoutManager(mLayoutManager);
            gifLV.setAdapter(gifyAdapter);
        } else {
            gifyAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void setDataInViewLayouts() {

    }

    class RetrieveFeedTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    connection.setRequestMethod("GET");
                } catch (ProtocolException e) {
                    e.printStackTrace();
                }
                try {
                    connection.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                StringBuilder total = new StringBuilder();
                try {
                    int code = connection.getResponseCode();

                    BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = r.readLine()) != null) {
                        total.append(line).append('\n');
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return total.toString();
            } catch (Exception e) {
                this.exception = e;

                return null;
            }
        }

        protected void onPostExecute(String feed) {
            if (feed != null && !feed.isEmpty()) {
                String json = feed.trim();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.has("data")) {
                        JSONArray array = jsonObject.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            if (object.has("images")) {
                                JSONObject imagesObject = object.getJSONObject("images");
                                if (imagesObject.has("original")) {
                                    JSONObject originalImagesObject = imagesObject.getJSONObject("original");
                                    DataGiphyPojo pojo = new DataGiphyPojo(Parcel.obtain());
                                    pojo.gif_url = originalImagesObject.getString("url");
                                    dataGiphyPojos.add(pojo);
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            setUpLayouts();

        }
    }


}
