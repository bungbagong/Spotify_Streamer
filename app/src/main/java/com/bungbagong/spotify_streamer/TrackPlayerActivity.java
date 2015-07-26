package com.bungbagong.spotify_streamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.bungbagong.spotify_steamer.R;

;

public class TrackPlayerActivity extends AppCompatActivity {

    private DialogFragment dialogFragment;

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("nanda", "TrackPlayerActivity is Stop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("nanda", "TrackPlayerActivity is destroyed");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_player);

        Log.v("nanda", "TrackPlayerActivity is created");

        FragmentManager fm = getSupportFragmentManager();
        dialogFragment = (DialogFragment) fm.findFragmentByTag("dialog_fragment");

        if(dialogFragment==null) {
            Intent intent = getIntent();
            Bundle arguments = new Bundle();
            arguments.putParcelableArrayList(SpotifyActivity.TRACK_LIST,
                    intent.getParcelableArrayListExtra(SpotifyActivity.TRACK_LIST));
            arguments.putInt(SpotifyActivity.POSITION, intent.getIntExtra(SpotifyActivity.POSITION, 0));

            //Create Dialog as Fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            DialogPlayerActivityFragment newFragment = new DialogPlayerActivityFragment();
            newFragment.setArguments(arguments);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.add(R.id.fragment_track_player, newFragment, "dialog_fragment").addToBackStack(null).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_track_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;

        }

        return super.onOptionsItemSelected(item);
    }




}
