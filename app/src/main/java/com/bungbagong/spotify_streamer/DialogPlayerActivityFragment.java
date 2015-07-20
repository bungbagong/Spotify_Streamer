package com.bungbagong.spotify_streamer;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bungbagong.spotify_steamer.R;


/**
 * A placeholder fragment containing a simple view.
 */
public class DialogPlayerActivityFragment extends DialogFragment {

    boolean mIsLargeLayout;

    public DialogPlayerActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //setAsFragmentOrDialog();
        return inflater.inflate(R.layout.fragment_dialog_track_player, container, false);
    }









}
