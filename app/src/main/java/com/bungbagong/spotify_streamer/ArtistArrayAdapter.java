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
 * Created by bungbagong on 24/6/2015.
 */
public class ArtistArrayAdapter extends ArrayAdapter<SimpleArtist> {

    private static class ViewHolder{
        TextView artistName;
        ImageView artistImage;
    }


    public ArtistArrayAdapter(Context context, int resource, List<SimpleArtist> artists){
        super(context,resource,artists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SimpleArtist artist_i = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_artist_spotify,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.artistName = (TextView) convertView.findViewById(R.id.list_item_text_view_artist);
            viewHolder.artistImage = (ImageView) convertView.findViewById(R.id.list_item_image_view_artist);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.artistName.setText(artist_i.getName());
        if(artist_i.getImage_200px() != null){
            Picasso.with(getContext()).load(artist_i.getImage_200px()).into(viewHolder.artistImage);
        }

        return convertView;
    }
}
