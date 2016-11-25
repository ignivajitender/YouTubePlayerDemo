package com.igniva.youtubeplayer.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.igniva.youtubeplayer.model.DataGalleryPojo;
import com.igniva.youtubeplayer.ui.activities.MainActivity;
import com.igniva.youtubeplayer.ui.application.MyApplication;
import com.igniva.youtubeplayer.ui.fragments.CategoriesFragment;
import com.igniva.youtubeplayer.ui.adapters.CategoryListAdapter;
import com.igniva.youtubeplayer.ui.adapters.CategoryListAdapterGallery;
import com.igniva.youtubeplayer.R;
import com.igniva.youtubeplayer.model.DataYoutubePojo;
import com.igniva.youtubeplayer.db.DatabaseHandler;
import com.igniva.youtubeplayer.ui.activities.YouTubeActivity;
import com.igniva.youtubeplayer.ui.fragments.FavouritesFragment;
import com.igniva.youtubeplayer.ui.fragments.GallaryFragment;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import static com.igniva.youtubeplayer.ui.activities.MainActivity.TRACK_LOG;


public class UtilsUI {


    public static boolean galery_status, favourite_status, channels_status;

    static MainActivity m;

    static Boolean isShare = true;

    static String data;

    static  EditText share_message_editText;

    private final static String APP_PNAME = "com.igniva.youtubeplayer";


    public static int darker(int color, double factor) {

        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        return Color.argb(a, Math.max((int) (r * factor), 0), Math.max((int) (g * factor), 0), Math.max((int) (b * factor), 0));

    }

    public static Drawer setNavigationDrawer(Activity activity, final Context context, Toolbar toolbar) {

        int header;

        m = new MainActivity();

        final DatabaseHandler db = new DatabaseHandler(context);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        header = R.color.colorPrimaryDark;

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

                switch (iDrawerItem.getIdentifier()) {
                    case 1:

                        favourite_status = false;

                        galery_status = false;

                        MainActivity.replaceFragment(new CategoriesFragment());

                        MainActivity.toolbar.setTitle("Latest Videos");

                        break;
                    case 2:

                        favourite_status = false;

                        MainActivity.replaceFragment(new GallaryFragment());

                        MainActivity.menu_fab.close(true);

                        MainActivity.toolbar.setTitle("Gallery");

                        break;

                    case 3:

                        favourite_status = true;

                        galery_status = false;

                        MainActivity.replaceFragment(new FavouritesFragment());

                        MainActivity.menu_fab.close(true);

                        MainActivity.toolbar.setTitle("Favourite");

                        break;

                    case 4:

                        favourite_status = false;

                        galery_status = false;

                        Intent settingActivityIntent = new Intent(context, YouTubeActivity.class);

                        settingActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(settingActivityIntent);

                        try {

                            ((Activity) context).overridePendingTransition(R.anim.left_in, R.anim.left_out);

                        }catch (Exception e){

                            e.printStackTrace();

                        }

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
                        String appname = getAppName(context);
                        LayoutInflater layoutInflater = LayoutInflater.from(context);

                        View v = layoutInflater.inflate(R.layout.custum_share_dailog, null);

                       MaterialDialog.Builder  materialDialog = new MaterialDialog.Builder((Activity)context)
                                .title(context.getResources().getString(R.string.share)+" " +appname)
                                .customView(v,true)

                                .negativeText(context.getResources().getString(R.string.Later))
                                .positiveText(context.getResources().getString(R.string.share))
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                        Intent shareIntent = new Intent();

                                        shareIntent.setAction(Intent.ACTION_SEND);

                                        shareIntent.putExtra(Intent.EXTRA_TEXT,
                                                share_message_editText.getText().toString());

                                        shareIntent.setType("text/plain");

                                        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                        try {

                                            context.startActivity(shareIntent);

                                        }catch (Exception e){

                                            e.printStackTrace();

                                            MyApplication.getInstance().trackEvent(TRACK_LOG,"crash",e.getMessage());

                                        }
                                    }
                                })
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                        dialog.dismiss();

                                    }
                                });

                        share_message_editText = (EditText)v.findViewById(R.id.iv_share_message);

                        share_message_editText.setText("Hey check out my app at: https://play.google.com/store/apps/details?id=" + context.getPackageName());

                        share_message_editText.setSelection(share_message_editText.getText().length());

                        materialDialog.show();

                        break;

                    default:

                        break;

                }

                return false;
            }
        });

        return drawerBuilder.build();
    }

    public static String getAppName(Context context){
        final PackageManager pm = context.getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo(context.getPackageName(), 0);
        } catch (Exception e) {
            ai = null;
        }
        final String applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
        return applicationName;
    }

}