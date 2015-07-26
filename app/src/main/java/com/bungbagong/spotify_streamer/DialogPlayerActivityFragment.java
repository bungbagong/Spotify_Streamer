package com.bungbagong.spotify_streamer;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bungbagong.spotify_steamer.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class DialogPlayerActivityFragment extends DialogFragment implements View.OnClickListener {

    boolean mIsLargeLayout;
    private MediaPlayerService mediaPlayer;
    private boolean isBound = false;
    private boolean isPaused = true;
    private boolean isInit = false;
    private ArrayList<SimpleTrack> simpleTracks;
    private int position;
    private String previewUrl;
    private TextView artistName;
    private TextView albumName;
    private TextView trackName;
    private ImageView albumImage;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlayerService.MediaPlayerBinder mediaPlayerBinder =
                    (MediaPlayerService.MediaPlayerBinder) service;
            mediaPlayer = mediaPlayerBinder.getMediaPlayer();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v("nanda", "DialogPlayerFragment onDestroyView");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);




    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Log.v("nanda", "DialogPlayerFragment onDestroy");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(getActivity().isFinishing() && isBound){
            getActivity().unbindService(connection);
            isBound = false;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if(!isBound) {
            Intent intent = new Intent(getActivity(), MediaPlayerService.class);
            getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);


        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v("nanda", "DialogPlayerFragment onStop");

    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.button_previous: {
                int size = simpleTracks.size();
                if (position == 0) {
                    position = size - 1;

                } else {
                    position -= 1;
                }
                previewUrl = simpleTracks.get(position).getPreviewUrl();

                artistName.setText(simpleTracks.get(position).getArtist());
                albumName.setText(simpleTracks.get(position).getAlbum());
                trackName.setText(simpleTracks.get(position).getTrack());


                ImageView albumImage = (ImageView) getView().findViewById(R.id.album_image);
                if(simpleTracks.get(position).getImage_640px() != null){
                    Picasso.with(getActivity()).load(simpleTracks.get(position).getImage_640px()).into(albumImage);
                }

                Log.v("bungbagong", previewUrl);
                mediaPlayer.reset();
                mediaPlayer.setPreviewUrl(previewUrl);
                mediaPlayer.initStart();

                isInit = true;
                isPaused = false;

                break;
            }
            case R.id.button_play: {
                if (!isInit) {
                    //intent.putExtra(MediaPlayerService.PREVIEW_URL, previewUrl);
                    Log.v("bungbagong", previewUrl);

                    mediaPlayer.setPreviewUrl(previewUrl);
                    mediaPlayer.initStart();

                    ((ImageButton) v.findViewById(R.id.button_play)).setImageResource(android.R.drawable.ic_media_pause);
                    isInit = true;
                    isPaused = false;

                } else {

                    if (!isPaused) {
                        ((ImageButton) v.findViewById(R.id.button_play)).setImageResource(android.R.drawable.ic_media_play);
                        isPaused = true;
                        mediaPlayer.pause();
                    } else {
                        ((ImageButton) v.findViewById(R.id.button_play)).setImageResource(android.R.drawable.ic_media_pause);
                        isPaused = false;
                        mediaPlayer.start();
                    }
                }

                break;
            }
            case R.id.button_next: {
                int size = simpleTracks.size();
                if (position == size - 1) {
                    position = 0;

                } else {
                    position += 1;
                }
                previewUrl = simpleTracks.get(position).getPreviewUrl();

                artistName.setText(simpleTracks.get(position).getArtist());
                albumName.setText(simpleTracks.get(position).getAlbum());
                trackName.setText(simpleTracks.get(position).getTrack());

                ImageView albumImage = (ImageView) getView().findViewById(R.id.album_image);
                if(simpleTracks.get(position).getImage_640px() != null){
                    Picasso.with(getActivity()).load(simpleTracks.get(position).getImage_640px()).into(albumImage);
                }

                Log.v("bungbagong", previewUrl);
                mediaPlayer.reset();
                mediaPlayer.setPreviewUrl(previewUrl);
                mediaPlayer.initStart();

                isInit = true;
                isPaused = false;

                break;
            }
            default:
                break;

        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_dialog_track_player, container, false);

        if(isPaused) {
            ImageButton button_play = (ImageButton) rootView.findViewById(R.id.button_play);
            button_play.setOnClickListener(this);
            button_play.setImageResource(android.R.drawable.ic_media_play);
        } else {
            ImageButton button_play = (ImageButton) rootView.findViewById(R.id.button_play);
            button_play.setOnClickListener(this);
            button_play.setImageResource(android.R.drawable.ic_media_pause);
        }

        ImageButton button_next = (ImageButton) rootView.findViewById(R.id.button_next);
        button_next.setOnClickListener(this);
        ImageButton button_previous = (ImageButton) rootView.findViewById(R.id.button_previous);
        button_previous.setOnClickListener(this);

        if(!isInit) {
            simpleTracks = getArguments().getParcelableArrayList(SpotifyActivity.TRACK_LIST);
            position = getArguments().getInt(SpotifyActivity.POSITION);
            previewUrl = simpleTracks.get(position).getPreviewUrl();
        }

        artistName = (TextView) rootView.findViewById(R.id.artist_name);
        artistName.setText(simpleTracks.get(position).getArtist());
        albumName = (TextView) rootView.findViewById(R.id.album_name);
        albumName.setText(simpleTracks.get(position).getAlbum());
        trackName = (TextView) rootView.findViewById(R.id.track_name);
        trackName.setText(simpleTracks.get(position).getTrack());


        albumImage = (ImageView) rootView.findViewById(R.id.album_image);
        if(simpleTracks.get(position).getImage_640px() != null) {
            Picasso.with(getActivity()).load(simpleTracks.get(position).getImage_640px()).into(albumImage);
        }


        Log.v("nanda", "DialogPlayerFragment onCreateView");

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







}
