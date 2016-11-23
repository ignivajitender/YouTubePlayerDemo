package com.igniva.youtubeplayer.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.igniva.youtubeplayer.libs.FloatingActionButton;
import com.igniva.youtubeplayer.libs.FloatingActionMenu;
import com.igniva.youtubeplayer.model.DataGalleryPojo;
import com.igniva.youtubeplayer.model.DataYoutubePojo;
import com.igniva.youtubeplayer.ui.fragments.CategoriesFragment;
import com.igniva.youtubeplayer.R;

import com.igniva.youtubeplayer.ui.fragments.FavouritesFragment;
import com.igniva.youtubeplayer.ui.fragments.TopRatedCategoryFragment;
import com.igniva.youtubeplayer.utils.Constants;
import com.igniva.youtubeplayer.utils.UtilsUI;
import com.mikepenz.materialdrawer.Drawer;

import java.io.Serializable;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,FloatingActionMenu.OnMenuToggleListener {
    static FragmentManager fragmentManager;
    private Context context;
    private Drawer drawer;
    public static Toolbar toolbar;
    private Boolean doubleBackToExitPressedOnce = false;
   public static List<DataYoutubePojo> mAllData;
   public static List<DataGalleryPojo> mAllImages;

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

        fragmentManager = getSupportFragmentManager();

        if (getIntent().hasExtra(Constants.ALL_DATA)) {
            mAllData = (List<DataYoutubePojo>) getIntent().getSerializableExtra(Constants.ALL_DATA);

        }

        if (getIntent().hasExtra(Constants.ALL_IMAGES)) {
            mAllImages = (List<DataGalleryPojo>) getIntent().getSerializableExtra(Constants.ALL_IMAGES);
        }
        initToolBar();

        try {

            drawer = UtilsUI.setNavigationDrawer(MainActivity.this, context, toolbar);

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

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.replaceFragment(new CategoriesFragment());


                MainActivity.toolbar.setTitle("Latest Videos");
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                menu_fab.close(true);

                MainActivity.replaceFragment(new TopRatedCategoryFragment());

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

}
