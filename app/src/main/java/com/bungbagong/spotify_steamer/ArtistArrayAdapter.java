package com.bungbagong.spotify_steamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;


/**
 * Created by bungbagong on 24/6/2015.
 */
public class ArtistArrayAdapter extends ArrayAdapter<Artist> {

    private Context context;
    private List<Artist> list_artist;
    private int resource;

    public ArtistArrayAdapter(Context context, int resource, List<Artist> artists){
        super(context,resource,artists);
        this.context = context;
        this.list_artist = artists;
        this.resource = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Artist artist_i = getItem(position);


            convertView = LayoutInflater.from(getContext()).inflate(resource,parent);

        // Lookup view for data population
        TextView artistName = (TextView) convertView.findViewById(R.id.list_item_text_view_artist);
        ImageView artistImage = (ImageView) convertView.findViewById(R.id.list_item_image_view_artist);
        // Populate the data into the template view using the data object
        artistName.setText(artist_i.name);
        Picasso.with(context).load(artist_i.images.get(2).url).into(artistImage);
        // Return the completed view to render on screen
        return convertView;






    }
}
