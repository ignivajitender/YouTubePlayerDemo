package com.igniva.youtubeplayer.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.igniva.youtubeplayer.ui.adapters.CategoryListAdapter;
import com.igniva.youtubeplayer.ui.adapters.CategoryListAdapterChannels;
import com.igniva.youtubeplayer.R;
import com.igniva.youtubeplayer.ui.fragments.CategoriesFragment;
//import com.igniva.youtubeplayer.utils.AppPreferences;
import com.igniva.youtubeplayer.utils.UtilsUI;
import com.mikepenz.materialdrawer.Drawer;

import java.util.ArrayList;

public class ChannelActivity extends AppCompatActivity {
    FragmentTransaction fragmentTransaction;
    private Context context;
    private Drawer drawer;
//    private AppPreferences appPreferences;
    private static ArrayList<String> Channel_name,Channel_image;
    private static RecyclerView mRvCategories;
    public static ArrayList<String> listCategories,listDuration,listNames,listRating,listFavourite;

    private Menu menu;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ChannelActivity.this);
        editor = sharedPreferences.edit();
        Channel_name=new ArrayList<>();
        Channel_image=new ArrayList<>();




        listCategories = new ArrayList<String>();
        listDuration = new ArrayList<String>();
        listNames = new ArrayList<String>();
        listRating = new ArrayList<String>();
        listFavourite=new ArrayList<String>();

        listCategories.clear();
        listDuration.clear();
        listNames.clear();
        listRating.clear();
        listFavourite.clear();

        this.context = this;


        Intent intent=getIntent();

        listCategories=intent.getStringArrayListExtra("listCategories");
        listNames=intent.getStringArrayListExtra("listNames");
        listDuration=intent.getStringArrayListExtra("listDuration");
        listRating=intent.getStringArrayListExtra("listRating");
        listFavourite=intent.getStringArrayListExtra("listFavourite");

        toolbar.setTitle(intent.getStringExtra("toolbar_title"));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do something you want

                onBackPressed();
            }
        });

        mRvCategories = (RecyclerView)findViewById(R.id.rv_categories);
        getCategoriesData(2);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        this.menu=menu;

                if(sharedPreferences.getInt("cat",2)==2)
        {
            menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.list));
            getCategoriesData(2);
        }
        else
        {
            menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.grid));
            getCategoriesData(1);
        }

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_settings:
                if(menu.getItem(0).getTitle().equals(getResources().getString(R.string.grid))) {
                    menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.list));
                    menu.getItem(0).setTitle(getResources().getString(R.string.list));

                    if(UtilsUI.galery_status) {
                        getCategoriesData(2);
                    }
                    else
                    {
                        getCategoriesData(2);
                    }
                    int cat=2;
                    editor.putInt("cat",cat);
                    editor.commit();
                }else{
                    menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.grid));
                    menu.getItem(0).setTitle(getResources().getString(R.string.grid));
                    if(UtilsUI.galery_status) {
                        getCategoriesData(1);
                    }
                    else
                    {
                        getCategoriesData(1);
                    }
                    int cat=1;
                    editor.putInt("cat",cat);
                    editor.commit();
                }
                break;

        }
        return false;


    }


    @Override
    public void onBackPressed() {
//        if (drawer.isDrawerOpen()) {
//            drawer.closeDrawer();
//        } else {
            super.onBackPressed();
//        }
    }


    private void getCategoriesData(int pos) {

        try {
            int no = sharedPreferences.getInt("cat", 2);

            mRvCategories.setAdapter(new CategoryListAdapter(context, listCategories,listNames,listDuration,listRating,listFavourite,no));

            mRvCategories.setHasFixedSize(true);
            GridLayoutManager mLayoutManager = new GridLayoutManager(this, no);
            mRvCategories.setLayoutManager(mLayoutManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
