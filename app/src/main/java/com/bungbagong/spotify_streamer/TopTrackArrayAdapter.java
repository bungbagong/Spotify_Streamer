package com.bungbagong.spotify_streamer;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bungbagong.spotify_steamer.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by bungbagong on 30/6/2015.
 */
public class TopTrackArrayAdapter extends ArrayAdapter<Track> {

    protected List<Track> toptracks;
    Context context;

    public TopTrackArrayAdapter(Context context, int resource, List<Track> tracks){
        super(context,resource,tracks);
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Track track_i = getItem(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_item_top_tracks, null);

        TextView trackName = (TextView) view.findViewById(R.id.list_item_text_view_top_tracks);
        ImageView albumImage = (ImageView) view.findViewById(R.id.list_item_image_view_album);
        TextView albumName = (TextView) view.findViewById(R.id.list_item_text_view_album_name);

        trackName.setText(track_i.name);
        albumName.setText(track_i.album.name);

        if(track_i.album.images.size() != 0) {

            int a = 1000;
            int b = 0;
            //int width = 1000;
            for (int i = 0; i < track_i.album.images.size(); i++ ){
                int width = track_i.album.images.get(i).width;
                if (a >= width && width >=64){
                    b = i;
                    a = width;
                }
            }
            Log.v("width", track_i.album.images.get(b).width.toString() + "  " + track_i.album.images.get(b).height.toString());
            Picasso.with(context).load(track_i.album.images.get(b).url).into(albumImage);

        }
        return view;

    }
}
