package com.bungbagong.spotify_streamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.bungbagong.spotify_steamer.R;


public class TopTrackActivity extends AppCompatActivity {

    public static final String ARTIST_ID = "ARTIST_ID";
    public static final String ARTIST_NAME = "ARTIST_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_top_track);
        Intent intent = getIntent();
        String artist_name = intent.getStringExtra(ARTIST_NAME);
        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setSubtitle(artist_name);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if(id == android.R.id.home){
             NavUtils.navigateUpTo(this,new Intent
                    (this,SpotifyActivity.class).setFlags
                    (Intent.FLAG_ACTIVITY_CLEAR_TOP));
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
