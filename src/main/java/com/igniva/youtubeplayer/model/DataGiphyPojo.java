package com.igniva.youtubeplayer.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by igniva-android-17 on 30/3/17.
 */

public class DataGiphyPojo implements Parcelable {

    public String gif_url;

    public DataGiphyPojo(Parcel in) {
        gif_url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(gif_url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DataGiphyPojo> CREATOR = new Creator<DataGiphyPojo>() {
        @Override
        public DataGiphyPojo createFromParcel(Parcel in) {
            return new DataGiphyPojo(in);
        }

        @Override
        public DataGiphyPojo[] newArray(int size) {
            return new DataGiphyPojo[size];
        }
    };
}
