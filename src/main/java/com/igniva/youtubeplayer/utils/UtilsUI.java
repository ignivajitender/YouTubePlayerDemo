package com.igniva.youtubeplayer.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.igniva.youtubeplayer.model.DataGalleryPojo;
import com.igniva.youtubeplayer.ui.activities.MainActivity;
import com.igniva.youtubeplayer.ui.fragments.CategoriesFragment;
import com.igniva.youtubeplayer.ui.adapters.CategoryListAdapter;
import com.igniva.youtubeplayer.ui.adapters.CategoryListAdapterGallery;
import com.igniva.youtubeplayer.R;
import com.igniva.youtubeplayer.model.DataYoutubePojo;
import com.igniva.youtubeplayer.db.DatabaseHandler;
import com.igniva.youtubeplayer.ui.activities.YouTubeActivity;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;

import yt.sdk.access.InitializationException;

public class UtilsUI {

    public static ArrayList<String> listCategories, listDuration, listNames, listRating, listFavourite;
    static List<DataYoutubePojo> contacts;
    public static ArrayList<String> small_images_url, medium_images_url, large_images_url, channels_name, channel_thumb;
    public static boolean galery_status, favourite_status, channels_status;
    static List<DataYoutubePojo> mAllData;
    static List<DataGalleryPojo> mAllImages;
    static MainActivity m;

    private final static String APP_PNAME = "com.igniva.youtubeplayer";// Package Name


    public static int darker(int color, double factor) {
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        return Color.argb(a, Math.max((int) (r * factor), 0), Math.max((int) (g * factor), 0), Math.max((int) (b * factor), 0));
    }

    public static Drawer setNavigationDrawer(Activity activity, final Context context, Toolbar toolbar) {
        final String loadingLabel = "...";
        int header;
        String apps, systemApps, favoriteApps, hiddenApps;

        channels_name = new ArrayList<>();
        channel_thumb = new ArrayList<>();
        listCategories = new ArrayList<String>();
        listDuration = new ArrayList<String>();
        listNames = new ArrayList<String>();
        listRating = new ArrayList<String>();
        listFavourite = new ArrayList<String>();
        small_images_url = new ArrayList<String>();
        medium_images_url = new ArrayList<String>();
        large_images_url = new ArrayList<String>();

        m = new MainActivity();


        final DatabaseHandler db = new DatabaseHandler(context);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        header = R.color.colorPrimaryDark;

//        if (appAdapter != null) {
//            apps = Integer.toString(appAdapter.getItemCount());
//        } else {
//            apps = loadingLabel;
//        }
//        if (appSystemAdapter != null) {
//            systemApps = Integer.toString(appSystemAdapter.getItemCount());
//        } else {
//            systemApps = loadingLabel;
//        }
//        if (appFavoriteAdapter != null) {
//            favoriteApps = Integer.toString(appFavoriteAdapter.getItemCount());
//        } else {
//            favoriteApps = loadingLabel;
//        }
//        if (appHiddenAdapter != null) {
//            hiddenApps = Integer.toString(appHiddenAdapter.getItemCount());
//        } else {
//            hiddenApps = loadingLabel;
//        }

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(header)
                .build();


        Integer badgeColor = ContextCompat.getColor(context, R.color.divider);
        BadgeStyle badgeStyle = new BadgeStyle(badgeColor, badgeColor).withTextColor(Color.GRAY);

        DrawerBuilder drawerBuilder = new DrawerBuilder();
        drawerBuilder.withActivity(activity);
        drawerBuilder.withToolbar(toolbar);
        drawerBuilder.withAccountHeader(headerResult);
//        drawerBuilder.withStatusBarColor(UtilsUI.darker(appPreferences.getPrimaryColorPref(), 0.8));


        drawerBuilder.addDrawerItems(
                new PrimaryDrawerItem().withName("Videos").withIcon(R.drawable.ic_new_releases_black_24dp).withIdentifier(1),
                new PrimaryDrawerItem().withName("Gallery").withIcon(R.drawable.ic_menu_gallery).withIdentifier(2),

                new PrimaryDrawerItem().withName("Favourite").withIcon(R.drawable.ic_favorite_black_24dp).withIdentifier(3),

              //  new PrimaryDrawerItem().withName("Gallery").withIcon(R.drawable.ic_menu_gallery).withIdentifier(5),

                new DividerDrawerItem(),

                new SecondaryDrawerItem().withName("Settings").withIcon(R.drawable.ic_settings_black_24dp).withSelectable(false).withIdentifier(4),

                new SecondaryDrawerItem().withName("Rate Us").withIcon(R.drawable.top_rated).withSelectable(false).withIdentifier(5),

                new SecondaryDrawerItem().withName("More Free Apps").withIcon(R.drawable.ic_menu_send).withSelectable(false).withIdentifier(6),

                new SecondaryDrawerItem().withName("Share").withIcon(R.drawable.ic_menu_share).withSelectable(false).withIdentifier(7));


        drawerBuilder.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem iDrawerItem) {

                CategoriesFragment.hideShowLayout(View.GONE);

                CategoriesFragment.menu_fab.close(true);

                switch (iDrawerItem.getIdentifier()) {
                    case 1:

                        CategoriesFragment.hideShowLayout(View.GONE);

                        clear();
                        favourite_status = false;
                        galery_status = false;
                        channels_status = false;

                        // Reading all contacts
                        Log.d("Reading: ", "Reading all contacts..");
//                        contacts = db.getAllContacts();

                        mAllData = db.getAllContacts();
                        for (DataYoutubePojo cn : mAllData) {
                            String log = "video_Id: " + cn.getVideo_no() + " , Video_Title: " + cn.getVideo_title() + " Video_id" + cn.getVideo_id() + "Video_channel" + cn.getVideo_channel() +
                                    " ,Duration: " + cn.getVideo_duration() + " Rating: " + cn.getVideo_rating() + " Thumb: " + cn.getVideo_thumb() + " Playlist: " + cn.getVideo_playlist() +
                                    " order: " + cn.getVideo_order() + " Favourite= " + cn.getVideo_favourite();

                            listCategories.add(cn.getVideo_id().toString());
                            listNames.add(cn.getVideo_title().toString());
                            listDuration.add(cn.getVideo_duration().toString());
                            listRating.add(""+cn.getVideo_rating());
                            listFavourite.add(cn.getVideo_favourite());


                            // Writing Contacts to log
                            Log.e("Name: ", log);

                        }

                        CategoriesFragment.listCategories = listCategories;
                        CategoriesFragment.listNames = listNames;
                        CategoriesFragment.listDuration = listDuration;
                        CategoriesFragment.listRating = listRating;
                        CategoriesFragment.listFavourite = listFavourite;



                            try {

                                setUpGridLayout(context);

                            }catch (Exception e){
                                e.printStackTrace();
                            }


                        MainActivity.toolbar.setTitle("Latest Videos");
                        break;
                    case 2:
                        CategoriesFragment.hideShowLayout(View.GONE);

                        favourite_status = false;
                        favourite_status = false;
                        channels_status = false;

                        small_images_url.clear();
                        medium_images_url.clear();
                        large_images_url.clear();

                        CategoriesFragment.mRvCategories.setVisibility(View.VISIBLE);

                        galery_status = true;
                        int no = sharedPreferences.getInt("cat", 2);

                        mAllImages = m.getMyImages();

                        for (DataGalleryPojo cn : mAllImages) {
//                            String log = "video_Id: " + cn.getVideo_no() + " , Video_Title: " + cn.getVideo_title() + " Video_id" + cn.getVideo_id() + "Video_channel" + cn.getVideo_channel() +
//                                    " ,Duration: " + cn.getVideo_duration() + " Rating: " + cn.getVideo_rating() + " Thumb: " + cn.getVideo_thumb() + " Playlist: " + cn.getVideo_playlist() +
//                                    " order: " + cn.getVideo_order() + " Favourite= " + cn.getVideo_favourite();

                            large_images_url.add(cn.getImage_link().toString());

                            // Writing Contacts to log
//                            Log.e("Name: ", log);

                        }


                            CategoriesFragment.mRvCategories.setAdapter(new CategoryListAdapterGallery(context, large_images_url, 1));
                            CategoriesFragment.mRvCategories.setHasFixedSize(true);
                            GridLayoutManager mLayoutManager = new GridLayoutManager(context, 1);
                            CategoriesFragment.mRvCategories.setLayoutManager(mLayoutManager);

                        MainActivity.toolbar.setTitle("Gallery");
                        break;


                    case 3:

                        CategoriesFragment.hideShowLayout(View.GONE);

                        clear();
                        channels_status = false;
                        favourite_status = true;
                        galery_status = false;
                        // Reading all contacts
                        Log.d("Reading: ", "Reading all contacts..");
//                        contacts = db.getAllContacts();

         //               mAllData = m.getMyData();
                        mAllData = db.getAllContacts();
                        for (DataYoutubePojo cn : mAllData) {
                            String log = "video_Id_favourite: " + cn.getVideo_no() + " , Video_Title: " + cn.getVideo_title() + " Video_id" + cn.getVideo_id() + "Video_channel" + cn.getVideo_channel() +
                                    " ,Duration: " + cn.getVideo_duration() + " Rating: " + cn.getVideo_rating() + " Thumb: " + cn.getVideo_thumb() + " Playlist: " + cn.getVideo_playlist() +
                                    " order: " + cn.getVideo_order() + " Favourite= " + cn.getVideo_favourite();

                            if (cn.getVideo_favourite().equals("1")) {
                                listCategories.add(cn.getVideo_id().toString());
                                listNames.add(cn.getVideo_title().toString());
                                listDuration.add(cn.getVideo_duration().toString());
                                listRating.add(""+cn.getVideo_rating());
                                listFavourite.add(cn.getVideo_favourite());
                            }

// Writing Contacts to log
                            Log.e("Name: ", log);

                        }

                        if(listCategories.size() == 0){
                            CategoriesFragment.hideShowLayout(View.GONE);
                        }

                        CategoriesFragment.listCategories = listCategories;
                        CategoriesFragment.listNames = listNames;
                        CategoriesFragment.listDuration = listDuration;
                        CategoriesFragment.listRating = listRating;
                        CategoriesFragment.listFavourite = listFavourite;

                        try {

                            setUpGridLayout(context);

                        }catch (Exception e){
                            e.printStackTrace();
                        }


                        MainActivity.toolbar.setTitle("Favourite");
                        break;


                    case 4:
                        CategoriesFragment.mRvCategories.setVisibility(View.VISIBLE);
                        favourite_status = false;
                        channels_status = false;
                        galery_status = false;
                        Intent in5 = new Intent(context, YouTubeActivity.class);
                        in5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(in5);
                        //overridePendingTransition(R.anim.right_in, R.anim.right_out);

                        break;

                    case 5:
                        Uri uri = Uri.parse("market://details?id=" + "com.sqwip");
                        Intent rateApp = new Intent(Intent.ACTION_VIEW, uri);
                        // To count with Play market backstack, After pressing back button,
                        // to taken back to our application, we need to add following flags to intent.
                        rateApp.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK|
                                Intent.FLAG_ACTIVITY_NEW_TASK
                                );
                        try {
                            context.startActivity(rateApp);
                        } catch (ActivityNotFoundException e) {
                            context.startActivity(new Intent(Intent.ACTION_VIEW,

                                    Uri.parse("http://play.google.com/store/apps/details?id=" + "com.sqwip")));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;


                    case 6:
                         uri = Uri.parse("market://search?q=pub:saga");
                        Intent moreFreeApps = new Intent(Intent.ACTION_VIEW, uri);
                        // To count with Play market backstack, After pressing back button,
                        // to taken back to our application, we need to add following flags to intent.
                        moreFreeApps.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK|
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        try {
                            context.startActivity(moreFreeApps);
                        } catch (ActivityNotFoundException e) {
                            context.startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                        break;

                    case 7:

                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_TEXT,
                                "Hey check out my app at: https://play.google.com/store/apps/details?id="+context.getPackageName());
                        shareIntent.setType("text/plain");
                        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        try {
                            context.startActivity(shareIntent);
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                        break;
                    default:
                        break;
                }

                return false;
            }
        });

        return drawerBuilder.build();
    }

    private static void clear() {

        channel_thumb.clear();
        channels_name.clear();

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

    public static void setUpGridLayout(Context context) throws InitializationException {
        CategoriesFragment.mRvCategories.setAdapter(new CategoryListAdapter(context, listCategories, listNames, listDuration, listRating, listFavourite, 1));
        CategoriesFragment.mRvCategories.setHasFixedSize(true);
        GridLayoutManager mLayoutManager = new GridLayoutManager(context, 1);
        CategoriesFragment.mRvCategories.setLayoutManager(mLayoutManager);
    }

}