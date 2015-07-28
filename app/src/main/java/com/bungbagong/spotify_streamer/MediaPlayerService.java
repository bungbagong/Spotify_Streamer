package com.bungbagong.spotify_streamer;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener {

    private final IBinder binder = new MediaPlayerBinder();
    private MediaPlayer mMediaPlayer;
    private String previewUrl;
    public static final String PREVIEW_URL = "PREV_URL";
    public int progress;



    public int getProgress() {
        return progress;
    }



    public MediaPlayerService() {
    }

    private void watchProgress(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long pos = mMediaPlayer.getCurrentPosition();
                Log.d("current position =", Long.toString(pos));
                long duration = 30000;
                Log.d("duration =", Long.toString(duration));
                double temp = ((double)pos/(double)duration)*100;
                Log.d("temp =", Double.toString(temp));
                progress = (int)temp;
                Log.d("progress = ", Integer.toString(progress));
                handler.postDelayed(this,100);
            }
        });


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
        watchProgress();
    }

public void pause(){
    mMediaPlayer.pause();
}

    public void start(){
        mMediaPlayer.start();
    }

    public void reset(){
        mMediaPlayer.reset();
    }

}
