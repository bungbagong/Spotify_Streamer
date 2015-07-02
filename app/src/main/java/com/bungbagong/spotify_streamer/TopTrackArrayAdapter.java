package com.bungbagong.spotify_streamer;

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

    private static class ViewHolder{
        TextView trackName;
        ImageView albumImage;
        TextView albumName;
    }

    public TopTrackArrayAdapter(Context context, int resource, List<SimpleTrack> tracks){
        super(context,resource,tracks);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SimpleTrack track_i = getItem(position);

        ViewHolder viewHolder;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_top_tracks,parent,false);
            viewHolder.trackName = (TextView) convertView.findViewById(R.id.list_item_text_view_top_tracks);
            viewHolder.albumImage = (ImageView) convertView.findViewById(R.id.list_item_image_view_album);
            viewHolder.albumName = (TextView) convertView.findViewById(R.id.list_item_text_view_album_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.trackName.setText(track_i.getTrack());
        viewHolder.albumName.setText(track_i.getAlbum());

        if(track_i.getImage_200px() != null){
            Picasso.with(getContext()).load(track_i.getImage_200px()).into(viewHolder.albumImage);
        }
        return convertView;

    }
}
