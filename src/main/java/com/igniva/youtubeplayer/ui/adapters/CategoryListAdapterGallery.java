package com.igniva.youtubeplayer.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.igniva.youtubeplayer.R;
import com.igniva.youtubeplayer.ui.activities.All_Image_View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by igniva-php-08 on 20/5/16.
 */

public class CategoryListAdapterGallery  extends RecyclerView.Adapter<CategoryListAdapterGallery.ViewHolder> implements Parcelable
{

    List<String> mImageUrl=new ArrayList<>();
    List<String> mImageName;
    Context mContext;
    public static  ArrayList<Bitmap> bitmap=new ArrayList<>();
    SQLiteDatabase db;
    int i;
public static   Bitmap bitmap2;
    public CategoryListAdapterGallery(Context context, List<String> listCategories,int i) {
        this.mImageUrl.clear();
        this.mImageUrl=listCategories;
//            this.mImageName=listCategories;
        this.i=i;
        this.mContext = context;
//         db = SQLiteDatabase.openOrCreateDatabase("YouTubeDB", null);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mTvCategoryImg;

        public ViewHolder(View itemView) {
            super(itemView);

            mTvCategoryImg=(ImageView)itemView.findViewById(R.id.iv_adapter_image);

            if(i==1)
            {
                // Gets linearlayout
                CardView layout = (CardView)itemView.findViewById(R.id.cv_category_main);
// Gets the layout params that will allow you to resize the layout
                ViewGroup.LayoutParams params = layout.getLayoutParams();
// Changes the height and width to the specified *pixels*
                params.height = 700;
                params.width = CardView.LayoutParams.MATCH_PARENT;
                layout.setLayoutParams(params);
            }
            else
            {
                // Gets linearlayout
                CardView layout = (CardView)itemView.findViewById(R.id.cv_category_main);
// Gets the layout params that will allow you to resize the layout
                ViewGroup.LayoutParams params = layout.getLayoutParams();
// Changes the height and width to the specified *pixels*
                params.height = 220;
                params.width = CardView.LayoutParams.MATCH_PARENT;
                layout.setLayoutParams(params);

            }
        }
    }


    protected CategoryListAdapterGallery(Parcel in) {
        mImageUrl = in.createStringArrayList();
        mImageName = in.createStringArrayList();
        i = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(mImageUrl);
        dest.writeStringList(mImageName);
        dest.writeInt(i);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CategoryListAdapterGallery> CREATOR = new Creator<CategoryListAdapterGallery>() {
        @Override
        public CategoryListAdapterGallery createFromParcel(Parcel in) {
            return new CategoryListAdapterGallery(in);
        }

        @Override
        public CategoryListAdapterGallery[] newArray(int size) {
            return new CategoryListAdapterGallery[size];
        }
    };

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_image, parent, false);
        ViewHolder vhItem = new ViewHolder(v);
        return vhItem;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {

            Glide
                    .with(mContext)

                    .load( mImageUrl.get(position) )
//                    .bitmapTransform( new jp.wasabeef.glide.transformations.BlurTransformation( mContext, 10 ) )
                    .placeholder(R.drawable.loading).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .thumbnail(.3f)
                    .into( holder.mTvCategoryImg );


            holder.mTvCategoryImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Glide.with(mContext)
                            .load(mImageUrl.get(position))
                            .asBitmap()
                            .placeholder(R.drawable.placeholder).diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(new SimpleTarget<Bitmap>() {

                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                                    bitmap2=resource;

                                }
                            });

                    Intent intent = new Intent(mContext,All_Image_View.class);
                    intent.putExtra("array", (Serializable) mImageUrl);
                    intent.putExtra("position",position);


                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);


                }
            });


        }catch (Exception e){
            e.printStackTrace();
            Log.e("Exception",""+e);

        }
    }

    @Override
    public int getItemCount() {//return array.size
        return mImageUrl.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}



















//public class CategoryListAdapterGallery  extends RecyclerView.Adapter<CategoryListAdapterGallery.ViewHolder> {
//
//    List<String> mImageUrl;
//    List<String> mImageName;
//    Context mContext;
//    SQLiteDatabase db;
//    int i;
//
//    public CategoryListAdapterGallery(Context context, List<String> listCategories,int i) {
//        this.mImageUrl=listCategories;
//      this.i=i;
//
//        this.mContext = context;
//
// }
//
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView mTvCategoryImg;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//
//            mTvCategoryImg=(ImageView)itemView.findViewById(R.id.iv_adapter_image);
//
//
//            if(i==1)
//            {
//                // Gets linearlayout
//                CardView layout = (CardView)itemView.findViewById(R.id.cv_category_main);
//// Gets the layout params that will allow you to resize the layout
//                ViewGroup.LayoutParams params = layout.getLayoutParams();
//// Changes the height and width to the specified *pixels*
//                params.height = 700;
//                params.width = CardView.LayoutParams.MATCH_PARENT;
//                layout.setLayoutParams(params);
//            }
//            else
//            {
//                // Gets linearlayout
//                CardView layout = (CardView)itemView.findViewById(R.id.cv_category_main);
//// Gets the layout params that will allow you to resize the layout
//                ViewGroup.LayoutParams params = layout.getLayoutParams();
//// Changes the height and width to the specified *pixels*
//                params.height = 220;
//                params.width = CardView.LayoutParams.MATCH_PARENT;
//                layout.setLayoutParams(params);
//
//            }
//
//        }
//    }
//
//
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_image, parent, false);
//        ViewHolder vhItem = new ViewHolder(v);
//        return vhItem;
//    }
//
//    @Override
//    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        try {
//
//            Glide.with(mContext).load(mImageUrl.get(position))
//                    .thumbnail(0.3f)
//                    .crossFade()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(holder.mTvCategoryImg);
//
//
//
//            holder.mTvCategoryImg.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    try {
//                        Intent intent = new Intent(mContext, All_Image_View.class);
//                        intent.putExtra("array", (Serializable) mImageUrl);
//                        intent.putExtra("position", position);
//                        mContext.startActivity(intent);
//                    }
//                    catch(Exception e)
//                    {
//
//                    }
//
//                }
//            });
//
//
//
//        }catch (Exception e){
//            e.printStackTrace();
//            Log.e("Exception",""+e);
//
//        }
//    }
//
//    @Override
//    public int getItemCount() {//return array.size
//        return mImageUrl.size();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return position;
//    }
//
//
//}



