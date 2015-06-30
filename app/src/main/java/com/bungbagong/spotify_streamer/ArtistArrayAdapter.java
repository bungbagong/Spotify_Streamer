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

import kaaes.spotify.webapi.android.models.Artist;


/**
 * Created by bungbagong on 24/6/2015.
 */
public class ArtistArrayAdapter extends ArrayAdapter<Artist> {

    private Context context;


    public ArtistArrayAdapter(Context context, int resource, List<Artist> artists){
        super(context,resource,artists);
        this.context = context;


    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Artist artist_i = getItem(position);



        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_item_artist_spotify, null);

        // Lookup view for data population
        TextView artistName = (TextView) view.findViewById(R.id.list_item_text_view_artist);
        ImageView artistImage = (ImageView) view.findViewById(R.id.list_item_image_view_artist);
        // Populate the data into the template view using the data object
        artistName.setText(artist_i.name);

        if(artist_i.images.size() != 0) {

            int a = 1000;
            int b = 0;
            //int width = 1000;
            for (int i = 0; i < artist_i.images.size(); i++ ){
                int width = artist_i.images.get(i).width;
                if (a >= width && width >=64){
                    b = i;
                    a = width;
                }
            }
            Log.v("width",artist_i.images.get(b).width.toString()+"  "+ artist_i.images.get(b).height.toString());
            Picasso.with(context).load(artist_i.images.get(b).url).into(artistImage);
        }
        return view;






    }
}
