package com.igniva.youtubeplayer.model;

import java.io.Serializable;

/**
 * Created by igniva-andriod-03 on 15/9/16.
 */
public class DataGalleryPojo implements Serializable {

    //private variables
    String image_no;

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public String getImage_no() {
        return image_no;
    }

    public void setImage_no(String image_no) {
        this.image_no = image_no;
    }

    String image_link;

    String thumb_image;

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }
}
