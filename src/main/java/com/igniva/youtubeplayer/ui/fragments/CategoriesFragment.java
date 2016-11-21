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
import android.widget.TextView;

import com.igniva.youtubeplayer.libs.FloatingActionButton;
import com.igniva.youtubeplayer.libs.FloatingActionMenu;
import com.igniva.youtubeplayer.model.DataGalleryPojo;
import com.igniva.youtubeplayer.ui.activities.MainActivity;
import com.igniva.youtubeplayer.ui.adapters.CategoryListAdapter;
import com.igniva.youtubeplayer.R;
import com.igniva.youtubeplayer.model.DataYoutubePojo;
import com.igniva.youtubeplayer.ui.adapters.CategoryListAdapterChannels;
import com.igniva.youtubeplayer.ui.adapters.CategoryListAdapterGallery;
import com.igniva.youtubeplayer.utils.UtilsUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import yt.sdk.access.InitializationException;

/**
 * Created by igniva-php-08 on 18/5/16.
 */
public class CategoriesFragment extends BaseFragment {
    View mView;
    public static RecyclerView mRvCategories;
    private Menu menu;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    ArrayList<String> small_images_url, medium_images_url, large_images_url;
    List<DataYoutubePojo> mAllData;
    public static TextView message;
    public static ArrayList<String> channels_name, channel_thumb, listCategories, listDuration, listNames, listRating, listFavourite;

     public static FloatingActionMenu menu_fab;
     FloatingActionButton fab1,fab2,fab3;
    MainActivity main = new MainActivity();
    List<DataGalleryPojo> mAllImages = main.getMyImages();

    public static CategoriesFragment newInstance() {
        CategoriesFragment fragment = new CategoriesFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_category, container, false);
        setHasOptionsMenu(true);

        MainActivity activity = (MainActivity) getActivity();
        mAllData = activity.getMyData();

        for (DataYoutubePojo cn : mAllData) {
            String log = "video_Id: " + cn.getVideo_no() + " , Video_Title: " + cn.getVideo_title() + " Video_id" + cn.getVideo_id() + "Video_channel" + cn.getVideo_channel() +
                    " ,Duration: " + cn.getVideo_duration() + " Rating: " + cn.getVideo_rating() + " Thumb: " + cn.getVideo_thumb() + " Playlist: " + cn.getVideo_playlist() +
                    " order: " + cn.getVideo_order() + " Favourite= " + cn.getVideo_favourite();

            Log.e("Name2: ", log);

        }


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = sharedPreferences.edit();

        channels_name = new ArrayList<>();
        channel_thumb = new ArrayList<>();

        small_images_url = new ArrayList<>();
        medium_images_url = new ArrayList<>();
        large_images_url = new ArrayList<>();

        listCategories = new ArrayList<String>();
        listDuration = new ArrayList<String>();
        listNames = new ArrayList<String>();
        listRating = new ArrayList<String>();
        listFavourite = new ArrayList<String>();

        // Reading all contacts

        message = (TextView)mView.findViewById(R.id.iv_message);

        fetchLatestVideos();

        fab1 = (FloatingActionButton) mView.findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) mView.findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) mView.findViewById(R.id.fab3);
        menu_fab = (FloatingActionMenu) mView.findViewById(R.id.menu_red);


        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Reading all contacts
                clear();
                fetchLatestVideos();

                 setUpLayouts();

                MainActivity.toolbar.setTitle("Latest Videos");
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoriesFragment.message.setVisibility(View.INVISIBLE);

                clear();
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
                    listRating.add(""+cn.getVideo_rating());
                    listFavourite.add(cn.getVideo_favourite());

                    // Writing Contacts to log
//                            Log.e("Name: ", log);

                }
                if(listCategories.size() == 0){
                    CategoriesFragment.message.setVisibility(View.VISIBLE);;
                }

                CategoriesFragment.listCategories = listCategories;
                CategoriesFragment.listNames = listNames;
                CategoriesFragment.listDuration = listDuration;
                CategoriesFragment.listRating = listRating;
                CategoriesFragment.listFavourite = listFavourite;
                try {
                    int no = sharedPreferences.getInt("cat", 2);


                        mRvCategories.setAdapter(new CategoryListAdapter(getActivity(), listCategories, listNames, listDuration, listRating, listFavourite, no));
                        mRvCategories.setHasFixedSize(true);
                        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), no);
                        mRvCategories.setLayoutManager(mLayoutManager);


                } catch (InitializationException e) {
                    e.printStackTrace();
                }
                MainActivity.toolbar.setTitle("Top Rated");
            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message.setVisibility(View.INVISIBLE);
                clear();
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
                    CategoriesFragment.message.setVisibility(View.VISIBLE);;
                }

                CategoriesFragment.listCategories = listCategories;
                CategoriesFragment.listNames = listNames;
                CategoriesFragment.listDuration = listDuration;
                CategoriesFragment.listRating = listRating;
                CategoriesFragment.listFavourite = listFavourite;
                try {
                    int no = sharedPreferences.getInt("cat", 2);


                        mRvCategories.setAdapter(new CategoryListAdapter(getActivity(), listCategories, listNames, listDuration, listRating, listFavourite, no));
                        mRvCategories.setHasFixedSize(true);
                        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), no);
                        mRvCategories.setLayoutManager(mLayoutManager);


                } catch (InitializationException e) {
                    e.printStackTrace();
                }

                MainActivity.toolbar.setTitle("Favourite");
            }
        });

        setUpLayouts();
        return mView;
    }

    @Override
    public void setUpLayouts() {

        mRvCategories = (RecyclerView) mView.findViewById(R.id.rv_categories);
//        getCategoriesData(2);
        try {

            int no = sharedPreferences.getInt("cat", 2);

            CategoriesFragment.mRvCategories.setVisibility(View.VISIBLE);


                mRvCategories.setAdapter(new CategoryListAdapter(getActivity(), listCategories, listNames, listDuration, listRating, listFavourite, no));

                mRvCategories.setHasFixedSize(true);
                GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), no);
                mRvCategories.setLayoutManager(mLayoutManager);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setDataInViewLayouts() {
    }

    private void getCategoriesDataOfGalary(int pos) {
        try {


            CategoriesFragment.mRvCategories.setVisibility(View.VISIBLE);

            mRvCategories.setAdapter(new CategoryListAdapterGallery(getActivity(), large_images_url, pos));
            mRvCategories.setHasFixedSize(true);
            GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), pos);
            mRvCategories.setLayoutManager(mLayoutManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCategoriesData(int pos) {
        try {
            CategoriesFragment.mRvCategories.setVisibility(View.VISIBLE);

            int no = sharedPreferences.getInt("cat", 2);

            mRvCategories.setAdapter(new CategoryListAdapter(getActivity(), listCategories, listNames, listDuration, listRating, listFavourite, no));

            mRvCategories.setHasFixedSize(true);
            GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), no);
            mRvCategories.setLayoutManager(mLayoutManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCategoriesDataOfChannels(int pos) {
        try {

            channel_thumb.clear();
            channels_name.clear();

            CategoriesFragment.mRvCategories.setVisibility(View.VISIBLE);
            channels_name.add("Bollywood");
            channel_thumb.add("http://img.youtube.com/vi/aWMTj-rejvc/hqdefault.jpg");
            channels_name.add("English");
            channel_thumb.add("http://img.youtube.com/vi/iS1g8G_njx8/hqdefault.jpg");
            channels_name.add("Punjabi");
            channel_thumb.add("http://img.youtube.com/vi/ojAIYTXU7ZI/hqdefault.jpg");
            channels_name.add("Coke Studio");
            channel_thumb.add("http://img.youtube.com/vi/7w8AR7jnhpc/hqdefault.jpg");


            CategoriesFragment.mRvCategories.setAdapter(new CategoryListAdapterChannels(getActivity(), channels_name, channel_thumb));
//
            MainActivity.toolbar.setTitle("Channels");
            mRvCategories.setHasFixedSize(true);
            GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), pos);
            mRvCategories.setLayoutManager(mLayoutManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        this.menu = menu;

        if (sharedPreferences.getInt("cat", 2) == 2) {
            menu.getItem(0).setTitle(getResources().getString(R.string.list));
            getCategoriesData(1);
            editor.putInt("cat", 1);
            editor.commit();
        } else {
            menu.getItem(0).setTitle(getResources().getString(R.string.grid));

            getCategoriesData(2);
            editor.putInt("cat", 2);
            editor.commit();
        }


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        menu_fab.close(true);
        switch (item.getItemId()) {
            case R.id.action_settings:

                CategoriesFragment.mRvCategories.setVisibility(View.VISIBLE);

                if (menu.getItem(0).getTitle().equals(getResources().getString(R.string.grid))) {
                    menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.list));
                    menu.getItem(0).setTitle(getResources().getString(R.string.list));

                    if (UtilsUI.galery_status) {

                        getCategoriesDataOfGalary(2);
                    } else if (UtilsUI.channels_status) {

                        getCategoriesDataOfChannels(2);
                    } else {
                        getCategoriesData(2);
                    }
                    int cat = 1;
                    editor.putInt("cat", cat);
                    editor.commit();
                } else {
                    menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.grid));
                    menu.getItem(0).setTitle(getResources().getString(R.string.grid));
                    if (UtilsUI.galery_status) {
                        getCategoriesDataOfGalary(1);
                    } else if (UtilsUI.channels_status) {
                        getCategoriesDataOfChannels(1);
                    } else {
                        getCategoriesData(1);
                    }
                    int cat = 2;
                    editor.putInt("cat", cat);
                    editor.commit();
                }
                break;

        }
        return false;


    }

//    private class CategoryListAdapterGallery  extends RecyclerView.Adapter<CategoryListAdapterGallery.ViewHolder> {
//
//        List<String> mImageUrl;
//        List<String> mImageName;
//        Context mContext;
//        SQLiteDatabase db;
//        int i;
//
//        public CategoryListAdapterGallery(Context context, List<String> listCategories,int i) {
//            this.mImageUrl=listCategories;
////            this.mImageName=listCategories;
//this.i=i;
//            this.mContext = context;
//
////         db = SQLiteDatabase.openOrCreateDatabase("YouTubeDB", null);
//        }
//
//
//        public class ViewHolder extends RecyclerView.ViewHolder {
//            ImageView mTvCategoryImg;
//
//            public ViewHolder(View itemView) {
//                super(itemView);
//
//                mTvCategoryImg=(ImageView)itemView.findViewById(R.id.iv_adapter_image);
//
//if(i==1)
//{
//                // Gets linearlayout
//                CardView layout = (CardView)itemView.findViewById(R.id.cv_category_main);
//// Gets the layout params that will allow you to resize the layout
//                ViewGroup.LayoutParams params = layout.getLayoutParams();
//// Changes the height and width to the specified *pixels*
//                params.height = 700;
//                params.width = CardView.LayoutParams.MATCH_PARENT;
//                layout.setLayoutParams(params);
//            }
//                else
//{
//    // Gets linearlayout
//    CardView layout = (CardView)itemView.findViewById(R.id.cv_category_main);
//// Gets the layout params that will allow you to resize the layout
//    ViewGroup.LayoutParams params = layout.getLayoutParams();
//// Changes the height and width to the specified *pixels*
//    params.height = 220;
//    params.width = CardView.LayoutParams.MATCH_PARENT;
//    layout.setLayoutParams(params);
//
//}
//            }
//        }
//
//
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_image, parent, false);
//            ViewHolder vhItem = new ViewHolder(v);
//            return vhItem;
//        }
//
//        @Override
//        public void onBindViewHolder(final ViewHolder holder, final int position) {
//            try {
//
////
//
//
//
//
//
//
////
////                Picasso.with(mContext)
////                        .load(mImageUrl.get(position))
////                        .fit()
////                        .into(holder.mTvCategoryImg);
//
//
//                Glide.with(mContext).load(mImageUrl.get(position))
//                        .thumbnail(0.5f)
//                        .crossFade()
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .into(holder.mTvCategoryImg);
//
//
//
//                holder.mTvCategoryImg.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        Intent intent=new Intent(getActivity(),All_Image_View.class);
//                        intent.putExtra("array", (Serializable) mImageUrl);
//                        intent.putExtra("position",position);
//                        startActivity(intent);
//
//
//
//                    }
//                });
//
//
//
//            }catch (Exception e){
//                e.printStackTrace();
//                Log.e("Exception",""+e);
//
//            }
//        }
//
//        @Override
//        public int getItemCount() {//return array.size
//            return mImageUrl.size();
//        }
//
//        @Override
//        public int getItemViewType(int position) {
//            return position;
//        }
//
//
//    }

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

        CategoriesFragment.message.setVisibility(View.INVISIBLE);

        menu_fab.close(true);

    }

    public void fetchLatestVideos(){
        Log.d("Reading: ", "Reading all contacts..");
//        List<DataYoutubePojo> contacts = db.getAllContacts();

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

        for (DataGalleryPojo cn : mAllImages) {


            large_images_url.add(cn.getImage_link().toString());


        }


    }

}