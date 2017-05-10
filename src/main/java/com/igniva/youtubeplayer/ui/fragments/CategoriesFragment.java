package com.igniva.youtubeplayer.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.igniva.youtubeplayer.R;
import com.igniva.youtubeplayer.db.DatabaseHandler;
import com.igniva.youtubeplayer.model.DataYoutubePojo;
import com.igniva.youtubeplayer.ui.activities.MainActivity;
import com.igniva.youtubeplayer.ui.adapters.CategoryListAdapter;
import com.igniva.youtubeplayer.ui.application.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by igniva-php-08 on 18/5/16.
 */
public class CategoriesFragment extends BaseFragment {
    View mView;
    public static RecyclerView mRvCategories;
    private List<DataYoutubePojo> mAllData = new ArrayList<>();
    public static RelativeLayout no_data_found_layout;
    private SwipeRefreshLayout swipeRefreshLayout;

    private DatabaseHandler mDatabaseHandler;

    private ChildEventListener videoChildEventListener;
    private DatabaseReference mDatabaseVideos;
    private CategoryListAdapter categoryListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_category, container, false);

        MainActivity.menu_fab.setVisibility(View.VISIBLE);

        // Reading all contacts

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        boolean a = isGooglePlayServicesAvailable(getActivity());
        no_data_found_layout = (RelativeLayout) mView.findViewById(R.id.no_data_found_layout);

        mDatabaseHandler = new DatabaseHandler(getActivity());

        fetchLatestVideos();

        initSwipeRefreshLayout();

        initFireBaseListener();

        mDatabaseVideos = FirebaseDatabase.getInstance().getReference("videos");
        mDatabaseVideos.addChildEventListener(videoChildEventListener);

        return mView;
    }

    public boolean isGooglePlayServicesAvailable(Context context){
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context);
        return resultCode == ConnectionResult.SUCCESS;
    }

    private void initFireBaseListener() {
        videoChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e("CF: onChildAdded: ", dataSnapshot.toString());
                DataYoutubePojo post = dataSnapshot.getValue(DataYoutubePojo.class);
                mDatabaseHandler.addUpdateVideoData(post, dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.e("CF: onChildChanged: ", dataSnapshot.toString());
                DataYoutubePojo post = dataSnapshot.getValue(DataYoutubePojo.class);
                mDatabaseHandler.addUpdateVideoData(post, dataSnapshot.getKey());
                post.setVideo_fire_base_id(dataSnapshot.getKey());
                for (int i = 0; i < mAllData.size(); i++) {
                    if (post.getVideo_no().equals(mAllData.get(i).getVideo_no())) {
                        mAllData.set(i, post);
                        break;
                    }
                }
                if (categoryListAdapter != null) {
                    categoryListAdapter.notifyDataSetChanged();
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

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchLatestVideos();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mDatabaseVideos.removeEventListener(videoChildEventListener);
    }

    @Override
    public void setUpLayouts() {

        if (categoryListAdapter == null) {
            mRvCategories = (RecyclerView) mView.findViewById(R.id.rv_categories);
            try {
                mRvCategories.setVisibility(View.VISIBLE);
                categoryListAdapter = new CategoryListAdapter(getActivity(), mAllData, 1);
                mRvCategories.setAdapter(categoryListAdapter);
                mRvCategories.setHasFixedSize(true);
                GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
                mRvCategories.setLayoutManager(mLayoutManager);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            categoryListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setDataInViewLayouts() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        return false;
    }


    public void fetchLatestVideos() {
        Log.d("Reading: ", "Reading all contacts..");
        mAllData.clear();
        mAllData.addAll(mDatabaseHandler.getAllContacts());
        setUpLayouts();
    }

    @Override
    public void onResume() {
        super.onResume();
        Tracker tracker = MyApplication.getInstance().getGoogleAnalyticsTracker();
        tracker.setScreenName("video Gallary");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }


}