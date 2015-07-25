package com.bungbagong.spotify_streamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.bungbagong.spotify_steamer.R;

import java.util.ArrayList;


public class SpotifyActivity extends AppCompatActivity implements SpotifyActivityFragment.ArtistCallback, TopTrackActivityFragment.TracksCallback {


    private boolean mTwoPane;
    public static final String TRACK_LIST = "TRACK_LIST";
    public static final String POSITION = "POS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify);

        if (findViewById(R.id.fragment_container) != null){
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }

    }


    @Override
    public void onArtistSelected(ArrayList<String> artist_data) {

        String id = artist_data.get(0);
        String name = artist_data.get(1);

        if (mTwoPane){
            Bundle arguments = new Bundle();
            arguments.putString(TopTrackActivity.ARTIST_ID, id);
            arguments.putString(TopTrackActivity.ARTIST_NAME,name);

            TopTrackActivityFragment fragment = new TopTrackActivityFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment).commit();

        } else {
            Intent intent = new Intent(this, TopTrackActivity.class);
            intent.putExtra(TopTrackActivity.ARTIST_ID, id);
            intent.putExtra(TopTrackActivity.ARTIST_NAME, name);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);

        }
    }

    @Override
    public void onTrackSelected(ArrayList<SimpleTrack> simpleTracks, int position) {
        Bundle arguments = new Bundle();
        arguments.putParcelableArrayList(TRACK_LIST,simpleTracks);
        arguments.putInt(POSITION, position);

        DialogPlayerActivityFragment newFragment = new DialogPlayerActivityFragment();
        newFragment.setArguments(arguments);

        FragmentManager fragmentManager = getSupportFragmentManager();
        newFragment.show(fragmentManager, "dialog");
    }
}
