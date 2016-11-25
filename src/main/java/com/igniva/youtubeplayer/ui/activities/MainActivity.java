package com.igniva.youtubeplayer.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.igniva.youtubeplayer.db.DatabaseHandler;
import com.igniva.youtubeplayer.libs.FloatingActionButton;
import com.igniva.youtubeplayer.libs.FloatingActionMenu;
import com.igniva.youtubeplayer.model.DataGalleryPojo;
import com.igniva.youtubeplayer.model.DataYoutubePojo;
import com.igniva.youtubeplayer.ui.adapters.CategoryListAdapter;
import com.igniva.youtubeplayer.ui.application.MyApplication;
import com.igniva.youtubeplayer.ui.fragments.CategoriesFragment;

import com.igniva.youtubeplayer.ui.fragments.FavouritesFragment;
import com.igniva.youtubeplayer.ui.fragments.TopRatedCategoryFragment;
import com.igniva.youtubeplayer.utils.Constants;
import com.igniva.youtubeplayer.utils.UtilsUI;
import com.mikepenz.materialdrawer.Drawer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.igniva.youtubeplayer.R;


import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,FloatingActionMenu.OnMenuToggleListener {
    static FragmentManager fragmentManager;
    private Context context;
    private Drawer drawer;
    public static Toolbar toolbar;
    private Boolean doubleBackToExitPressedOnce = false;
   public static List<DataYoutubePojo> mAllData;
   public static List<DataGalleryPojo> mAllImages;
    DatabaseHandler mDatabaseHandler;
    public static String TRACK_LOG= "track log event";
    public static String BUTTON_CLICK_EVENT= "button clicked";
    public static ArrayList<String> listCategories, listDuration, listNames, listRating, listFavourite;

    InterstitialAd mInterstitialAd;

    public String device_id;

    private AdView mAdView;


    FloatingActionButton fab1,fab2,fab3;
    public static FloatingActionMenu menu_fab;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        device_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        mDatabaseHandler = new DatabaseHandler(MainActivity.this);

        fragmentManager = getSupportFragmentManager();

        if (getIntent().hasExtra(Constants.ALL_DATA)) {
            mAllData = (List<DataYoutubePojo>) getIntent().getSerializableExtra(Constants.ALL_DATA);

        }

        if (getIntent().hasExtra(Constants.ALL_IMAGES)) {
            mAllImages = (List<DataGalleryPojo>) getIntent().getSerializableExtra(Constants.ALL_IMAGES);
        }
        initToolBar();

        try {

            drawer = UtilsUI.setNavigationDrawer(MainActivity.this, MainActivity.this, toolbar);

        }catch (Exception e){

            e.printStackTrace();

        }

        drawer.getDrawerLayout();
        //
        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        MenuItem item = navigationView.getMenu().getItem(0);
        onNavigationItemSelected(item);


       replaceFragment(new CategoriesFragment());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        menu_fab = (FloatingActionMenu) findViewById(R.id.menu_red);

        menu_fab.setAnimated(false);

        menu_fab.setOnMenuToggleListener(MainActivity.this);

        listCategories = new ArrayList<String>();
        listDuration = new ArrayList<String>();
        listNames = new ArrayList<String>();
        listRating = new ArrayList<String>();
        listFavourite = new ArrayList<String>();

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    MyApplication.getInstance().trackEvent("Dashboard",BUTTON_CLICK_EVENT,"Latest Video Clicked");

                    fetchLatestVideos();

                    menu_fab.close(true);

                    MainActivity.toolbar.setTitle("Latest Videos");

                }catch (Exception e){
                    e.printStackTrace();
                    MyApplication.getInstance().trackEvent(TRACK_LOG,"Latest button pressed",e.getMessage());

                }

            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                menu_fab.close(true);

                fetchTopRatedVideos();

                MainActivity.toolbar.setTitle("Top Rated");
            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UtilsUI.favourite_status = true;

                menu_fab.close(true);

                MainActivity.replaceFragment(new FavouritesFragment());

                MainActivity.toolbar.setTitle("Favourite");
            }
        });


        showBannerAdd();

        initializeCrashLytics();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void showBannerAdd() {
        try {

            mAdView = (AdView) findViewById(R.id.adView);

            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            mAdView.loadAd(adRequest);

        }catch (Exception e){

            e.printStackTrace();

            MyApplication.getInstance().trackEvent(TRACK_LOG,"banner add",e.getMessage());

        }

    }

    private void initializeCrashLytics() {

        try {

            Fabric.with(this, new Crashlytics());

        }catch (Exception e){

            e.printStackTrace();

            MyApplication.getInstance().trackEvent(TRACK_LOG,"crash",e.getMessage());


        }

    }

    public List<DataYoutubePojo> getMyData() {
        return mAllData;
    }

    public List<DataGalleryPojo> getMyImages() {
        return mAllImages;
    }

    void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Latest Videos");
        setSupportActionBar(toolbar);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {
            if (doubleBackToExitPressedOnce) {

                showAdd();
              //  super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, R.string.tap_exit, Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    private void showAdd() {

        try {


            mInterstitialAd = new InterstitialAd(this);

            // set the ad unit ID
            mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

            AdRequest adRequest = new AdRequest.Builder()
                    .build();

            // Load ads into Interstitial Ads
            mInterstitialAd.loadAd(adRequest);

            mInterstitialAd.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    showInterstitial();
                }


            });

        }catch (Exception e){
            e.printStackTrace();

            MyApplication.getInstance().trackEvent(TRACK_LOG,"crash",e.getMessage());

        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        try {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            int id = item.getItemId();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Main Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://com.igniva.youtubeplayer.ui.activities/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
    }
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }


    public  static void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.left_in, R.anim.left_out);
        fragmentTransaction.replace(R.id.fl_main, fragment);

        fragmentTransaction.commitAllowingStateLoss();


    }

    @Override
    public void onMenuToggle(boolean opened) {

        if(menu_fab != null) {

            if (opened) {

                menu_fab.getMenuIconView().setImageDrawable(getResources().getDrawable(R.drawable.ic_clear_white_24dp));

            } else {

                menu_fab.getMenuIconView().setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_filter));

            }

        }

    }
    public void fetchLatestVideos(){
        clear();
        Log.d("Reading: ", "Reading all contacts..");

        mAllData = mDatabaseHandler.getAllContacts();

        for (DataYoutubePojo cn : mAllData) {
            String log = "video_Id: " + cn.getVideo_no() + " , Video_Title: " + cn.getVideo_title() + " Video_id" + cn.getVideo_id() + "Video_channel" + cn.getVideo_channel() +
                    " ,Duration: " + cn.getVideo_duration() + " Rating: " + cn.getVideo_rating() + " Thumb: " + cn.getVideo_thumb() + " Playlist: " + cn.getVideo_playlist() +
                    " order: " + cn.getVideo_order() + " Favourite= " + cn.getVideo_favourite();

            listCategories.add(cn.getVideo_id().toString());
            listNames.add(cn.getVideo_title().toString());
            listDuration.add(cn.getVideo_duration().toString());
            listRating.add("" + cn.getVideo_rating());
            listFavourite.add(cn.getVideo_favourite());

            // Writing Contacts to log
            Log.e("Name: ", log);

        }

        setUpLayouts();

    }


    public void fetchTopRatedVideos(){
        clear();
        Log.d("Reading: ", "Reading all contacts..");

        mAllData = mDatabaseHandler.getAllContacts();

        Collections.sort(mAllData, new Comparator<DataYoutubePojo>() {
            @Override
            public int compare(DataYoutubePojo c1, DataYoutubePojo c2) {
                return Integer.compare(c1.getVideo_rating(), c2.getVideo_rating());
            }
        });

        Collections.reverse(mAllData);

        for (DataYoutubePojo cn : mAllData) {
            String log = "video_Id: " + cn.getVideo_no() + " , Video_Title: " + cn.getVideo_title() + " Video_id" + cn.getVideo_id() + "Video_channel" + cn.getVideo_channel() +
                    " ,Duration: " + cn.getVideo_duration() + " Rating: " + cn.getVideo_rating() + " Thumb: " + cn.getVideo_thumb() + " Playlist: " + cn.getVideo_playlist() +
                    " order: " + cn.getVideo_order() + " Favourite= " + cn.getVideo_favourite();

            listCategories.add(cn.getVideo_id().toString());
            listNames.add(cn.getVideo_title().toString());
            listDuration.add(cn.getVideo_duration().toString());
            listRating.add("" + cn.getVideo_rating());
            listFavourite.add(cn.getVideo_favourite());


        }
        if (listCategories.size() == 0) {

            CategoriesFragment.no_data_found_layout.setVisibility(View.VISIBLE);

        }


        setUpLayouts();

    }

    public void setUpLayouts() {

        try {
            CategoriesFragment.mRvCategories.setVisibility(View.VISIBLE);
            CategoriesFragment.mRvCategories.setAdapter(new CategoryListAdapter(MainActivity.this, listCategories, listNames, listDuration, listRating, listFavourite, 1));
            CategoriesFragment.mRvCategories.setHasFixedSize(true);
            GridLayoutManager mLayoutManager = new GridLayoutManager(MainActivity.this, 1);
            CategoriesFragment.mRvCategories.setLayoutManager(mLayoutManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clear(){
        listCategories.clear();
        listNames.clear();
        listDuration.clear();
        listFavourite.clear();
        listRating.clear();
    }
    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            super.onBackPressed();
        }
    }
}
