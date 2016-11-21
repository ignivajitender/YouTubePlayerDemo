package com.igniva.youtubeplayer.ui.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.igniva.youtubeplayer.R;
import com.igniva.youtubeplayer.db.DatabaseHandler;
import com.igniva.youtubeplayer.model.DataGalleryPojo;
import com.igniva.youtubeplayer.model.DataYoutubePojo;
import com.igniva.youtubeplayer.utils.Constants;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by igniva-php-08 on 18/7/16.
 */
public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    String s;
    DatabaseHandler db;
    List<DataYoutubePojo> mAllData;
    List<DataGalleryPojo> mAllImages;
    public static ArrayList<String> listCategories, listDuration, listNames, listRating, listFavourite;
    AsyncTask<Void, Void, Void> execute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AsyncTask<Void, Void, Void> execute = new CreateDatabaseAsyncTask();

        execute.execute();


//        listCategories = new ArrayList<String>();
//        listDuration = new ArrayList<String>();
//        listNames = new ArrayList<String>();
//        listRating = new ArrayList<String>();
//        listFavourite=new ArrayList<String>();


//        db = openOrCreateDatabase("YouTubeDB", Context.MODE_PRIVATE, null);
//        db.execSQL("CREATE TABLE IF NOT EXISTS youtubeData( video_no INTEGER PRIMARY KEY,video_title VARCHAR,video_id VARCHAR, video_channel VARCHAR,video_duration VARCHAR,video_rating VARCHAR,video_thumbs VARCHAR,video_playlist VARCHAR,video_order VARCHAR,video_favourite VARCHAR);");
//
//        if(b) {
//
//            Toast.makeText(SplashActivity.this, "b "+b, Toast.LENGTH_SHORT).show();
//            db.execSQL("INSERT INTO youtubeData VALUES(1,'Android N Developer Preview Review!','CEFWMP5M6Zk','Bollywood','09:09','3','http://img.youtube.com/vi/CEFWMP5M6Zk/hqdefault.jpg','','','');");
//            db.execSQL("INSERT INTO youtubeData VALUES(2,'Baby Ko Bass Pasand Hai Song| Sultan | Salman Khan | Anushka Sharma | Vishal | Badshah | Shalmali','aWMTj-rejvc','Bollywood','02:35','4','http://img.youtube.com/vi/aWMTj-rejvc/hqdefault.jpg','','','');");
//            db.execSQL("INSERT INTO youtubeData VALUES(3,'Dheere Dheere Se Meri Zindagi Video Song (OFFICIAL) Hrithik Roshan, Sonam Kapoor | Yo Yo Honey Singh','nCD2hj6zJEc','Bollywood','05:04','1','http://img.youtube.com/vi/nCD2hj6zJEc/hqdefault.jpg','','','');");
//            db.execSQL("INSERT INTO youtubeData VALUES(4,' Ariana Grande - Problem ft. Iggy Azalea ','iS1g8G_njx8','Hollywood','03:27','5','http://img.youtube.com/vi/iS1g8G_njx8/hqdefault.jpg','','','');");
//            db.execSQL("INSERT INTO youtubeData VALUES(5,'Top 6 Hindi Video Songs 2016 ','vxIj3JKEGvE','Bollywood','20:32','2','http://img.youtube.com/vi/vxIj3JKEGvE/hqdefault.jpg','','','');");
//            db.execSQL("INSERT INTO youtubeData VALUES(6,'Best of ARIJIT SINGH Romantic songs with Lyrics Part 1','9s5l6w-35Wc','Bollywood','54:57','3','http://img.youtube.com/vi/9s5l6w-35Wc/hqdefault.jpg','','','');");
//            db.execSQL("INSERT INTO youtubeData VALUES(7,'Ek Villain Full Songs Audio Jukebox | Sidharth Malhotra | Shraddha Kapoor ','zFxo_397aL8','Bollywood','31:21','1','http://img.youtube.com/vi/zFxo_397aL8/hqdefault.jpg','','','');");
//            db.execSQL("INSERT INTO youtubeData VALUES(8,'BEST OF EMRAN HASHMI','6HiAZTrCf_s','Bollywood','1:03:19','5','http://img.youtube.com/vi/6HiAZTrCf_s/hqdefault.jpg','','','');");
//            db.execSQL("INSERT INTO youtubeData VALUES(9,'Tu hi meri shab hai - Gangster','TrupdvVQnpM','Bollywood','06:37','3','http://img.youtube.com/vi/TrupdvVQnpM/hqdefault.jpg','','','');");
//            db.execSQL("INSERT INTO youtubeData VALUES(10,'Agar Tum Mil Jao - Zeher (HD)','i2MbOhBkkf0','Bollywood','04:33','2','http://img.youtube.com/vi/i2MbOhBkkf0/hqdefault.jpg','','','');");
//            db.execSQL("INSERT INTO youtubeData VALUES(11,'Lamha Lamha [Full Song] Gangster- A Love Story ','dYei_71npF4','Bollywood','05:04','5','http://img.youtube.com/vi/dYei_71npF4/hqdefault.jpg','','','');");
//            editor.putBoolean("b",false);
//            editor.commit();
//        }
//        Cursor c=db.rawQuery("SELECT * FROM youtubeData ", null);
//        while(c.moveToNext())
//        {
//            c.getString(0);
//            listNames.add(c.getString(1));
//            listCategories.add(c.getString(2));
//            c.getString(3);
//            listDuration.add(c.getString(4));
//            listRating.add(c.getString(5));
//            c.getString(6);
//            c.getString(7);
//            c.getString(8);
//            listFavourite.add(c.getString(9));
//            Log.e("c.getString",""+c.getString(0)+c.getString(1)+c.getString(2)+c.getString(3)+c.getString(4)+c.getString(5)+c.getString(6)+c.getString(7)+c.getString(8)+c.getString(9));
//        }
//
//        db.close();

    }

    public class CreateDatabaseAsyncTask
    extends AsyncTask<Void, Void, Void>

    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {



            db=new DatabaseHandler(SplashActivity.this);

            try {
                db.createDataBase();
            } catch (Exception e) {
                e.printStackTrace();
            }

            db.openDataBase();

            mAllData=db.getAllContacts();

            mAllImages=db.getAllImages();

            for (DataGalleryPojo cn : mAllImages) {
                String log = "Image_index_no: " + cn.getImage_no() + " , Image_link: " + cn.getImage_link();

                Log.e("Images_Splash: ", log);
            }

            for (DataYoutubePojo cn : mAllData) {
                String log = "video_no: " + cn.getVideo_no() + " , Video_Title: " + cn.getVideo_title()+" Video_id: "+cn.getVideo_id()+"Video_channel"+cn.getVideo_channel() +
                        " ,Duration: " + cn.getVideo_duration()+" Rating: " + cn.getVideo_rating()+" Thumb: " + cn.getVideo_thumb()+" Playlist: " + cn.getVideo_playlist()+
                        " order: " + cn.getVideo_order()+" Favourite= "+cn.getVideo_favourite();
//
//                listCategories.add(cn.getVideo_id().toString());
//                listNames.add(cn.getVideo_title().toString());
//                listDuration.add(cn.getVideo_duration().toString());
//                listRating.add(cn.getVideo_rating().toString());
//                listFavourite.add(cn.getVideo_favourite());


                // Writing Contacts to log
                Log.e("Name_Splash: ", log);

            }

            db.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class).putExtra(Constants.ALL_DATA,(Serializable) mAllData).putExtra(Constants.ALL_IMAGES,(Serializable) mAllImages);
            startActivity(mainIntent);
            finish();

        }
    }


}
