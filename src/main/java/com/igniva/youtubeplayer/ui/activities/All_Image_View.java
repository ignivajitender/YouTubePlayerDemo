package com.igniva.youtubeplayer.ui.activities;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.igniva.youtubeplayer.R;
import com.igniva.youtubeplayer.controller.BasicImageDownloader;
import com.igniva.youtubeplayer.libs.FloatingActionButton;
import com.igniva.youtubeplayer.subscaleview.ImageSource;
import com.igniva.youtubeplayer.subscaleview.SubsamplingScaleImageView;
import com.igniva.youtubeplayer.ui.application.MyApplication;
import com.igniva.youtubeplayer.utils.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.igniva.youtubeplayer.ui.activities.MainActivity.TRACK_LOG;


/**
 * Created by igniva-andriod-03 on 2/9/16.
 */
public class All_Image_View extends AppCompatActivity {

    private static final String TAG = "saving issue";
    private static final String BUTTON_CLICK_EVENT = "Click Event";
    List<String> image_list;
    image_adapter image_adapter;
    ViewPager viewPager;
    int position_previous;
    private Bitmap theBitmap = null;
    public static int sPosition;
    public static List<String> wallpaper_list = new ArrayList<>();

    CircularProgressView progressWheel;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    private File mFile = null;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.all_image_screen);
        progressWheel = (CircularProgressView) findViewById(R.id.progressBar);


        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);

        viewPager = (ViewPager) findViewById(R.id.img);
        image_list = new ArrayList();

// inside onCreate of Activity or Fragment
        Intent intent = getIntent();

        image_list = intent.getStringArrayListExtra("array");
        position_previous = intent.getIntExtra("position", 0);

        image_adapter = new image_adapter(this, image_list);
        viewPager.setAdapter(image_adapter);
        viewPager.setCurrentItem(position_previous);

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.getInstance().trackEvent("Gallary View","Set Wallpaper ",BUTTON_CLICK_EVENT);
                progressWheel.setVisibility(View.VISIBLE);
                progressWheel.startAnimation();

                setbackdroundImage(viewPager.getCurrentItem());

            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyApplication.getInstance().trackEvent("Gallary View","Save Image ",BUTTON_CLICK_EVENT);


                progressWheel.setVisibility(View.VISIBLE);
                progressWheel.startAnimation();

                final int data = viewPager.getCurrentItem();

                String dirPath = getFilesDir().getAbsolutePath() + File.separator + "Wallpapers";

                File projDir = new File(dirPath);

                if (!projDir.exists())
                    projDir.mkdirs();

//                            File f = new File(Environment.getExternalStorageDirectory() +"/Wallpapers/"+ File.separator +imageName);

                // create a File object for the parent directory
                File wallpaperDirectory = new File("/" + Environment.getExternalStorageDirectory() + "/Wallpapers/");

// have the object build the directory structure, if needed.

                wallpaperDirectory.mkdirs();

// create a File object for the output file



                final String imageUrl = getImageLink(wallpaper_list.get(data).toString());

                String imageName = null;

                String filename = null;


                if(!imageUrl.contains("drive.google.com")) {

                    filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.lastIndexOf("."));

                    imageName = "Wallpaper_" + filename + ".jpg";

                    mFile = new File(wallpaperDirectory, imageName);

                }else {

                    filename = imageUrl.substring(imageUrl.indexOf("="),imageUrl.length()-1);

                    imageName = "Wallpaper_" + filename + ".jpg";

                    mFile = new File(wallpaperDirectory, imageName);
                }

                mFile = new File(projDir, imageName);

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                       // Looper.prepare();
                        try {

                            theBitmap = Glide.
                                    with(All_Image_View.this).
                                    load(imageUrl).
                                    asBitmap().
                                    into(-1, -1).
                                    get();
                        } catch (final ExecutionException e) {
                           Log.e(TAG, e.getMessage());
                        } catch (final InterruptedException e) {Log.e(TAG, e.getMessage());
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void dummy) {
                        if (null != theBitmap) {
                            // The full bitmap should be available here

                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                            theBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);



                            try {
                            //   f.createNewFile();
                                FileOutputStream fo = new FileOutputStream(mFile);
                                fo.write(bytes.toByteArray());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            progressWheel.stopAnimation();
                            progressWheel.setVisibility(View.GONE);

                            Toast.makeText(All_Image_View.this, "Image saved successfully", Toast.LENGTH_SHORT).show();

                        }
                    }
                }.execute();


            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyApplication.getInstance().trackEvent("Gallary View","Share Image ",BUTTON_CLICK_EVENT);

                progressWheel.setVisibility(View.VISIBLE);
                progressWheel.startAnimation();

                final int data = viewPager.getCurrentItem();

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        Looper.prepare();
                        try {
                            theBitmap = Glide.
                                    with(All_Image_View.this).
                                    load(wallpaper_list.get(data).toString()).
                                    asBitmap().
                                    into(-1, -1).
                                    get();
                        } catch (final ExecutionException e) {

                            MyApplication.getInstance().trackEvent(TRACK_LOG,"crash",e.getMessage());

//                            Log.e(TAG, e.getMessage());
                        } catch (final InterruptedException e) {

                            MyApplication.getInstance().trackEvent(TRACK_LOG,"crash",e.getMessage());

//                            Log.e(TAG, e.getMessage());
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void dummy) {
                        if (null != theBitmap) {
                            // The full bitmap should be available here

                            String imageName = "temp_" + System.currentTimeMillis() + ".jpg";
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("image/jpeg");
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            theBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                            File wallpaperDirectory = new File("/" + Environment.getExternalStorageDirectory() + "/Temp/");
// have the object build the directory structure, if needed.
                            wallpaperDirectory.mkdirs();
// create a File object for the output file
                            File f = new File(wallpaperDirectory, imageName);


                            try {
                                f.createNewFile();
                                FileOutputStream fo = new FileOutputStream(f);
                                fo.write(bytes.toByteArray());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            progressWheel.stopAnimation();
                            progressWheel.setVisibility(View.GONE);


                            share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"));
                            startActivity(Intent.createChooser(share, "Share Image"));


                        }
                        ;
                    }
                }.execute();


            }
        });


 // client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();
       // client.connect();

    }

    @Override
    public void onStop() {
        super.onStop();
      //  client.disconnect();
    }

    public String getImageLink(String imageUrl) {

        if(imageUrl.contains(Constants.DRIVE_STRING)){

            String[] splitLinkArray=imageUrl.split("/");

            String imageLink=Constants.DRIVE_URL+splitLinkArray[5];

            return imageLink;

        }

        return imageUrl;
    }

    public class image_adapter extends PagerAdapter {
        LayoutInflater mLayoutInflater;

        List arrayList;
        Context context;

        public image_adapter(All_Image_View all_image_view, List image_list) {
            context = all_image_view;
            arrayList = image_list;

        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            LayoutInflater inflater = LayoutInflater.from(context);
            View layout = (View) inflater.inflate(R.layout.image_fullscreen_preview,
                    container, false);

            final SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) layout.findViewById(R.id.image_preview);
//            imageView.setZoomEnabled(true);

            sPosition = position;
            wallpaper_list = arrayList;

            String dirPath = getFilesDir().getAbsolutePath() + File.separator + "Wallpapers";

            File wallpaperDirectory = new File(dirPath);

            if (!wallpaperDirectory.exists())

                wallpaperDirectory.mkdirs();

            String imageUrl = getImageLink(wallpaper_list.get(position).toString());

            String imageName = null;

            String filename = null;

            File f = null;

            if(!imageUrl.contains(Constants.DRIVE_STRING)) {

                 filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.lastIndexOf("."));

                 imageName = "Wallpaper_" + filename + ".jpg";

                 f = new File(wallpaperDirectory, imageName);

            }else {

                filename = imageUrl.substring(imageUrl.indexOf("="),imageUrl.length()-1);

                imageName = "Wallpaper_" + filename + ".jpg";

                f = new File(wallpaperDirectory, imageName);
            }



                if (f.exists()) {

                    Toast.makeText(getApplicationContext(), "File Already Exits", Toast.LENGTH_LONG).show();

                    BitmapFactory.Options options = new BitmapFactory.Options();

                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;

                    Bitmap bitmap = BitmapFactory.decodeFile(dirPath + "/" + "Wallpaper_" + filename + ".jpg", options);

                    imageView.setImage(ImageSource.bitmap(bitmap));

                    progressWheel.setVisibility(View.GONE);

                    progressWheel.stopAnimation();

                } else {

                Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.loading);

                imageView.setImage(ImageSource.bitmap(largeIcon));


                imageUrl = arrayList.get(position).toString();

                String originalImage = getImageLink(imageUrl);

                //you have to get the part of the link 0B9nFwumYtUw9Q05WNlhlM2lqNzQ

                //Create the new image link


              //  Picasso.with(YourActivity.this).load(imageLink).into(imageView);

                Glide.with(context)
                        .load(originalImage)
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                                progressWheel.setVisibility(View.GONE);
                                progressWheel.stopAnimation();
                                imageView.setImage(ImageSource.bitmap(bitmap));

                            }

                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);
                                Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.no_preview);

                                imageView.setImage(ImageSource.bitmap(largeIcon));


                            //    Toast.makeText(context, "Image loading failed", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onLoadStarted(Drawable placeholder) {
                                super.onLoadStarted(placeholder);
                                progressWheel.setVisibility(View.GONE);
                                progressWheel.stopAnimation();
                            }


                        });

            }



            imageView.setMaxScale(10);
//            imageView.setZoomEnabled(true);

            imageView.setDoubleTapZoomScale(6);
            imageView.setDoubleTapZoomStyle(1);


            container.addView(layout);

            return layout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private void setbackdroundImage(int data) {

        final BasicImageDownloader downloader = new BasicImageDownloader(new BasicImageDownloader.OnImageLoaderListener() {
            @Override
            public void onError(BasicImageDownloader.ImageError error) {
                Toast.makeText(All_Image_View.this, " Error code " + error.getErrorCode() + ": " +
                        error.getMessage(), Toast.LENGTH_LONG).show();
                error.printStackTrace();

                progressWheel.stopAnimation();
                progressWheel.setVisibility(View.GONE);
            }

            @Override
            public void onProgressChange(int percent) {
            }

            @Override
            public void onComplete(Bitmap result) {
                        /* save the image - I'm gonna use JPEG */
                final Bitmap.CompressFormat mFormat = Bitmap.CompressFormat.JPEG;
                        /* don't forget to include the extension into the file name */
                final File myImageFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                        File.separator + "image_" + System.currentTimeMillis() + ".jpg");
                BasicImageDownloader.writeToDisk(myImageFile, result, new BasicImageDownloader.OnBitmapSaveListener() {
                    @Override
                    public void onBitmapSaved() {

                        WallpaperManager myWallpaperManager
                                = WallpaperManager.getInstance(getApplicationContext());


                        progressWheel.stopAnimation();
                        progressWheel.setVisibility(View.GONE);


                        Toast.makeText(All_Image_View.this, "Wallpaper sets successfully", Toast.LENGTH_LONG).show();

                        try {
                            Bitmap decodedSampleBitmap = BitmapFactory.decodeFile(myImageFile.getAbsolutePath());
                            myWallpaperManager.setBitmap(decodedSampleBitmap);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();

                            MyApplication.getInstance().trackEvent(TRACK_LOG,"crash",e.getMessage());

                        }


                    }

                    @Override
                    public void onBitmapSaveError(BasicImageDownloader.ImageError error) {
                        Toast.makeText(All_Image_View.this, "Error code " + error.getErrorCode() + ": " +
                                error.getMessage(), Toast.LENGTH_LONG).show();

                        progressWheel.stopAnimation();
                        progressWheel.setVisibility(View.GONE);
                        error.printStackTrace();
                    }


                }, mFormat, false);

            }
        });
        downloader.download(wallpaper_list.get(data).toString(), true);


    }




}