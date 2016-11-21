package com.igniva.youtubeplayer.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.youtube.player.YouTubePlayer;
import com.igniva.youtubeplayer.R;
import com.igniva.youtubeplayer.model.DataYoutubePojo;
import com.igniva.youtubeplayer.db.DatabaseHandler;
import com.igniva.youtubeplayer.ui.activities.MainActivity;
import com.igniva.youtubeplayer.ui.fragments.CategoriesFragment;
import com.igniva.youtubeplayer.utils.OrientationEnum;
import com.igniva.youtubeplayer.utils.QualityEnum;
import com.igniva.youtubeplayer.ui.activities.YouTubePlayerActivity;
import com.igniva.youtubeplayer.utils.UtilsUI;
import com.igniva.youtubeplayer.utils.YouTubeThumbnail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import yt.sdk.access.InitializationException;
import yt.sdk.access.YTSDK;

/**
 * Created by igniva-php-08 on 20/5/16.
 */
public class CategoryListAdapter  extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {

public  static ArrayList<String> mListCategories=new ArrayList<>();;
    ArrayList<String> mListNames=new ArrayList<>();;
    ArrayList<String> mListDuration=new ArrayList<>();;
    ArrayList<String> mListRating=new ArrayList<>();;
    ArrayList<String> mListFavourite=new ArrayList<>();;
    ;

    YTSDK sdk = null;

    Context mContext;
    Activity activity;
    SQLiteDatabase db;
    static List<DataYoutubePojo> contacts;
    int mData ;
    SharedPreferences sharedPreferences;

    public CategoryListAdapter(Context context, ArrayList<String> listCategories,ArrayList<String> listNames,ArrayList<String> listDuration,ArrayList<String> listRating,ArrayList<String> listFavourite ,int data) throws InitializationException {
        this.mListCategories=listCategories;
        this.mListNames=listNames;
        this.mListDuration=listDuration;
        this.mListRating=listRating;
        this.mListFavourite=listFavourite;
        this.mContext = context;

        this.mData = data;
//        this.activity=(Activity)context;

//        try {
//            sdk = YTSDK.getInstance(activity);
//        }catch (Exception e)
//            {System.out.println("hlo");}
        if(mListCategories.size()!=0)
        {
            CategoriesFragment.mRvCategories.setVisibility(View.VISIBLE);
        }
        else
        {
            CategoriesFragment.mRvCategories.setVisibility(View.GONE);
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

//         db = SQLiteDatabase.openOrCreateDatabase("YouTubeDB", null);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvCategoryName,mTvCategoryDuration;
        CardView mCvMain;
        ImageView mTvCategoryImg,mIvFavourite,mIvShareIcon,mIvDownloadIcon;
        RatingBar mRatingBar;

        public ViewHolder(View itemView) {
            super(itemView);

            mTvCategoryName=(TextView)itemView.findViewById(R.id.tv_category_name);
            mCvMain=(CardView)itemView.findViewById(R.id.cv_category_main);
            mTvCategoryImg=(ImageView)itemView.findViewById(R.id.iv_category_img);
            mTvCategoryDuration=(TextView)itemView.findViewById(R.id.tv_category_duration);
            mRatingBar = (RatingBar)itemView.findViewById(R.id.ratingBar);
            mIvFavourite=(ImageView)itemView.findViewById(R.id.iv_star_favourite);
            mIvShareIcon=(ImageView)itemView.findViewById(R.id.iv_share_icon);



            if(mData ==2)
            {
                // Gets linearlayout
                RelativeLayout layout = (RelativeLayout)itemView.findViewById(R.id.iv_image_layout);
// Gets the layout params that will allow you to resize the layout
                ViewGroup.LayoutParams params = layout.getLayoutParams();
// Changes the height and width to the specified *pixels*
                params.height = (int) mContext.getResources().getInteger(R.integer.grid_row_height);
                params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                layout.setLayoutParams(params);

            }
            else
            {
                // Gets linearlayout
                RelativeLayout layout = (RelativeLayout)itemView.findViewById(R.id.iv_image_layout);
// Gets the layout params that will allow you to resize the layout
                ViewGroup.LayoutParams params = layout.getLayoutParams();
// Changes the height and width to the specified *pixels*
                params.height = (int) mContext.getResources().getInteger(R.integer.list_row_height);

                params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                layout.setLayoutParams(params);

                mTvCategoryName.setTextSize(20);
                mTvCategoryDuration.setTextSize(20);

            }
        }
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_category, parent, false);
        ViewHolder vhItem = new ViewHolder(v);
        return vhItem;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        try {

            final DatabaseHandler db = new DatabaseHandler(mContext);

            LayerDrawable stars = (LayerDrawable) holder.mRatingBar.getProgressDrawable();

            stars.getDrawable(2).setColorFilter(Color.parseColor("#D32F2F"), PorterDuff.Mode.SRC_ATOP);

            holder.mRatingBar.setRating(Float.parseFloat(mListRating.get(position).toString()));

            Picasso.with(mContext)
                    .load(YouTubeThumbnail.getUrlFromVideoId(mListCategories.get(position), QualityEnum.HIGH))
                    .fit()
                    .into(holder.mTvCategoryImg);

            holder.mTvCategoryName.setText(mListNames.get(position));

            holder.mTvCategoryDuration.setText(mListDuration.get(position));

            if(mListFavourite.get(position).equals("0"))
            {
                holder.mIvFavourite.setImageResource(R.drawable.star_grey);
            }
            else
            {
                holder.mIvFavourite.setImageResource(R.drawable.star_golden);
            }


            holder.mIvShareIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String link="https://www.youtube.com/watch?v="+mListCategories.get(position);

                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(""+link));
                    mContext.startActivity(Intent.createChooser(sharingIntent,"Share using"));

                }
            });

            holder.mIvFavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (mListFavourite.get(position).equals("0")) {
                        holder.mIvFavourite.setImageResource(R.drawable.star_golden);
                //                        Toast.makeText(mContext, "clicklistener", Toast.LENGTH_SHORT).show();
                        db.updateContact(new DataYoutubePojo(mListCategories.get(position), "1"));

                        mListFavourite.set(position, "1");

                        updateAdapter();
                    } else {
                      holder.mIvFavourite.setImageResource(R.drawable.star_grey);
                        db.updateContact(new DataYoutubePojo(mListCategories.get(position), "0"));
                        mListFavourite.set(position, "0 ");
                        updateAdapter();
                        if(UtilsUI.favourite_status) {
                                    clear();
                       Log.d("Reading: ", "Reading all contacts..");
                            contacts = db.getAllContacts();

                            for (DataYoutubePojo cn : contacts) {
                                String log = "video_Id: " + cn.getVideo_no() + " , Video_Title: " + cn.getVideo_title() + " Video_id" + cn.getVideo_id() + "Video_channel" + cn.getVideo_channel() +
                                        " ,Duration: " + cn.getVideo_duration() + " Rating: " + cn.getVideo_rating() + " Thumb: " + cn.getVideo_thumb() + " Playlist: " + cn.getVideo_playlist() +
                                        " order: " + cn.getVideo_order() + " Favourite= " + cn.getVideo_favourite();

                                if (cn.getVideo_favourite().equals("1")) {
                                    mListCategories.add(cn.getVideo_id().toString());
                                    mListNames.add(cn.getVideo_title().toString());
                                    mListDuration.add(cn.getVideo_duration().toString());
                                    mListRating.add(""+cn.getVideo_rating());
                                    mListFavourite.add(cn.getVideo_favourite());
                                }

                                Log.e("Name: ", log);

                            }



                            CategoriesFragment.listCategories = mListCategories;
                            CategoriesFragment.listNames = mListNames;
                            CategoriesFragment.listDuration = mListDuration;
                            CategoriesFragment.listRating = mListRating;
                            CategoriesFragment.listFavourite = mListFavourite;
                            try {



                                int no = sharedPreferences.getInt("cat", 2);
                                CategoriesFragment.mRvCategories.setAdapter(new CategoryListAdapter(mContext, mListCategories, mListNames, mListDuration, mListRating, mListFavourite,no));


                            } catch (InitializationException e) {
                                e.printStackTrace();
                            }

                            MainActivity.toolbar.setTitle("Favourite");

                        }

                }

               }
            });


            holder.mCvMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(mContext, YouTubePlayerActivity.class);
                    intent.putExtra("VideoId", mListCategories.get(position));
                    intent.putExtra("player_style", YouTubePlayer.PlayerStyle.DEFAULT);
                    intent.putExtra("orientation", OrientationEnum.AUTO);
                    intent.putExtra("show_audio_ui", true);
                    intent.putExtra("handle_error", true);
                    intent.putExtra("anim_enter", R.anim.fade_in);
                    intent.putExtra("anim_exit", R.anim.fade_out);
                    intent.putExtra("index_no",mListCategories.indexOf(mListCategories.get(position)) );
                    intent.putStringArrayListExtra("mListCategories",mListCategories);
//                    Toast.makeText(mContext, "mListCategories= "+mListCategories.indexOf(mListCategories.get(position)), Toast.LENGTH_SHORT).show();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Log.e("Exception",""+e);

        }
    }

    private void downloadVideo(int position)  throws InitializationException {




//        YTSDK.DownloadFormat.downloadmp4;
//        sdk.setDownloadFolderPath(String destinationFolder)
//        sdk.download(activity , mListCategories.get(position));

    }

    private void clear() {

        mListCategories.clear();
        mListNames.clear();
        mListDuration.clear();
        mListRating.clear();
        mListFavourite.clear();
    }

    @Override
    public int getItemCount() {//return array.size
        return mListCategories.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void updateAdapter(){
        final DatabaseHandler db = new DatabaseHandler(mContext);
        contacts = db.getAllContacts();

        clear();
        for (DataYoutubePojo cn : contacts) {
            String log = "video_Id: " + cn.getVideo_no() + " , Video_Title: " + cn.getVideo_title() + " Video_id" + cn.getVideo_id() + "Video_channel" + cn.getVideo_channel() +
                    " ,Duration: " + cn.getVideo_duration() + " Rating: " + cn.getVideo_rating() + " Thumb: " + cn.getVideo_thumb() + " Playlist: " + cn.getVideo_playlist() +
                    " order: " + cn.getVideo_order() + " Favourite= " + cn.getVideo_favourite();

            mListCategories.add(cn.getVideo_id().toString());
            mListNames.add(cn.getVideo_title().toString());
            mListDuration.add(cn.getVideo_duration().toString());
            mListRating.add(""+cn.getVideo_rating());
            mListFavourite.add(cn.getVideo_favourite());


            // Writing Contacts to log
            Log.e("Name: ", log);

        }


    //    notifyDataSetChanged();


    }

}

