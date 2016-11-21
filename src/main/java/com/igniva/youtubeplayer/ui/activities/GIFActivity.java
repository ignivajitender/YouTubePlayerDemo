package com.igniva.youtubeplayer.ui.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.igniva.youtubeplayer.R;

public class GIFActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);


        ImageView imageView = (ImageView) findViewById(R.id.iv_imageView);
//        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
//        Glide.with(this).load("http://67.media.tumblr.com/tumblr_lig2qcCt3j1qgfevpo1_400.gif").into(imageViewTarget);

        final ProgressDialog prog =ProgressDialog.show(this,"","Please wait...");


        Glide.with(this)
                .load("http://67.media.tumblr.com/tumblr_lig2qcCt3j1qgfevpo1_400.gif")
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        progressBar.setVisibility(View.GONE);
                        prog.dismiss();
                        return false;
                    }
                })
                .into(imageView)
        ;

    }
}
