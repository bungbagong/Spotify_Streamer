package com.bungbagong.spotify_streamer;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener {

    private final IBinder binder = new MediaPlayerBinder();
    private MediaPlayer mMediaPlayer = null;
    private String previewUrl;
    public static final String PREVIEW_URL = "PREV_URL";


    public MediaPlayerService() {
    }



    @Override
    public IBinder onBind(Intent intent) {

        //previewUrl = intent.getStringExtra(PREVIEW_URL);
        //Log.v("Service get Preview", previewUrl);

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
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    public void initStart(){
        String url = previewUrl; //"https://p.scdn.co/mp3-preview/18d0a45538122fbe33f22604d0e5608789c10ae4";


        mMediaPlayer= new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.setDataSource(url);
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
        }
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.prepareAsync();
    }

public void pause(){
    mMediaPlayer.pause();
}

    public void start(){
        mMediaPlayer.start();
    }


}
