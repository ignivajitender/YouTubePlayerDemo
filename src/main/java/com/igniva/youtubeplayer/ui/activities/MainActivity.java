package com.igniva.youtubeplayer.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.igniva.youtubeplayer.model.DataGalleryPojo;
import com.igniva.youtubeplayer.model.DataYoutubePojo;
import com.igniva.youtubeplayer.ui.fragments.CategoriesFragment;
import com.igniva.youtubeplayer.R;

import com.igniva.youtubeplayer.utils.Constants;
import com.igniva.youtubeplayer.utils.UtilsUI;
import com.mikepenz.materialdrawer.Drawer;

import java.io.Serializable;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FragmentTransaction fragmentTransaction;
    private Context context;
    private Drawer drawer;
    public static Toolbar toolbar;
    private Boolean doubleBackToExitPressedOnce = false;
   public static List<DataYoutubePojo> mAllData;
   public static List<DataGalleryPojo> mAllImages;
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
        //
        if (getIntent().hasExtra(Constants.ALL_DATA)) {
            mAllData = (List<DataYoutubePojo>) getIntent().getSerializableExtra(Constants.ALL_DATA);
//            for (DataYoutubePojo cn : mAllData) {
//                String log = "video_Id: " + cn.getVideo_no() + " , Video_Title: " + cn.getVideo_title()+" Video_id"+cn.getVideo_id()+"Video_channel"+cn.getVideo_channel() +
//                        " ,Duration: " + cn.getVideo_duration()+" Rating: " + cn.getVideo_rating()+" Thumb: " + cn.getVideo_thumb()+" Playlist: " + cn.getVideo_playlist()+
//                        " order: " + cn.getVideo_order()+" Favourite= "+cn.getVideo_favourite();
//            }
        }

        if (getIntent().hasExtra(Constants.ALL_IMAGES)) {
            mAllImages = (List<DataGalleryPojo>) getIntent().getSerializableExtra(Constants.ALL_IMAGES);
        }
        initToolBar();
        //
        drawer = UtilsUI.setNavigationDrawer(MainActivity.this, context, toolbar);
        drawer.getDrawerLayout();
        //
        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        MenuItem item = navigationView.getMenu().getItem(0);
        onNavigationItemSelected(item);


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_main, CategoriesFragment.newInstance());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        fragmentTransaction.commitAllowingStateLoss();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
                super.onBackPressed();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            int id = item.getItemId();

//            if (id == R.id.nav_camera) {
//
//                fragmentTransaction.replace(R.id.fl_main, CategoriesFragment.newInstance());
//            }
//            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//            drawer.closeDrawer(GravityCompat.START);
//
//            fragmentTransaction.commitAllowingStateLoss();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
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
//        AppIndex.AppIndexApi.start(client, viewAction);
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

}
