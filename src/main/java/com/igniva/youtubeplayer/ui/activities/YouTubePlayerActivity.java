package com.igniva.youtubeplayer.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.gson.Gson;
import com.igniva.youtubeplayer.utils.OrientationEnum;
import com.igniva.youtubeplayer.utils.AudioUtil;
import com.igniva.youtubeplayer.utils.StatusBarUtil;
import com.igniva.youtubeplayer.utils.YouTubeApp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class YouTubePlayerActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener,
        YouTubePlayer.OnFullscreenListener,
        YouTubePlayer.PlaylistEventListener,
        YouTubePlayer.PlayerStateChangeListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;

    private static final String META_DATA_NAME = ".YouTubePlayerActivity.ApiKey";

    public static final String EXTRA_VIDEO_ID = "video_id";

    public static final String EXTRA_PLAYER_STYLE = "player_style";

    public static final String EXTRA_ORIENTATION = "orientationEnum";

    public static final String EXTRA_SHOW_AUDIO_UI = "show_audio_ui";

    public static final String EXTRA_HANDLE_ERROR = "handle_error";

    public static final String EXTRA_ANIM_ENTER = "anim_enter";
    public static final String EXTRA_ANIM_EXIT = "anim_exit";

    private String googleApiKey;
    private String videoId;

    private int index_no;

    private YouTubePlayer.PlayerStyle playerStyle;
    private OrientationEnum orientationEnum;
    private boolean showAudioUi;
    private boolean handleError;
    private int animEnter;
    private int animExit;
    Gson gson;
    private YouTubePlayerView playerView;
    private YouTubePlayer player;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private MyPlayerStateChangeListener playerStateChangeListener;
    private MyPlaybackEventListener playbackEventListener;
    public  List<String> listCategories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        gson = new Gson();
        initialize();




        playerView = new YouTubePlayerView(this);
        playerView.initialize(googleApiKey, this);

        addContentView(playerView, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));

        playerView.setBackgroundResource(android.R.color.black);

        StatusBarUtil.hide(this);

        playerStateChangeListener = new MyPlayerStateChangeListener();
        playbackEventListener = new MyPlaybackEventListener();
    }

    private void initialize() {
        listCategories = new ArrayList<String>();
//        listCategories= Arrays.asList("CEFWMP5M6Zk","aWMTj-rejvc","nCD2hj6zJEc","iS1g8G_njx8","vxIj3JKEGvE","9s5l6w-35Wc","zFxo_397aL8","6HiAZTrCf_s","TrupdvVQnpM","i2MbOhBkkf0","dYei_71npF4");

        listCategories=getIntent().getStringArrayListExtra("mListCategories");

        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(),
                    PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            googleApiKey = bundle.getString(META_DATA_NAME);
            Log.e("google api key",googleApiKey);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (googleApiKey == null)
            throw new NullPointerException("Google API key must not be null. Set your api key as meta data in AndroidManifest.xml file.");

//        videoId = getIntent().getStringExtra(EXTRA_VIDEO_ID);

        videoId = getIntent().getStringExtra("VideoId");
        if (videoId == null)
            throw new NullPointerException("Video ID must not be null");

//        playerStyle = (YouTubePlayer.PlayerStyle) getIntent().getSerializableExtra(EXTRA_PLAYER_STYLE);


        String json = sharedPreferences.getString("playerStyle", "DEFAULT");
        playerStyle = gson.fromJson(json, YouTubePlayer.PlayerStyle.class);

        if (playerStyle == null)
            playerStyle = YouTubePlayer.PlayerStyle.DEFAULT;

//        orientationEnum = (OrientationEnum) getIntent().getSerializableExtra(EXTRA_ORIENTATION);


        String orientation_json = sharedPreferences.getString("orientationEnum", "AUTO");
         orientationEnum = gson.fromJson(orientation_json, OrientationEnum.class);

        if (orientationEnum == null)
            orientationEnum = OrientationEnum.AUTO;



//        showAudioUi = getIntent().getBooleanExtra(EXTRA_SHOW_AUDIO_UI, true);

        showAudioUi=sharedPreferences.getBoolean("showAudioUi",true);

        handleError = getIntent().getBooleanExtra(EXTRA_HANDLE_ERROR, true);
//        animEnter = getIntent().getIntExtra(EXTRA_ANIM_ENTER, 0);

        animEnter=sharedPreferences.getInt("anim_enter",0);

//        animExit = getIntent().getIntExtra(EXTRA_ANIM_EXIT, 0);

        animExit=sharedPreferences.getInt("anim_exit",0);

        index_no=getIntent().getIntExtra("index_no", 0);

    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player,
                                        boolean wasRestored) {
        this.player = player;
        player.setOnFullscreenListener(this);
        player.setPlayerStateChangeListener(this);

        switch (orientationEnum) {
            case AUTO:
                player.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION
                        | YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI
                        | YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE
                        | YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
                break;
            case AUTO_START_WITH_LANDSCAPE:
                player.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION
                        | YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI
                        | YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE
                        | YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
                player.setFullscreen(true);
                break;
            case ONLY_LANDSCAPE:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                player.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI
                        | YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
                player.setFullscreen(true);
                break;
            case ONLY_PORTRAIT:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                player.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI
                        | YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
                player.setFullscreen(true);
                break;
        }

        switch (playerStyle) {
            case CHROMELESS:
                player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
                break;
            case MINIMAL:
                player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
                break;
            case DEFAULT:
            default:
                player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                break;
        }

        if (!wasRestored)

        player.loadVideos(listCategories,index_no,index_no);



    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    "There was an error initializing the YouTubePlayer (%1$s)",
                    errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            playerView.initialize(googleApiKey, this);
        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        switch (orientationEnum) {
            case AUTO:
            case AUTO_START_WITH_LANDSCAPE:
                if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    if (player != null)
                        player.setFullscreen(true);
                } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT && player != null) {
                    player.setFullscreen(false);
                }
                break;
            case ONLY_LANDSCAPE:
            case ONLY_PORTRAIT:
                break;
        }
    }

    @SuppressLint("InlinedApi")
    private static final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9
            ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;

    @SuppressLint("InlinedApi")
    private static final int LANDSCAPE_ORIENTATION = Build.VERSION.SDK_INT < 9
            ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;

    @Override
    public void onFullscreen(boolean fullScreen) {
        switch (orientationEnum) {
            case AUTO:
            case AUTO_START_WITH_LANDSCAPE:
                if (fullScreen)
                    setRequestedOrientation(LANDSCAPE_ORIENTATION);
                else
                    setRequestedOrientation(PORTRAIT_ORIENTATION);
                break;
            case ONLY_LANDSCAPE:
            case ONLY_PORTRAIT:
                break;
        }
    }
    @Override
    public void onError(ErrorReason reason) {
        Log.e("onError", "onError : " + reason.name());
        if (handleError && ErrorReason.NOT_PLAYABLE.equals(reason))
            YouTubeApp.startVideo(this, videoId);
    }

    @Override
    public void onAdStarted() {
    }

    @Override
    public void onLoaded(String videoId) {
    }

    @Override
    public void onLoading() {
    }

    @Override
    public void onVideoEnded() {
    }

    @Override
    public void onVideoStarted() {
        StatusBarUtil.hide(this);
    }

    // Audio Managing
    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            AudioUtil.adjustMusicVolume(getApplicationContext(), true, showAudioUi);
            StatusBarUtil.hide(this);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            AudioUtil.adjustMusicVolume(getApplicationContext(), false, showAudioUi);
            StatusBarUtil.hide(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // Animation
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (animEnter != 0 && animExit != 0)
            overridePendingTransition(animEnter, animExit);
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPrevious() {

    }

    @Override
    public void onNext() {}

    @Override
    public void onPlaylistEnded() {
        showMessage("List Ended");
    }


    private final class MyPlaylistEventListener implements YouTubePlayer.PlaylistEventListener {


        @Override
        public void onPrevious() {

        }

        @Override
        public void onNext() {

        }

        @Override
        public void onPlaylistEnded() {

            showMessage("List Ended");
        }
    }

    private final class MyPlaybackEventListener implements YouTubePlayer.PlaybackEventListener {

        @Override
        public void onPlaying() {
            // Called when playback starts, either due to user action or call to play().
            showMessage("Playing");
        }

        @Override
        public void onPaused() {
            // Called when playback is paused, either due to user action or call to pause().
            showMessage("Paused");
        }

        @Override
        public void onStopped() {
            // Called when playback stops for a reason other than being paused.
            showMessage("Stopped");
        }

        @Override
        public void onBuffering(boolean b) {
            // Called when buffering starts or ends.
        }

        @Override
        public void onSeekTo(int i) {
            // Called when a jump in playback position occurs, either
            // due to user scrubbing or call to seekRelativeMillis() or seekToMillis()
        }
    }

    private final class MyPlayerStateChangeListener implements YouTubePlayer.PlayerStateChangeListener {

        @Override
        public void onLoading() {
            // Called when the player is loading a video
            // At this point, it's not ready to accept commands affecting playback such as play() or pause()
            Log.e("Video Loading","Video Loading");
        }

        @Override
        public void onLoaded(String s) {
            // Called when a video is done loading.
            // Playback methods such as play(), pause() or seekToMillis(int) may be called after this callback.
            Log.e("Video Loaded","Video Loaded");
        }

        @Override
        public void onAdStarted() {
            // Called when playback of an advertisement starts.
        }

        @Override
        public void onVideoStarted() {
            // Called when playback of the video starts.
            Log.e("Video Started","Video Started");
        }

        @Override
        public void onVideoEnded() {
            // Called when the video reaches its end.
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
            // Called when an error occurs.
        }
    }

}
