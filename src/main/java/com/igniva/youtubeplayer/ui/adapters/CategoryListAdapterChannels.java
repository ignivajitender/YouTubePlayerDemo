package com.igniva.youtubeplayer.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.igniva.youtubeplayer.R;
import com.igniva.youtubeplayer.db.DatabaseHandler;
import com.igniva.youtubeplayer.model.DataYoutubePojo;
import com.igniva.youtubeplayer.ui.activities.ChannelActivity;
import com.igniva.youtubeplayer.ui.activities.MainActivity;
import com.igniva.youtubeplayer.ui.fragments.CategoriesFragment;
import com.igniva.youtubeplayer.utils.QualityEnum;
import com.igniva.youtubeplayer.utils.YouTubeThumbnail;
import com.squareup.picasso.Picasso;

/**
 * Created by igniva-php-08 on 20/5/16.
 */
public class CategoryListAdapterChannels  extends RecyclerView.Adapter<CategoryListAdapterChannels.ViewHolder> {

    public static ArrayList<String> listCategories,listDuration,listNames,listRating,listFavourite;
    static List<DataYoutubePojo> contacts;

    List<String> mImageUrl=new ArrayList<>();
    List<String> mImageName=new ArrayList<>();;
    Context mContext;
    SQLiteDatabase db;
    static List<DataYoutubePojo> mAllData;


    public CategoryListAdapterChannels(Context context, List<String> listCategories,List<String> listCategories_icons) {
        clear_channels();
        this.mImageUrl=listCategories_icons;
            this.mImageName=listCategories;



        CategoriesFragment.mRvCategories.setVisibility(View.VISIBLE);
        this.mContext = context;
    }

    private void clear_channels() {

        mImageUrl.clear();
        mImageName.clear();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mTvCategoryImg;
        TextView mTvCategoryText;
        LinearLayout ll_adapter_channels;

        public ViewHolder(View itemView) {
            super(itemView);


            listCategories = new ArrayList<String>();
            listDuration = new ArrayList<String>();
            listNames = new ArrayList<String>();
            listRating = new ArrayList<String>();
            listFavourite=new ArrayList<String>();

            MainActivity m=new MainActivity();
            mAllData=m.getMyData();

            mTvCategoryImg=(ImageView)itemView.findViewById(R.id.iv_adapter_image);
            mTvCategoryText=(TextView)itemView.findViewById(R.id.iv_adapter_text);
            CategoriesFragment.mRvCategories.setVisibility(View.VISIBLE);
            ll_adapter_channels=(LinearLayout)itemView.findViewById(R.id.ll_adapter_channels);
        }
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_text_channels, parent, false);
        ViewHolder vhItem = new ViewHolder(v);
        return vhItem;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {

            holder.mTvCategoryText.setText(mImageName.get(position));

            Picasso.with(mContext)
                    .load(mImageUrl.get(position))
                    .fit()
                    .into(holder.mTvCategoryImg);

            final DatabaseHandler db = new DatabaseHandler(mContext);

            holder.ll_adapter_channels.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    clear();

                    // Reading all contacts
                    Log.d("Reading: ", "Reading all contacts..");
//                    contacts = db.getAllContacts();

                    for (DataYoutubePojo cn : mAllData) {
                        String log = "video_Id: " + cn.getVideo_no() + " , Video_Title: " + cn.getVideo_title()+" Video_id"+cn.getVideo_id()+"Video_channel"+cn.getVideo_channel() +
                                " ,Duration: " + cn.getVideo_duration()+" Rating: " + cn.getVideo_rating()+" Thumb: " + cn.getVideo_thumb()+" Playlist: " + cn.getVideo_playlist()+
                                " order: " + cn.getVideo_order()+" Favourite= "+cn.getVideo_favourite();

                        if(  mImageName.get(position).toString().equals("Coke Studio"))
                        {
                            if(cn.getVideo_channel().equals("Coke Studio")) {

                                listCategories.add(cn.getVideo_id().toString());
                                listNames.add(cn.getVideo_title().toString());
                                listDuration.add(cn.getVideo_duration().toString());
                                listRating.add(""+cn.getVideo_rating());
                                listFavourite.add(cn.getVideo_favourite());
                            }
                        }

                        else

                        if(  mImageName.get(position).toString().equals("Bollywood"))
                        {

                            if(cn.getVideo_channel().equals("Bollywood")) {


                                listCategories.add(cn.getVideo_id().toString());
                                listNames.add(cn.getVideo_title().toString());
                                listDuration.add(cn.getVideo_duration().toString());
                                listRating.add(""+cn.getVideo_rating());
                                listFavourite.add(cn.getVideo_favourite());
                            }
                        }

                        else
                        if(holder.mTvCategoryText.getText().toString().equals("English")) {
                            if (cn.getVideo_channel().equals("Hollywood")) {
                                listCategories.add(cn.getVideo_id().toString());
                                listNames.add(cn.getVideo_title().toString());
                                listDuration.add(cn.getVideo_duration().toString());
                                listRating.add(""+cn.getVideo_rating());
                                listFavourite.add(cn.getVideo_favourite());
                            }
                        }
                        else
                        if(holder.mTvCategoryText.getText().toString().equals("Punjabi")) {
                            if (cn.getVideo_channel().equals("Punjabi")) {
                                listCategories.add(cn.getVideo_id().toString());
                                listNames.add(cn.getVideo_title().toString());
                                listDuration.add(cn.getVideo_duration().toString());
                                listRating.add(""+cn.getVideo_rating());
                                listFavourite.add(cn.getVideo_favourite());
                            }
                        }

                        // Writing Contacts to log
                        Log.e("Name: ", log);

                    }

                    Intent intent=new Intent(mContext, ChannelActivity.class);
                    intent.putStringArrayListExtra("listCategories",listCategories);
                    intent.putStringArrayListExtra("listNames",listNames);
                    intent.putStringArrayListExtra("listDuration",listDuration);
                    intent.putStringArrayListExtra("listRating",listRating);
                    intent.putStringArrayListExtra("listFavourite",listFavourite);
                    intent.putExtra("toolbar_title",mImageName.get(position).toString());

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);

                }
            });



        }catch (Exception e){
            e.printStackTrace();
            Log.e("Exception",""+e);

        }
    }

    private static void clear() {



        listCategories.clear();
        listNames.clear();
        listDuration.clear();
        listRating.clear();
        listFavourite.clear();

        CategoriesFragment.listCategories.clear();
        CategoriesFragment.listNames.clear();
        CategoriesFragment.listDuration.clear();
        CategoriesFragment.listRating.clear();
        CategoriesFragment.listFavourite.clear();
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

