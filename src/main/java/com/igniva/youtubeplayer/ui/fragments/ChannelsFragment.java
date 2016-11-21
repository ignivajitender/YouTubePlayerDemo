package com.igniva.youtubeplayer.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.igniva.youtubeplayer.ui.activities.All_Image_View;
import com.igniva.youtubeplayer.ui.adapters.CategoryListAdapter;
import com.igniva.youtubeplayer.R;
import com.igniva.youtubeplayer.model.DataYoutubePojo;
import com.igniva.youtubeplayer.db.DatabaseHandler;
import com.igniva.youtubeplayer.utils.UtilsUI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by igniva-php-08 on 18/5/16.
 */
public class ChannelsFragment extends BaseFragment{
    View mView;
    public static RecyclerView mRvCategories;
    private Menu menu;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    ArrayList<String> small_images_url,medium_images_url,large_images_url ;

    public static  ArrayList<String> listCategories,listDuration,listNames,listRating,listFavourite;

    public static ChannelsFragment newInstance() {
        ChannelsFragment fragment = new ChannelsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_channels, container, false);
        setHasOptionsMenu(true);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = sharedPreferences.edit();

        small_images_url=new ArrayList<>();
        medium_images_url=new ArrayList<>();
        large_images_url=new ArrayList<>();

        small_images_url.add("http://api.androidhive.info/images/glide/small/deadpool.jpg");
        medium_images_url.add("http://api.androidhive.info/images/glide/medium/deadpool.jpg");
        large_images_url.add("http://api.androidhive.info/images/glide/large/deadpool.jpg");

        small_images_url.add("http://api.androidhive.info/images/glide/small/cacw.jpg");
        medium_images_url.add("http://api.androidhive.info/images/glide/medium/cacw.jpg");
        large_images_url.add("http://api.androidhive.info/images/glide/large/cacw.jpg");

        small_images_url.add("http://api.androidhive.info/images/glide/small/cacw.jpg");
        medium_images_url.add("http://api.androidhive.info/images/glide/medium/cacw.jpg");
        large_images_url.add("http://api.androidhive.info/images/glide/large/cacw.jpg");

        small_images_url.add("http://api.androidhive.info/images/glide/small/bourne.jpg");
        medium_images_url.add("http://api.androidhive.info/images/glide/medium/bourne.jpg");
        large_images_url.add("http://api.androidhive.info/images/glide/large/bourne.jpg");

        small_images_url.add("http://api.androidhive.info/images/glide/small/squad.jpg");
        medium_images_url.add("http://api.androidhive.info/images/glide/medium/squad.jpg");
        large_images_url.add("http://api.androidhive.info/images/glide/large/squad.jpg");

        small_images_url.add("http://api.androidhive.info/images/glide/small/doctor.jpg");
        medium_images_url.add("http://api.androidhive.info/images/glide/medium/doctor.jpg");
        large_images_url.add("http://api.androidhive.info/images/glide/large/doctor.jpg");

        small_images_url.add("http://api.androidhive.info/images/glide/small/dory.jpg");
        medium_images_url.add("http://api.androidhive.info/images/glide/medium/dory.jpg");
        large_images_url.add("http://api.androidhive.info/images/glide/large/dory.jpg");

        small_images_url.add("http://api.androidhive.info/images/glide/small/hunger.jpg");
        medium_images_url.add("http://api.androidhive.info/images/glide/medium/hunger.jpg");
        large_images_url.add("http://api.androidhive.info/images/glide/large/hunger.jpg");

        small_images_url.add("http://api.androidhive.info/images/glide/small/hours.jpg");
        medium_images_url.add("http://api.androidhive.info/images/glide/medium/hours.jpg");
        large_images_url.add("http://api.androidhive.info/images/glide/large/hours.jpg");

        small_images_url.add("http://api.androidhive.info/images/glide/small/ipman3.jpg");
        medium_images_url.add("http://api.androidhive.info/images/glide/medium/ipman3.jpg");
        large_images_url.add("http://api.androidhive.info/images/glide/large/ipman3.jpg");



        listCategories = new ArrayList<String>();
        listDuration = new ArrayList<String>();
        listNames = new ArrayList<String>();
        listRating = new ArrayList<String>();
        listFavourite= new ArrayList<String>();



        boolean b=sharedPreferences.getBoolean("b",true);
//        Toast.makeText(getActivity(), ""+b, Toast.LENGTH_SHORT).show();
        DatabaseHandler db = new DatabaseHandler(getActivity());
//        if(b) {
////            Toast.makeText(getActivity(), "b "+b, Toast.LENGTH_SHORT).show();
//            /**
//             * CRUD Operations
//             * */
//            // Inserting Contacts
//            Log.d("Insert: ", "Inserting ..");
//            db.addContact(new DataYoutubePojo("1", "Android N Developer Preview Review!", "CEFWMP5M6Zk", "Bollywood", "09:09",3, "http://img.youtube.com/vi/CEFWMP5M6Zk/hqdefault.jpg", " ", " ", "0"));
//            db.addContact(new DataYoutubePojo("2", "Baby Ko Bass Pasand Hai Song| Sultan | Salman Khan | Anushka Sharma | Vishal | Badshah | Shalmali", "aWMTj-rejvc", "Bollywood", "02:35", "4", "http://img.youtube.com/vi/aWMTj-rejvc/hqdefault.jpg", " ", " ", "0"));
//            db.addContact(new DataYoutubePojo("3", "Dheere Dheere Se Meri Zindagi Video Song (OFFICIAL) Hrithik Roshan, Sonam Kapoor | Yo Yo Honey Singh", "nCD2hj6zJEc", "Bollywood", "05:04", "1", "http://img.youtube.com/vi/nCD2hj6zJEc/hqdefault.jpg", " ", " ", "0"));
//            db.addContact(new DataYoutubePojo("4", " Ariana Grande - Problem ft. Iggy Azalea ", "iS1g8G_njx8", "Hollywood", "03:27", 5, "http://img.youtube.com/vi/iS1g8G_njx8/hqdefault.jpg", " ", " ", "0"));
//            db.addContact(new DataYoutubePojo("5", "Top 6 Hindi Video Songs 2016 ", "vxIj3JKEGvE", "Bollywood", "20:32", 2, "http://img.youtube.com/vi/vxIj3JKEGvE/hqdefault.jpg", " ", " ", "0"));
//            db.addContact(new DataYoutubePojo("6", "Best of ARIJIT SINGH Romantic songs with Lyrics Part 1", "9s5l6w-35Wc", "Bollywood", "54:57", 3, "http://img.youtube.com/vi/9s5l6w-35Wc/hqdefault.jpg", " ", " ", "0"));
//            db.addContact(new DataYoutubePojo("7", "Ek Villain Full Songs Audio Jukebox | Sidharth Malhotra | Shraddha Kapoor ", "zFxo_397aL8", "Bollywood", "31:21", 1, "http://img.youtube.com/vi/zFxo_397aL8/hqdefault.jpg", " ", " ", "0"));
//            db.addContact(new DataYoutubePojo("8", "BEST OF EMRAN HASHMI", "6HiAZTrCf_s", "Bollywood", "1:03:19", 5, "http://img.youtube.com/vi/6HiAZTrCf_s/hqdefault.jpg", " ", " ", "0"));
//            db.addContact(new DataYoutubePojo("9", "Tu hi meri shab hai - Gangster", "TrupdvVQnpM", "Bollywood", "06:37", 3, "http://img.youtube.com/vi/TrupdvVQnpM/hqdefault.jpg", " ", " ", "0"));
//            db.addContact(new DataYoutubePojo("10", "Agar Tum Mil Jao - Zeher (HD)", "i2MbOhBkkf0", "Bollywood", "04:33", 2, "http://img.youtube.com/vi/i2MbOhBkkf0/hqdefault.jpg", " ", " ", "0"));
//            db.addContact(new DataYoutubePojo("11", "Lamha Lamha [Full Song] Gangster- A Love Story ", "dYei_71npF4", "Bollywood", "05:04", 5, "http://img.youtube.com/vi/dYei_71npF4/hqdefault.jpg", " ", " ", "0"));
//
//            db.addContact(new DataYoutubePojo("12", "New Punjabi Songs 2016 || Mastana Jogi - Official Video || Kanwar Grewal || Latest Punjabi Song 2015 ", "pGutbmWNXoc", "Punjabi", "05:53", "5", "http://img.youtube.com/vi/pGutbmWNXoc/hqdefault.jpg", " ", " ", "0"));
//            db.addContact(new DataYoutubePojo("13", "Kinna sohna tenu rab ne banaya (Nusrat Fateh Ali Khan) ", "X196nARkzIA", "Punjabi", "07:54", 4, "http://img.youtube.com/vi/X196nARkzIA/hqdefault.jpg", " ", " ", "0"));
//            db.addContact(new DataYoutubePojo("14", "Aaya Laariye, Meesha Shafi & Naeem Abbas Rufi, Episode 4, Coke Studio Season 9", "zVnbojCYPxU", "Coke Studio", "07:29", "4", "http://img.youtube.com/vi/zVnbojCYPxU/hqdefault.jpg", " ", " ", "0"));
//            db.addContact(new DataYoutubePojo("15", "Afreen Afreen, Rahat Fateh Ali Khan & Momina Mustehsan, Episode 2, Coke Studio 9 ", "kw4tT7SCmaY", "Coke Studio", "06:44", "5", "http://img.youtube.com/vi/kw4tT7SCmaY/hqdefault.jpg", " ", " ", "0"));
//            db.addContact(new DataYoutubePojo("16", "Baliye (Laung Gawacha), Quratulain Baloch & Haroon Shahid, Episode 2 , Coke Studio 9", "xhgt47nvZUQ", "Coke Studio", "07:41", "4", "http://img.youtube.com/vi/xhgt47nvZUQ/hqdefault.jpg", " ", " ", "0"));
//            db.addContact(new DataYoutubePojo("17", "Bholay Bhalay, Meesha Shafi, Episode 2,Coke Studio 9", "7w8AR7jnhpc", "Coke Studio", "06:46", "4", "http://img.youtube.com/vi/7w8AR7jnhpc/hqdefault.jpg", " ", " ", "0"));
//            db.addContact(new DataYoutubePojo("18", "Akon - Smack That ft. Eminem", "bKDdT_nyP54", "Hollywood", "04:13", "4", "http://img.youtube.com/vi/bKDdT_nyP54/hqdefault.jpg", " ", " ", "0"));
//            db.addContact(new DataYoutubePojo("19", "Akon - Sorry, Blame It On Me", "ynMk2EwRi4Q", "Hollywood", "05:19", "3", "http://img.youtube.com/vi/ynMk2EwRi4Q/hqdefault.jpg", " ", " ", "0"));
//            db.addContact(new DataYoutubePojo("20", "Kaali Camaro (Full Video) | Amrit Maan | Latest Punjabi Song 2016 | Speed Records ", "XyCOJa5dcBE", "Punjabi", "04:15", "5", "http://img.youtube.com/vi/XyCOJa5dcBE/hqdefault.jpg", " ", " ", "0"));
//
//            editor.putBoolean("b",false);
//            editor.commit();
//        }

        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        List<DataYoutubePojo> contacts = db.getAllContacts();

        for (DataYoutubePojo cn : contacts) {
            String log = "video_Id: " + cn.getVideo_no() + " , Video_Title: " + cn.getVideo_title()+" Video_id"+cn.getVideo_id()+"Video_channel"+cn.getVideo_channel() +
                    " ,Duration: " + cn.getVideo_duration()+" Rating: " + cn.getVideo_rating()+" Thumb: " + cn.getVideo_thumb()+" Playlist: " + cn.getVideo_playlist()+
                    " order: " + cn.getVideo_order()+" Favourite= "+cn.getVideo_favourite();

            listCategories.add(cn.getVideo_id().toString());
            listNames.add(cn.getVideo_title().toString());
            listDuration.add(cn.getVideo_duration().toString());
            listRating.add(""+cn.getVideo_rating());
            listFavourite.add(cn.getVideo_favourite());


            // Writing Contacts to log
            Log.e("Name: ", log);

        }
        setUpLayouts();
        return mView;
    }

    @Override
    public void setUpLayouts() {

        mRvCategories = (RecyclerView) mView.findViewById(R.id.rv_categories);
        getCategoriesData(2);

    }

    @Override
    public void setDataInViewLayouts() {
    }

    private void getCategoriesDataOfGalary(int pos) {
        try {
//            listCategories=Arrays.asList("CEFWMP5M6Zk","aWMTj-rejvc","nCD2hj6zJEc","iS1g8G_njx8","vxIj3JKEGvE","9s5l6w-35Wc","zFxo_397aL8","6HiAZTrCf_s","TrupdvVQnpM","i2MbOhBkkf0","dYei_71npF4");
//            listCategories=SplashActivity.listCategories;
//            listDuration=Arrays.asList("09:09","02:35","05:04","03:27","20:32","54:57","31:21","1:03:19","06:37","04:33","05:04");
//            listDuration=SplashActivity.listDuration;
//            listNames=Arrays.asList("Android N Developer Preview Review!","Baby Ko Bass Pasand Hai Song | Sultan | Salman Khan | Anushka Sharma | Vishal | Badshah | Shalmali","Dheere Dheere Se Meri Zindagi Video Song (OFFICIAL) Hrithik Roshan, Sonam Kapoor | Yo Yo Honey Singh"," Ariana Grande - Problem ft. Iggy Azalea ","Top 6 Hindi Video Songs 2016 ","Best of ARIJIT SINGH Romantic songs with Lyrics Part 1","Ek Villain Full Songs Audio Jukebox | Sidharth Malhotra | Shraddha Kapoor ","BEST OF EMRAN HASHMI ","Tu hi meri shab hai - Gangster ","Agar Tum Mil Jao - Zeher (HD) ","Lamha Lamha [Full Song] Gangster- A Love Story ");
//            listNames=SplashActivity.listNames;
//            listRating=Arrays.asList("3","4","1","5","2","3","1","5","3","2","5");
//            listRating=SplashActivity.listRating;

//            listFavourite=SplashActivity.listFavourite;

            mRvCategories.setAdapter(new CategoryListAdapterGallery(getActivity(), large_images_url));
            mRvCategories.setHasFixedSize(true);
            GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), pos);
            mRvCategories.setLayoutManager(mLayoutManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCategoriesData(int pos) {
        try {
//            listCategories=Arrays.asList("CEFWMP5M6Zk","aWMTj-rejvc","nCD2hj6zJEc","iS1g8G_njx8","vxIj3JKEGvE","9s5l6w-35Wc","zFxo_397aL8","6HiAZTrCf_s","TrupdvVQnpM","i2MbOhBkkf0","dYei_71npF4");
//            listCategories=SplashActivity.listCategories;
//            listDuration=Arrays.asList("09:09","02:35","05:04","03:27","20:32","54:57","31:21","1:03:19","06:37","04:33","05:04");
//            listDuration=SplashActivity.listDuration;
//            listNames=Arrays.asList("Android N Developer Preview Review!","Baby Ko Bass Pasand Hai Song | Sultan | Salman Khan | Anushka Sharma | Vishal | Badshah | Shalmali","Dheere Dheere Se Meri Zindagi Video Song (OFFICIAL) Hrithik Roshan, Sonam Kapoor | Yo Yo Honey Singh"," Ariana Grande - Problem ft. Iggy Azalea ","Top 6 Hindi Video Songs 2016 ","Best of ARIJIT SINGH Romantic songs with Lyrics Part 1","Ek Villain Full Songs Audio Jukebox | Sidharth Malhotra | Shraddha Kapoor ","BEST OF EMRAN HASHMI ","Tu hi meri shab hai - Gangster ","Agar Tum Mil Jao - Zeher (HD) ","Lamha Lamha [Full Song] Gangster- A Love Story ");
//            listNames=SplashActivity.listNames;
//            listRating=Arrays.asList("3","4","1","5","2","3","1","5","3","2","5");
//            listRating=SplashActivity.listRating;

//            listFavourite=SplashActivity.listFavourite;
            int no = sharedPreferences.getInt("cat", 2);
            mRvCategories.setAdapter(new CategoryListAdapter(getActivity(), listCategories,listNames,listDuration,listRating,listFavourite,no));
            mRvCategories.setHasFixedSize(true);
            GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), no);
            mRvCategories.setLayoutManager(mLayoutManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        this.menu = menu;

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


        super.onCreateOptionsMenu(menu, inflater);
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
                        getCategoriesDataOfGalary(2);
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
                        getCategoriesDataOfGalary(1);
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

    private class CategoryListAdapterGallery  extends RecyclerView.Adapter<CategoryListAdapterGallery.ViewHolder> {

        List<String> mImageUrl;
        List<String> mImageName;
        Context mContext;
        SQLiteDatabase db;

        public CategoryListAdapterGallery(Context context, List<String> listCategories) {
            this.mImageUrl=listCategories;
//            this.mImageName=listCategories;

            this.mContext = context;

//         db = SQLiteDatabase.openOrCreateDatabase("YouTubeDB", null);
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView mTvCategoryImg;

            public ViewHolder(View itemView) {
                super(itemView);

                mTvCategoryImg=(ImageView)itemView.findViewById(R.id.iv_adapter_image);


            }
        }



        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_image, parent, false);
            ViewHolder vhItem = new ViewHolder(v);
            return vhItem;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            try {

//






//
//                Picasso.with(mContext)
//                        .load(mImageUrl.get(position))
//                        .fit()
//                        .into(holder.mTvCategoryImg);


                Glide.with(mContext).load(mImageUrl.get(position))
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.mTvCategoryImg);



                holder.mTvCategoryImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent=new Intent(getActivity(),All_Image_View.class);
                        intent.putExtra("array", (Serializable) mImageUrl);
                        intent.putExtra("position",position);
                        startActivity(intent);



                    }
                });



            }catch (Exception e){
                e.printStackTrace();
                Log.e("Exception",""+e);

            }
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

}