package com.igniva.youtubeplayer.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubePlayer;
import com.igniva.youtubeplayer.R;
import com.igniva.youtubeplayer.db.DatabaseHandler;
import com.igniva.youtubeplayer.model.DataYoutubePojo;
import com.igniva.youtubeplayer.ui.activities.YouTubePlayerActivity;
import com.igniva.youtubeplayer.ui.fragments.CategoriesFragment;
import com.igniva.youtubeplayer.utils.OrientationEnum;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import yt.sdk.access.InitializationException;
import yt.sdk.access.YTSDK;

/**
 * Created by igniva-php-08 on 20/5/16.
 */
public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {

    private List<DataYoutubePojo> dataYoutubePojos;

    YTSDK sdk = null;

    Context mContext;
    private DatabaseHandler db;
    static List<DataYoutubePojo> contacts;
    int mData;
    SharedPreferences sharedPreferences;

    public CategoryListAdapter(Context context, List<DataYoutubePojo> youtubePojos, int data) throws InitializationException {
        this.dataYoutubePojos = youtubePojos;
        this.mContext = context;
        this.mData = data;
        db = new DatabaseHandler(mContext);
//        if (mListCategories.size() != 0) {
        CategoriesFragment.mRvCategories.setVisibility(View.VISIBLE);
//        } else {
//            CategoriesFragment.mRvCategories.setVisibility(View.GONE);
//        }
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvCategoryName, mTvCategoryDuration;
        CardView mCvMain;
        ImageView mTvCategoryImg, mIvFavourite, mIvShareIcon, mIvDownloadIcon;
        RatingBar mRatingBar;

        public ViewHolder(View itemView) {
            super(itemView);

            mTvCategoryName = (TextView) itemView.findViewById(R.id.tv_category_name);
            mCvMain = (CardView) itemView.findViewById(R.id.cv_category_main);
            mTvCategoryImg = (ImageView) itemView.findViewById(R.id.iv_category_img);
            mTvCategoryDuration = (TextView) itemView.findViewById(R.id.tv_category_duration);
            mRatingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            mIvFavourite = (ImageView) itemView.findViewById(R.id.iv_star_favourite);
            mIvShareIcon = (ImageView) itemView.findViewById(R.id.iv_share_icon);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_videos, parent, false);
        ViewHolder vhItem = new ViewHolder(v);
        return vhItem;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {

            LayerDrawable stars = (LayerDrawable) holder.mRatingBar.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.parseColor("#D32F2F"), PorterDuff.Mode.SRC_ATOP);
            holder.mRatingBar.setRating(dataYoutubePojos.get(position).getVideo_rating());

            Picasso.with(mContext)
                    .load(dataYoutubePojos.get(position).getVideo_thumb())
                    .fit()
                    .into(holder.mTvCategoryImg);

            holder.mTvCategoryName.setText(dataYoutubePojos.get(position).getVideo_title());
            holder.mTvCategoryDuration.setText(dataYoutubePojos.get(position).getVideo_duration());


            if (dataYoutubePojos.get(position).getVideo_favourite().equals("0")) {
                holder.mIvFavourite.setImageResource(R.drawable.star_grey);
            } else {
                holder.mIvFavourite.setImageResource(R.drawable.star_golden);
            }

            holder.mIvShareIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String link = "https://www.youtube.com/watch?v=" + dataYoutubePojos.get(position).getVideo_channel();
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml("" + link));
                    mContext.startActivity(Intent.createChooser(sharingIntent, "Share using"));

                }
            });

            holder.mIvFavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dataYoutubePojos.get(position).getVideo_favourite().equals("0")) {
                        holder.mIvFavourite.setImageResource(R.drawable.star_golden);
                        dataYoutubePojos.get(position).setVideo_favourite("1");
                        db.updateContact(dataYoutubePojos.get(position));
                        db.updateFavouriteInFirebase(dataYoutubePojos.get(position), "1");
                        updateAdapter();
                    } else {
                        holder.mIvFavourite.setImageResource(R.drawable.star_grey);
                        dataYoutubePojos.get(position).setVideo_favourite("0");
                        db.updateContact(dataYoutubePojos.get(position));
                        db.updateFavouriteInFirebase(dataYoutubePojos.get(position), "0");
                        updateAdapter();
                    }
                }
            });


            holder.mCvMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, YouTubePlayerActivity.class);
                    intent.putExtra("VideoId", dataYoutubePojos.get(position).getVideo_id());
                    intent.putExtra("player_style", YouTubePlayer.PlayerStyle.DEFAULT);
                    intent.putExtra("orientation", OrientationEnum.AUTO);
                    intent.putExtra("show_audio_ui", true);
                    intent.putExtra("handle_error", true);
                    intent.putExtra("anim_enter", R.anim.fade_in);
                    intent.putExtra("anim_exit", R.anim.fade_out);
//                    intent.putExtra("index_no", dataYoutubePojos.get(position).getVideo_id());
                    intent.putExtra("index_no", position);

                    ArrayList<String> video_ids_list = new ArrayList<String>();
                    for (int i = 0; i < dataYoutubePojos.size(); i++) {
                        video_ids_list.add(dataYoutubePojos.get(i).getVideo_id());
                    }

                    intent.putStringArrayListExtra("mListCategories", video_ids_list);
//                    Toast.makeText(mContext, "mListCategories= "+mListCategories.indexOf(mListCategories.get(position)), Toast.LENGTH_SHORT).show();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception", "" + e);

        }
    }


    private void clear() {
        dataYoutubePojos.clear();
    }

    @Override
    public int getItemCount() {//return array.size
        return dataYoutubePojos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void updateAdapter() {
        clear();
        final DatabaseHandler db = new DatabaseHandler(mContext);
        contacts = db.getAllContacts();

        for (DataYoutubePojo cn : contacts) {
            String log = "video_Id: " + cn.getVideo_no() + " , Video_Title: " + cn.getVideo_title() + " Video_id" + cn.getVideo_id() + "Video_channel" + cn.getVideo_channel() +
                    " ,Duration: " + cn.getVideo_duration() + " Rating: " + cn.getVideo_rating() + " Thumb: " + cn.getVideo_thumb() + " Playlist: " + cn.getVideo_playlist() +
                    " order: " + cn.getVideo_order() + " Favourite= " + cn.getVideo_favourite();

            dataYoutubePojos.add(cn);

            // Writing Contacts to log
            Log.e("Name: ", log);
        }
    }
}

