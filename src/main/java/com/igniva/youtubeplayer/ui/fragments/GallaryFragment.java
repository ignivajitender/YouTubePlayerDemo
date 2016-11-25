package com.igniva.youtubeplayer.ui.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.igniva.youtubeplayer.R;
import com.igniva.youtubeplayer.db.DatabaseHandler;
import com.igniva.youtubeplayer.libs.FloatingActionButton;
import com.igniva.youtubeplayer.libs.FloatingActionMenu;
import com.igniva.youtubeplayer.model.DataGalleryPojo;
import com.igniva.youtubeplayer.model.DataYoutubePojo;
import com.igniva.youtubeplayer.ui.activities.MainActivity;
import com.igniva.youtubeplayer.ui.adapters.CategoryListAdapter;
import com.igniva.youtubeplayer.ui.adapters.CategoryListAdapterGallery;
import com.igniva.youtubeplayer.ui.application.MyApplication;
import com.igniva.youtubeplayer.utils.UtilsUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by igniva-php-08 on 18/5/16.
 */
public class GallaryFragment extends BaseFragment implements FloatingActionMenu.OnMenuToggleListener {
    View mView;
    public static RecyclerView mRvCategories;
    ArrayList<String> small_images_url, medium_images_url, large_images_url;
    public static RelativeLayout no_data_found_layout;



    public static ArrayList<String>  listCategories, listDuration, listNames, listRating, listFavourite;

    public static FloatingActionMenu menu_fab;
    FloatingActionButton fab1, fab2, fab3;
    MainActivity main = new MainActivity();
    List<DataGalleryPojo> mAllImages = main.getMyImages();



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_category, container, false);

        MainActivity.menu_fab.setVisibility(View.GONE);

        small_images_url = new ArrayList<>();
        medium_images_url = new ArrayList<>();
        large_images_url = new ArrayList<>();

        listCategories = new ArrayList<String>();
        listDuration = new ArrayList<String>();
        listNames = new ArrayList<String>();
        listRating = new ArrayList<String>();
        listFavourite = new ArrayList<String>();

        // Reading all contacts

        no_data_found_layout = (RelativeLayout)mView.findViewById(R.id.no_data_found_layout);

        final DatabaseHandler db = new DatabaseHandler(getActivity());

        fetchLatestVideos();

        return mView;
    }

    @Override
    public void setUpLayouts() {

        mRvCategories = (RecyclerView) mView.findViewById(R.id.rv_categories);
        try {
            GallaryFragment.mRvCategories.setVisibility(View.VISIBLE);
            mRvCategories.setAdapter(new CategoryListAdapter(getActivity(), listCategories, listNames, listDuration, listRating, listFavourite, 1));
            mRvCategories.setHasFixedSize(true);
            GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
            mRvCategories.setLayoutManager(mLayoutManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setDataInViewLayouts() {
    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        return false;
}


    public void fetchLatestVideos(){
        Log.d("Reading: ", "Reading all contacts..");

        UtilsUI.favourite_status = false;

        small_images_url.clear();
        medium_images_url.clear();
        large_images_url.clear();


        UtilsUI.galery_status = true;



        MainActivity mainActivityObject =(MainActivity)getActivity();

        mAllImages = mainActivityObject.getMyImages();

        for (DataGalleryPojo cn : mAllImages) {
//                            String log = "video_Id: " + cn.getVideo_no() + " , Video_Title: " + cn.getVideo_title() + " Video_id" + cn.getVideo_id() + "Video_channel" + cn.getVideo_channel() +
//                                    " ,Duration: " + cn.getVideo_duration() + " Rating: " + cn.getVideo_rating() + " Thumb: " + cn.getVideo_thumb() + " Playlist: " + cn.getVideo_playlist() +
//                                    " order: " + cn.getVideo_order() + " Favourite= " + cn.getVideo_favourite();

            large_images_url.add(cn.getImage_link().toString());

        }


        mRvCategories = (RecyclerView) mView.findViewById(R.id.rv_categories);
        mRvCategories.setAdapter(new CategoryListAdapterGallery(getActivity(), large_images_url, 1));
        mRvCategories.setHasFixedSize(true);
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        mRvCategories.setLayoutManager(mLayoutManager);


    }

    @Override
    public void onMenuToggle(boolean opened) {

        if(opened){

            menu_fab.getMenuIconView().setImageDrawable(getResources().getDrawable(R.drawable.ic_clear_white_24dp));

        }else {

            menu_fab.getMenuIconView().setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_filter));

        }

    }
    @Override
    public void onResume(){

        super.onResume();
        Tracker tracker = MyApplication.getInstance().getGoogleAnalyticsTracker();
        tracker.setScreenName("Gallary");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }


}