package com.bungbagong.spotify_streamer;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;

import com.bungbagong.spotify_steamer.R;


/**
 * A placeholder fragment containing a simple view.
 */
public class DialogPlayerActivityFragment extends DialogFragment implements View.OnClickListener {

    boolean mIsLargeLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.button_previous:
                break;

            case R.id.button_play:
                ((ImageButton)getActivity().findViewById(R.id.button_play)).setImageResource(android.R.drawable.ic_media_pause);
                break;

            case R.id.button_next:
                break;

            default:
                break;

        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_dialog_track_player, container, false);
        ImageButton button_play = (ImageButton) rootView.findViewById(R.id.button_play);
        button_play.setOnClickListener(this);
        ImageButton button_next = (ImageButton) rootView.findViewById(R.id.button_next);
        button_next.setOnClickListener(this);
        ImageButton button_previous = (ImageButton) rootView.findViewById(R.id.button_previous);
        button_previous.setOnClickListener(this);

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
