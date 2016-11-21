package com.igniva.youtubeplayer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.igniva.youtubeplayer.BuildConfig;
import com.igniva.youtubeplayer.model.DataGalleryPojo;
import com.igniva.youtubeplayer.model.DataYoutubePojo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLData;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by igniva-andriod-03 on 31/8/16.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    SQLiteDatabase myDataBase;
//    /data/data/<application_package_name>/databases/<database_file_name>

//    context.getDatabasePath(name_of_database_file);

    private static final String DATABASE_PATH = "/data/data/"+ BuildConfig.APPLICATION_ID+"/databases/";
//    + BuildConfig.APPLICATION_ID+  //  com.igniva.youtubeplayer
    // Database Name
    private static final String DATABASE_NAME = "youtubeManager.db";

    // Contacts table name
    private static final String TABLE_YOUTUBE = "youtube";
    private static final String TABLE_GALLERY = "gallery_data";

    // Contacts Table Columns names
    private static final String KEY_VIDEO_NO = "video_no";
    private static final String KEY_VIDEO_ID = "video_id";
    private static final String KEY_VIDEO_TITLE = "video_title";
    private static final String KEY_VIDEO_CHANNEL = "video_Channel";
    private static final String KEY_VIDEO_DURATION = "video_duration";
    private static final String KEY_VIDEO_RATING = "video_rating";
    private static final String KEY_VIDEO_THUMB = "video_thumb";
    private static final String KEY_VIDEO_PLAYLIST = "video_playlist";
    private static final String KEY_VIDEO_ORDER = "video_order";
    private static final String KEY_VIDEO_FAVOURITE = "video_favourite";

    private static final String KEY_IMAGE_NO = "index_no";
    private static final String KEY_IMAGE_LINK = "image_link";

    Context mContext;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext=context;

    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
//        String CREATE_YOUTUBE_TABLE = "CREATE TABLE " + TABLE_YOUTUBE + "("
//                + KEY_VIDEO_NO + " INTEGER PRIMARY KEY AUTOINCREMENT ,"  + KEY_VIDEO_TITLE + " TEXT," + KEY_VIDEO_ID + " TEXT,"
//                + KEY_VIDEO_CHANNEL + " TEXT," + KEY_VIDEO_DURATION + " TEXT," + KEY_VIDEO_RATING + " INTEGER,"
//                + KEY_VIDEO_THUMB + " TEXT," + KEY_VIDEO_PLAYLIST + " TEXT," + KEY_VIDEO_ORDER + " TEXT, "
//                + KEY_VIDEO_FAVOURITE + " TEXT" + ");" ;
//        db.execSQL(CREATE_YOUTUBE_TABLE);
//        Log.e("onCreate-","Table Created");
    }

    /** This method open database for operations **/
    public boolean openDataBase() throws SQLException {
        String mPath = DATABASE_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(mPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        return myDataBase.isOpen();
    }
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        // Drop older table if existed
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_YOUTUBE);
//        // Create tables again
//        onCreate(db);
    }

    /**
     * This method will create database in application package /databases
     * directory when first time application launched
     **/
    public void createDataBase() throws IOException {
        boolean mDataBaseExist = checkDataBase();
        if (!mDataBaseExist) {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException mIOException) {
                mIOException.printStackTrace();
                throw new Error("Error copying database");
            } finally {
                this.close();
            }
        }
    }

    /** This method checks whether database is exists or not **/
    private boolean checkDataBase() {
        try {
            final String mPath = DATABASE_PATH + DATABASE_NAME;
            final File file = new File(mPath);
            if (file.exists())
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method will copy database from /assets directory to application
     * package /databases directory
     **/
    private void copyDataBase() throws IOException {
        try {

            InputStream mInputStream = mContext.getAssets().open(DATABASE_NAME);
            String outFileName = DATABASE_PATH + DATABASE_NAME;
            OutputStream mOutputStream = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = mInputStream.read(buffer)) > 0) {
                mOutputStream.write(buffer, 0, length);
            }
            mOutputStream.flush();
            mOutputStream.close();
            mInputStream.close();

            Toast.makeText(mContext, "Database Copied", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }






    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void addContact(DataYoutubePojo contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_VIDEO_NO, contact.getVideo_no());
        values.put(KEY_VIDEO_ID, contact.getVideo_id());
        values.put(KEY_VIDEO_TITLE, contact.getVideo_title());
        values.put(KEY_VIDEO_CHANNEL, contact.getVideo_channel());
        values.put(KEY_VIDEO_DURATION, contact.getVideo_duration());
        values.put(KEY_VIDEO_RATING, contact.getVideo_rating());
        values.put(KEY_VIDEO_THUMB, contact.getVideo_thumb());
        values.put(KEY_VIDEO_PLAYLIST, contact.getVideo_playlist());
        values.put(KEY_VIDEO_ORDER, contact.getVideo_order());
        values.put(KEY_VIDEO_FAVOURITE, contact.getVideo_favourite());


        // Inserting Row
        db.insert(TABLE_YOUTUBE, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
//    public DataYoutubePojo getContact(String id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_YOUTUBE, new String[] { KEY_VIDEO_NO,
//                        KEY_VIDEO_TITLE,KEY_VIDEO_ID,KEY_VIDEO_CHANNEL,KEY_VIDEO_DURATION,KEY_VIDEO_RATING,KEY_VIDEO_THUMB,KEY_VIDEO_PLAYLIST,KEY_VIDEO_ORDER,KEY_VIDEO_FAVOURITE }, KEY_VIDEO_ID + "=?",
//                new String[] { String.valueOf(id) }, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        DataYoutubePojo contact = new DataYoutubePojo(cursor.getString(0),
//                cursor.getString(1), cursor.getString(2),cursor.getString(3),
//                cursor.getString(4),cursor.getString(5),cursor.getString(6),
//                cursor.getString(7),cursor.getString(8),cursor.getString(9));
//        // return contact
//        return contact;
//    }


    // Getting All Contacts
    public List<DataGalleryPojo> getAllImages() {
        List<DataGalleryPojo> contactList = new ArrayList<DataGalleryPojo>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_GALLERY;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataGalleryPojo contact = new DataGalleryPojo();
                contact.setImage_no(cursor.getString(0));
                contact.setImage_link(cursor.getString(1));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }


    // Getting All Contacts
    public List<DataYoutubePojo> getAllContacts() {
        List<DataYoutubePojo> contactList = new ArrayList<DataYoutubePojo>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_YOUTUBE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataYoutubePojo contact = new DataYoutubePojo();
                contact.setVideo_no(cursor.getString(0));
                contact.setVideo_id(cursor.getString(1));
                contact.setVideo_title(cursor.getString(2));
                contact.setVideo_channel(cursor.getString(3));
                contact.setVideo_duration(cursor.getString(4));
                contact.setVideo_rating(cursor.getInt(5));
                contact.setVideo_thumb(cursor.getString(6));
                contact.setVideo_playlist(cursor.getString(7));
                contact.setVideo_order(cursor.getString(8));
                contact.setVideo_favourite(cursor.getString(9));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // Getting All Contacts order by
    public List<DataYoutubePojo> getAllContactstoprated() {
        List<DataYoutubePojo> contactList = new ArrayList<DataYoutubePojo>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_YOUTUBE+" ORDER BY "+KEY_VIDEO_RATING+" DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataYoutubePojo contact = new DataYoutubePojo();
                contact.setVideo_no(cursor.getString(0));
                contact.setVideo_id(cursor.getString(1));
                contact.setVideo_title(cursor.getString(2));
                contact.setVideo_channel(cursor.getString(3));
                contact.setVideo_duration(cursor.getString(4));
                contact.setVideo_rating(cursor.getInt(5));
                contact.setVideo_thumb(cursor.getString(6));
                contact.setVideo_playlist(cursor.getString(7));
                contact.setVideo_order(cursor.getString(8));
                contact.setVideo_favourite(cursor.getString(9));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }



    // Updating single contact
    public int updateContact(DataYoutubePojo contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_VIDEO_FAVOURITE, contact.getVideo_favourite());
        // updating row
        return db.update(TABLE_YOUTUBE, values, KEY_VIDEO_ID + " = ?",
                new String[] { String.valueOf(contact.getVideo_id()) });
    }

    // Deleting single contact
    public void deleteContact(DataYoutubePojo contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_YOUTUBE, KEY_VIDEO_ID + " = ?",
                new String[] { String.valueOf(contact.getVideo_id()) });
        db.close();
    }


    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_YOUTUBE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}