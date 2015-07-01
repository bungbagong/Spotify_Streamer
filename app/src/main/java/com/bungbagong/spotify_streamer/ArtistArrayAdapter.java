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
 * Created by bungbagong on 24/6/2015.
 */
public class ArtistArrayAdapter extends ArrayAdapter<SimpleArtist> {
    private Context context;

    public ArtistArrayAdapter(Context context, int resource, List<SimpleArtist> artists){
        super(context,resource,artists);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SimpleArtist artist_i = getItem(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_item_artist_spotify, null);

        // Lookup view for data population
        TextView artistName = (TextView) view.findViewById(R.id.list_item_text_view_artist);
        ImageView artistImage = (ImageView) view.findViewById(R.id.list_item_image_view_artist);
        // Populate the data into the template view using the data object
        artistName.setText(artist_i.getName());

        if(artist_i.getImage_200px() != null){
            Picasso.with(context).load(artist_i.getImage_200px()).into(artistImage);
        }
        return view;






    }
}
