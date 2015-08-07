package com.bungbagong.spotify_streamer;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bungbagong.spotify_steamer.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


/**
 * A placeholder fragment containing a simple view.
 */
public class DialogPlayerActivityFragment extends DialogFragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    boolean mIsLargeLayout;
    private MediaPlayerService mediaPlayer;
    private boolean isBound = false;
    private boolean isPaused = false;
    private boolean isInit = false;
    private ArrayList<SimpleTrack> simpleTracks;
    private int position;
    private String previewUrl;
    private TextView artistName;
    private TextView albumName;
    private TextView trackName;
    private ImageView albumImage;
    private Handler progressHandler;
    private SeekBar seekBar;
    private ImageButton playButton;
    private TextView elapsedTime;
    private TextView endTime;
    private long trackDuration = 30000;

    private Runnable ProgressRunnable = new Runnable() {

        boolean isPlaying = false;


        @Override
        public void run() {


            if (mediaPlayer != null) {
                if (mediaPlayer.isSongPlaying()) {
                    //double pos = mediaPlayer.getProgress();
                    //double temp = (pos/(double)trackDuration)*100;

                    seekBar.setProgress(mediaPlayer.getProgress());
                    elapsedTime.setText(formatStringDuration(mediaPlayer.getProgress() + 500));
                }

                if (mediaPlayer.isSongCompleted()) {
                    isInit = false;
                    playButton.setImageResource(android.R.drawable.ic_media_play);
                    //Log.d("nanda", "isInit false");

                }
            }

            progressHandler.postDelayed(this, 100);
        }
    };
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlayerService.MediaPlayerBinder mediaPlayerBinder =
                    (MediaPlayerService.MediaPlayerBinder) service;
            mediaPlayer = mediaPlayerBinder.getMediaPlayer();
            isBound = true;

            if (!isInit) {
                //intent.putExtra(MediaPlayerService.PREVIEW_URL, previewUrl);
                //Log.v("bungbagong", previewUrl);

                mediaPlayer.setPreviewUrl(previewUrl);
                mediaPlayer.initStart();

                //((ImageButton) v.findViewById(R.id.button_play)).setImageResource(android.R.drawable.ic_media_pause);
                isInit = true;
                isPaused = false;
            }
            watchProgress();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    public void onDestroyView() {

        //if (getDialog() != null && getRetainInstance())
        //    getDialog().setDismissMessage(null);
        super.onDestroyView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
        Log.d("dialogsetRetainInstance", String.valueOf(getRetainInstance()));
        if (savedInstanceState != null) {
            isInit = savedInstanceState.getBoolean("isInit");
            isBound = savedInstanceState.getBoolean("isBound");
            isPaused = savedInstanceState.getBoolean("isPaused");
            position = savedInstanceState.getInt("position");
            simpleTracks = savedInstanceState.getParcelableArrayList("simpleTracks");
            previewUrl = savedInstanceState.getString("previewUrl");
            //trackDuration = savedInstanceState.getLong("trackDuration");
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Log.v("nanda", "DialogPlayerFragment onDestroy");

    }

    @Override
    public void onDetach() {
        super.onDetach();
/*        if(getActivity().isFinishing() && isBound){
            getActivity().unbindService(connection);
            isBound = false;
        }*/
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getActivity(), MediaPlayerService.class);
        getActivity().startService(intent);
        getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);


    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v("nanda", "DialogPlayerFragment onStop");
        getActivity().unbindService(connection);
        isBound = false;
        progressHandler.removeCallbacks(ProgressRunnable);

        if (isBound && getActivity().isFinishing()) {
            progressHandler.removeCallbacks(ProgressRunnable);
            mediaPlayer.release();
            Log.v("nanda", "DialogPlayerFragment isFinishing");
            getActivity().unbindService(connection);
            isBound = false;

        }
    }

    public int getSongPosition(boolean isNext){
        int size = simpleTracks.size();
        if (isNext) {

            if (position == size - 1) {
                position = 0;

            } else {
                position += 1;
            }
            return position;
        } else {
            if (position == 0) {
                position = size - 1;

            } else {
                position -= 1;
            }
            return position;
        }
    }

    public void initMediaPlayer(){
        seekBar.setProgress(0);
        elapsedTime.setText("00:00");
        previewUrl = simpleTracks.get(position).getPreviewUrl();
        mediaPlayer.setPreviewUrl(previewUrl);
        mediaPlayer.initStart();
        isInit=true;
        isPaused=false;
    }


    public void setDialogView(){


        artistName.setText(simpleTracks.get(position).getArtist());
        albumName.setText(simpleTracks.get(position).getAlbum());
        trackName.setText(simpleTracks.get(position).getTrack());

        endTime.setText(formatStringDuration(trackDuration));
        elapsedTime.setText("00:00");


        //ImageView albumImage = (ImageView) getView().findViewById(R.id.album_image);
        if (simpleTracks.get(position).getImage_640px() != null) {
                   Picasso.with(getActivity()).load(simpleTracks.get(position).getImage_640px()).into(albumImage);
        }
    }

    public String formatStringDuration(long millis){
        String duration = String.format("%02d:%02d",

                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        return duration;
    }


    @Override
    public void onClick(View v) {
        if (mediaPlayer != null) {
            switch (v.getId()) {
                case R.id.button_previous: {
                    isInit = false;
                    isPaused = true;


                    position = getSongPosition(false);
                    initMediaPlayer();
                    setDialogView();
                    setPlayButton();
                    break;
                }
                case R.id.button_play: {
                    if (!isInit) {
                        initMediaPlayer();
                        //Log.d("nanda", "button play isInit true");
                        //playButton.setImageResource(android.R.drawable.ic_media_pause);
                    } else if (!isPaused) {
                        isPaused = true;
                        mediaPlayer.pause();
                        //playButton.setImageResource(android.R.drawable.ic_media_play);
                    } else if (isPaused) {
                        isPaused = false;
                        mediaPlayer.start();
                        //playButton.setImageResource(android.R.drawable.ic_media_pause);
                    }
                    setPlayButton();

                    break;
                }
                case R.id.button_next: {
                    isInit = false;
                    isPaused = true;

                    position = getSongPosition(true);
                    setDialogView();
                    initMediaPlayer();
                    setPlayButton();

                    break;
                }
                default:
                    break;

            }

        }
    }


    public void setPlayButton(){
        if(isPaused) {
            playButton.setImageResource(android.R.drawable.ic_media_play);
        } else {
            playButton.setImageResource(android.R.drawable.ic_media_pause);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isBound", isBound);
        outState.putBoolean("isInit", isInit);
        outState.putBoolean("isPaused", isPaused);
        outState.putParcelableArrayList("simpleTracks", simpleTracks);
        outState.putString("previewUrl", previewUrl);
        outState.putInt("position", position);
        //outState.putLong("trackDuration",trackDuration);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_dialog_track_player, container, false);
        
        playButton = (ImageButton) rootView.findViewById(R.id.button_play);
        
        if (isPaused) {
            playButton.setOnClickListener(this);
            playButton.setImageResource(android.R.drawable.ic_media_play);
        } else {
            playButton.setOnClickListener(this);
            playButton.setImageResource(android.R.drawable.ic_media_pause);
        }

        ImageButton button_next = (ImageButton) rootView.findViewById(R.id.button_next);
        button_next.setOnClickListener(this);
        ImageButton button_previous = (ImageButton) rootView.findViewById(R.id.button_previous);
        button_previous.setOnClickListener(this);

        if (!isInit) {
            simpleTracks = getArguments().getParcelableArrayList(SpotifyActivity.TRACK_LIST);
            position = getArguments().getInt(SpotifyActivity.POSITION);
            previewUrl = simpleTracks.get(position).getPreviewUrl();
            //trackDuration = simpleTracks.get(position).getMsDuration();
        }

        artistName = (TextView) rootView.findViewById(R.id.artist_name);
        //artistName.setText(simpleTracks.get(position).getArtist());
        albumName = (TextView) rootView.findViewById(R.id.album_name);
        //albumName.setText(simpleTracks.get(position).getAlbum());
        trackName = (TextView) rootView.findViewById(R.id.track_name);
        //trackName.setText(simpleTracks.get(position).getTrack());
        elapsedTime = (TextView) rootView.findViewById(R.id.elapsed_time);
        endTime = (TextView) rootView.findViewById(R.id.end_time);


        seekBar = (SeekBar) rootView.findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setMax((int) trackDuration);
        //Log.d("nanda", "trackduration : "+trackDuration);


        albumImage = (ImageView) rootView.findViewById(R.id.album_image);
        //if (simpleTracks.get(position).getImage_640px() != null) {
        //    Picasso.with(getActivity()).load(simpleTracks.get(position).getImage_640px()).into(albumImage);
        //}
        setDialogView();

        //Log.v("nanda", "DialogPlayerFragment onCreateView");

        return rootView;


    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public void watchProgress() {
        progressHandler = new Handler();
        progressHandler.post(ProgressRunnable);

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser && (mediaPlayer != null)) {
            //progress = (int)Math.round((progress/100.0)*30000);
            mediaPlayer.seekTo(progress);
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }
}
