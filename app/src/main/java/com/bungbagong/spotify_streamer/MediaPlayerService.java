package com.bungbagong.spotify_streamer;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private final IBinder binder = new MediaPlayerBinder();
    private MediaPlayer mMediaPlayer;
    private String previewUrl;
    public static final String PREVIEW_URL = "PREV_URL";
    public int progress;
    private boolean isSongCompleted;
    private boolean isSongPlaying;
    private Handler progressHandler;



    public int getProgress() {
        return progress;
    }

    public boolean isSongPlaying() {
        return isSongPlaying;
    }

    public boolean isSongCompleted(){
        return isSongCompleted;
    }

    public MediaPlayerService() {
    }

    private void watchProgress(){
        progressHandler = new Handler();
        progressHandler.post(MediaPlayerRunnable);


    }



    @Override
    public IBinder onBind(Intent intent) {

        return binder;
    }

    public class MediaPlayerBinder extends Binder {
        MediaPlayerService getMediaPlayer(){
            return MediaPlayerService.this;
        }
    }

    public void setPreviewUrl(String previewUrl){
        this.previewUrl = previewUrl;
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        isSongPlaying = true;
        watchProgress();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        isSongCompleted = true;
        isSongPlaying = false;
        progressHandler.removeCallbacks(MediaPlayerRunnable);
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Toast.makeText(getApplicationContext(), "Error on Media Player playback. " +
                "Please check your internet connection and try again later", Toast.LENGTH_LONG).show();
        mp.reset();
        return true;
    }

    @Override
    public boolean onUnbind(Intent intent) {

        return super.onUnbind(intent);

    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    public void initStart(){
        progress = 0;
        isSongPlaying = false;
        isSongCompleted = false;
        String url = previewUrl;

        if (mMediaPlayer!=null){
            mMediaPlayer.release();
        }
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.setDataSource(url);
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
        }
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);

        mMediaPlayer.prepareAsync();


    }

public void pause(){
    if (mMediaPlayer!=null) {
        mMediaPlayer.pause();
        isSongPlaying = false;
    }
}

    public void start(){
        mMediaPlayer.start();
        isSongPlaying = true;
    }

    public void reset(){
        if (mMediaPlayer!=null) {
            mMediaPlayer.reset();
            isSongPlaying = false;
        }
    }

    public void release(){
        progressHandler.removeCallbacks(MediaPlayerRunnable);
        progress = 0;
        mMediaPlayer.release();
        isSongPlaying = false;

    }

    public void stop(){
        mMediaPlayer.stop();
        isSongPlaying = false;
    }


    public void seekTo(int progress){
        mMediaPlayer.seekTo(progress);
    }


private Runnable MediaPlayerRunnable =     new Runnable() {
    @Override
    public void run() {
        progress = mMediaPlayer.getCurrentPosition();
        progressHandler.postDelayed(this, 100);
    }
};

}
