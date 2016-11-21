package com.igniva.youtubeplayer.model;

import java.io.Serializable;

/**
 * Created by igniva-andriod-03 on 31/8/16.
 */
public class DataYoutubePojo implements Serializable{

    //private variables
    String video_no;
    String video_id;
    String video_title;
    String video_channel;
    String video_duration;
    int video_rating;
    String video_thumb;
    String video_playlist;
    String video_order;
    String video_favourite;

    // Empty constructor
    public DataYoutubePojo(){

    }



    // constructor
    public DataYoutubePojo(String video_no,String video_title, String video_id , String video_channel,
                           String video_duration, int video_rating,String video_thumb,
                           String video_playlist,String video_order,String video_favourite){
        this.video_no = video_no;
        this.video_id = video_id;
        this.video_title = video_title;
        this.video_channel = video_channel;
        this.video_duration = video_duration;

        this.video_rating = video_rating;
        this.video_thumb = video_thumb;
        this.video_playlist = video_playlist;
        this.video_order = video_order;
        this.video_favourite = video_favourite;
    }

    // constructor
    public DataYoutubePojo(String video_id, String video_favourite){
        this.video_id = video_id;
        this.video_favourite = video_favourite;
    }

    public String getVideo_no() {
        return video_no;
    }

    public void setVideo_no(String video_no) {
        this.video_no = video_no;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getVideo_title() {
        return video_title;
    }

    public void setVideo_title(String video_title) {
        this.video_title = video_title;
    }

    public String getVideo_channel() {
        return video_channel;
    }

    public void setVideo_channel(String video_channel) {
        this.video_channel = video_channel;
    }

    public String getVideo_duration() {
        return video_duration;
    }

    public void setVideo_duration(String video_duration) {
        this.video_duration = video_duration;
    }

    public int getVideo_rating() {
        return video_rating;
    }

    public void setVideo_rating(int video_rating) {
        this.video_rating = video_rating;
    }

    public String getVideo_thumb() {
        return video_thumb;
    }

    public void setVideo_thumb(String video_thumb) {
        this.video_thumb = video_thumb;
    }

    public String getVideo_playlist() {
        return video_playlist;
    }

    public void setVideo_playlist(String video_playlist) {
        this.video_playlist = video_playlist;
    }

    public String getVideo_order() {
        return video_order;
    }

    public void setVideo_order(String video_order) {
        this.video_order = video_order;
    }

    public String getVideo_favourite() {
        return video_favourite;
    }

    public void setVideo_favourite(String video_favourite) {
        this.video_favourite = video_favourite;
    }
}