package com.igniva.youtubeplayer.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.igniva.youtubeplayer.R;
import com.igniva.youtubeplayer.db.DatabaseHandler;
import com.igniva.youtubeplayer.libs.FloatingActionButton;
import com.igniva.youtubeplayer.libs.FloatingActionMenu;
import com.igniva.youtubeplayer.model.DataGalleryPojo;
import com.igniva.youtubeplayer.ui.activities.MainActivity;
import com.igniva.youtubeplayer.ui.adapters.CategoryListAdapterGallery;
import com.igniva.youtubeplayer.ui.application.MyApplication;
import com.igniva.youtubeplayer.utils.UtilsUI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by igniva-php-08 on 18/5/16.
 */
public class GallaryFragment extends BaseFragment implements FloatingActionMenu.OnMenuToggleListener {
    View mView;
    public static RecyclerView mRvCategories;
    private ArrayList<String> large_images_url = new ArrayList<>();
    public static RelativeLayout no_data_found_layout;


    public static ArrayList<String> listCategories, listDuration, listNames, listRating, listFavourite;

    public static FloatingActionMenu menu_fab;
    FloatingActionButton fab1, fab2, fab3;
    MainActivity main = new MainActivity();
    List<DataGalleryPojo> mAllImages = main.getMyImages();
    private DatabaseHandler db;

    private ChildEventListener imageChildEventListener;
    private DatabaseReference mDatabaseImages;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CategoryListAdapterGallery adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_category, container, false);

        MainActivity.menu_fab.setVisibility(View.GONE);

        listCategories = new ArrayList<String>();
        listDuration = new ArrayList<String>();
        listNames = new ArrayList<String>();
        listRating = new ArrayList<String>();
        listFavourite = new ArrayList<String>();

        // Reading all contacts

        no_data_found_layout = (RelativeLayout) mView.findViewById(R.id.no_data_found_layout);

        db = new DatabaseHandler(getActivity());

        fetchGallaryData();

        initSwipeRefreshLayout();

        initFireBaseListener();

        mDatabaseImages = FirebaseDatabase.getInstance().getReference("images");
        mDatabaseImages.addChildEventListener(imageChildEventListener);

        return mView;
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchGallaryData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initFireBaseListener() {
        imageChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DataGalleryPojo post = dataSnapshot.getValue(DataGalleryPojo.class);
                db.addUpdateImageData(post, dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                DataGalleryPojo post = dataSnapshot.getValue(DataGalleryPojo.class);
                db.addUpdateImageData(post, dataSnapshot.getKey());
                post.setImage_fire_base_id(dataSnapshot.getKey());
                for (int i = 0; i < mAllImages.size(); i++) {
                    if (post.getImage_no().equals(mAllImages.get(i).getImage_no())) {
                        mAllImages.set(i, post);
                        break;
                    }
                }
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    @Override
    public void setUpLayouts() {

    }

    @Override
    public void setDataInViewLayouts() {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        return false;
    }


    public void fetchGallaryData() {
        Log.d("Reading: ", "Reading all contacts..");

        UtilsUI.favourite_status = false;

        large_images_url.clear();


        UtilsUI.galery_status = true;


        MainActivity mainActivityObject = (MainActivity) getActivity();

        mAllImages = db.getAllImages();
//        mAllImages = mainActivityObject.getMyImages();

        for (DataGalleryPojo cn : mAllImages) {
//                            String log = "video_Id: " + cn.getVideo_no() + " , Video_Title: " + cn.getVideo_title() + " Video_id" + cn.getVideo_id() + "Video_channel" + cn.getVideo_channel() +
//                                    " ,Duration: " + cn.getVideo_duration() + " Rating: " + cn.getVideo_rating() + " Thumb: " + cn.getVideo_thumb() + " Playlist: " + cn.getVideo_playlist() +
//                                    " order: " + cn.getVideo_order() + " Favourite= " + cn.getVideo_favourite();

            large_images_url.add(cn.getImage_link());

        }


        mRvCategories = (RecyclerView) mView.findViewById(R.id.rv_categories);

        if (adapter == null) {
            adapter = new CategoryListAdapterGallery(getActivity(), large_images_url, 1);
            mRvCategories.setAdapter(adapter);
            mRvCategories.setHasFixedSize(true);
            GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
            mRvCategories.setLayoutManager(mLayoutManager);
        } else {
            adapter.notifyDataSetChanged();
        }


    }

    @Override
    public void onMenuToggle(boolean opened) {

        if (opened) {

            menu_fab.getMenuIconView().setImageDrawable(getResources().getDrawable(R.drawable.ic_clear_white_24dp));

        } else {

            menu_fab.getMenuIconView().setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_filter));

        }

    }

    @Override
    public void onResume() {

        super.onResume();
        Tracker tracker = MyApplication.getInstance().getGoogleAnalyticsTracker();
        tracker.setScreenName("Gallary");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

}