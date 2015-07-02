package com.bungbagong.spotify_streamer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bungbagong.spotify_steamer.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by bungbagong on 30/6/2015.
 */
public class TopTrackArrayAdapter extends ArrayAdapter<SimpleTrack> {

    Context context_view;

    public TopTrackArrayAdapter(Context context, int resource, List<SimpleTrack> tracks){
        super(context,resource,tracks);
        this.context_view = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SimpleTrack track_i = getItem(position);

        LayoutInflater inflater = (LayoutInflater) context_view.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_item_top_tracks, null);

        TextView trackName = (TextView) view.findViewById(R.id.list_item_text_view_top_tracks);
        ImageView albumImage = (ImageView) view.findViewById(R.id.list_item_image_view_album);
        TextView albumName = (TextView) view.findViewById(R.id.list_item_text_view_album_name);

        trackName.setText(track_i.getTrack());
        albumName.setText(track_i.getAlbum());

        if(track_i.getImage_200px() != null){
            Picasso.with(context_view).load(track_i.getImage_200px()).into(albumImage);
        }
        return view;

    }
}
