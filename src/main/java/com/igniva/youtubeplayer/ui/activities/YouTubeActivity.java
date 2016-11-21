package com.igniva.youtubeplayer.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.gson.Gson;
import com.igniva.youtubeplayer.R;
import com.igniva.youtubeplayer.utils.OrientationEnum;


import butterknife.Bind;


public class YouTubeActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.player_style_bt)
    View playerStyleBt;
    @Bind(R.id.player_style_tv)
    TextView playerStyleTv;
    @Bind(R.id.screen_orientation_bt)
    View screenOrientationBt;
    @Bind(R.id.screen_orientation_tv)
    TextView screenOrientationTv;
    @Bind(R.id.volume_bt)
    View volumeBt;
    @Bind(R.id.volume_tv)
    TextView volumeTv;
    @Bind(R.id.animation_bt)
    View animationBt;
    @Bind(R.id.animation_tv)
    TextView animationTv;
    Gson gson;
    YouTubePlayer.PlayerStyle playerStyle;
    OrientationEnum orientationEnum;
    boolean showAudioUi;
    boolean showFadeAnim;
    private boolean advertised = false;

    private static String VIDEO_ID = "";

    private Context context;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Settings");


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do something you want

                onBackPressed();
                overridePendingTransition(R.anim.right_in, R.anim.right_out);

            }
        });

        gson = new Gson();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        playerStyle = YouTubePlayer.PlayerStyle.DEFAULT;
        orientationEnum = OrientationEnum.AUTO;
        showAudioUi = true;
        showFadeAnim = true;

        playerStyleBt=(View) findViewById(R.id.player_style_bt);
        playerStyleTv=(TextView)findViewById(R.id.player_style_tv);
        screenOrientationBt=(View) findViewById(R.id.screen_orientation_bt);
        screenOrientationTv=(TextView)findViewById(R.id.screen_orientation_tv);
        volumeBt=(View) findViewById(R.id.volume_bt);
        volumeTv=(TextView)findViewById(R.id.volume_tv);
        animationBt=(View) findViewById(R.id.animation_bt);
        animationTv=(TextView)findViewById(R.id.animation_tv);

        String json = sharedPreferences.getString("playerStyle", "DEFAULT");
        playerStyle = gson.fromJson(json, YouTubePlayer.PlayerStyle.class);
        playerStyleTv.setText(playerStyle.name());


        String orientation_json = sharedPreferences.getString("orientationEnum", "AUTO");
        orientationEnum = gson.fromJson(orientation_json, OrientationEnum.class);
        screenOrientationTv.setText(orientationEnum.name());

        String volumeTv_text =sharedPreferences.getString("volumeTv","SHOW");
        volumeTv.setText(volumeTv_text+"");

        showFadeAnim=sharedPreferences.getBoolean("showFadeAnim",true);
        animationTv.setText(sharedPreferences.getString("animationTv","FADE"));

        playerStyleBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(YouTubeActivity.this)
                        .title(getString(R.string.player_style))
                        .items(getPlayerStyleNames())
                        .itemsCallbackSingleChoice(playerStyle.ordinal(), new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog materialDialog, View view, int which, CharSequence charSequence) {
                                playerStyle = YouTubePlayer.PlayerStyle.values()[which];

                                String json = gson.toJson(playerStyle); // myObject - instance of MyObject
                                editor.putString("playerStyle", json);
                                editor.commit();

                                playerStyleTv.setText(playerStyle.name());
                                return true;
                            }
                        })
                        .positiveText(getString(R.string.choose))
                        .show();
            }
        });

        screenOrientationBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(YouTubeActivity.this)
                        .title(getString(R.string.screen_orientation))
                        .items(getScreenOrientationNames())
                        .itemsCallbackSingleChoice(orientationEnum.ordinal(), new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog materialDialog, View view, int which, CharSequence charSequence) {
                                orientationEnum = OrientationEnum.values()[which];

                                String json = gson.toJson(orientationEnum); // myObject - instance of MyObject
                                editor.putString("orientationEnum", json);
                                editor.commit();

                                screenOrientationTv.setText(orientationEnum.name());
                                return true;
                            }
                        })
                        .positiveText(getString(R.string.choose))
                        .show();
            }
        });

        volumeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(YouTubeActivity.this)
                        .title(getString(R.string.volume_ui_control))
                        .items(new String[]{getString(R.string.show), getString(R.string.dont_show)})
                        .itemsCallbackSingleChoice(showAudioUi ? 0 : 1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog materialDialog, View view, int which, CharSequence charSequence) {
                                showAudioUi = which == 0;
                                volumeTv.setText(showAudioUi ? getString(R.string.show) : getString(R.string.dont_show));
                                editor.putBoolean("showAudioUi",showAudioUi);
                                editor.putString("volumeTv",volumeTv.getText().toString());
                                editor.commit();

                                return true;
                            }
                        })
                        .positiveText(getString(R.string.choose))
                        .show();
            }
        });

        animationBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(YouTubeActivity.this)
                        .title(getString(R.string.animation_on_close))
                        .items(new String[]{getString(R.string.fade), getString(R.string.modal)})
                        .itemsCallbackSingleChoice(showFadeAnim ? 0 : 1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog materialDialog, View view, int which, CharSequence charSequence) {
                                showFadeAnim = which == 0;
                                animationTv.setText(showFadeAnim ? getString(R.string.fade) : getString(R.string.modal));
                                editor.putBoolean("showFadeAnim",showFadeAnim);
                                editor.putString("animationTv",animationTv.getText().toString());

                                if (showFadeAnim) {
                                    editor.putInt("anim_enter",R.anim.fade_in);
                                    editor.putInt("anim_exit",R.anim.fade_out);

                } else {
                                    editor.putInt("anim_enter",R.anim.modal_close_enter);
                                    editor.putInt("anim_exit",R.anim.modal_close_exit);
                                }

                                editor.commit();


                                return true;
                            }
                        })
                        .positiveText(getString(R.string.choose))
                        .show();
            }
        });
    }

    private String[] getScreenOrientationNames() {
        OrientationEnum[] states = OrientationEnum.values();
        String[] names = new String[states.length];
        for (int i = 0; i < states.length; i++)
            names[i] = states[i].name();
        return names;
    }

    private String[] getPlayerStyleNames() {
        YouTubePlayer.PlayerStyle[] states = YouTubePlayer.PlayerStyle.values();
        String[] names = new String[states.length];
        for (int i = 0; i < states.length; i++)
            names[i] = states[i].name();
        return names;
    }


}
