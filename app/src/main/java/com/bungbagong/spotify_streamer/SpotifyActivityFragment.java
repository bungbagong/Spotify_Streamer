package com.bungbagong.spotify_streamer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.bungbagong.spotify_steamer.R;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;


/**
 * A placeholder fragment containing a simple view.
 */
public class SpotifyActivityFragment extends Fragment {

    protected List<Artist> list_artist;
    public final String LOG_CAT = this.getClass().getSimpleName();
    ArtistArrayAdapter artistArrayAdapter;
    String artist_id;
    ArrayList<SimpleArtist> artistParcel;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (artistParcel != null){
            outState.putParcelableArrayList("artist", artistParcel);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        if(savedInstanceState != null){
            artistParcel = savedInstanceState.getParcelableArrayList("artist");

        }
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_spotify, container, false);

        SearchView search = (SearchView)rootView.findViewById(R.id.search_artist_name);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SpotifyQueryTask spotifyQuery = new SpotifyQueryTask();
                spotifyQuery.execute(query);
                getView().clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        if(savedInstanceState != null){
            artistParcel = savedInstanceState.getParcelableArrayList("artist");
            artistArrayAdapter = new ArtistArrayAdapter(
                    getActivity(), R.layout.list_item_artist_spotify, artistParcel);
        }
        if(artistParcel==null) {
            artistArrayAdapter = new ArtistArrayAdapter(
                    getActivity(), R.layout.list_item_artist_spotify, new ArrayList<SimpleArtist>());
        }

        ListView listView = (ListView) rootView.findViewById(R.id.list_view_item_artist);
        listView.setAdapter(artistArrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),TopTrackActivity.class);
                artist_id = artistParcel.get(position).getId();
                intent.putExtra(TopTrackActivity.ARTIST_ID,artist_id);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public class SpotifyQueryTask extends AsyncTask<String, Void, List<SimpleArtist>> {


        private final String LOG_TAG = SpotifyQueryTask.class.getSimpleName();

        @Override
        protected void onPostExecute(List<SimpleArtist> artistResult) {

            if(artistResult == null || artistResult.isEmpty()) {
                Context context = getActivity();

                if(context!=null) {
                    Toast.makeText
                            (context,"There is no data for this artist. " +
                                    "Make sure the name is spelled correctly.",
                                    Toast.LENGTH_LONG).show();
                }
            }
            else {
                if (artistArrayAdapter == null) {
                    artistArrayAdapter = new ArtistArrayAdapter(
                            getActivity(), R.layout.list_item_artist_spotify, artistResult);
                    ListView listView = (ListView) getView().findViewById(R.id.list_view_item_artist);
                    listView.setAdapter(artistArrayAdapter);
                }

                else {
                    artistArrayAdapter.clear();
                    artistArrayAdapter.addAll(artistResult);
                    artistArrayAdapter.notifyDataSetChanged();
                }

            }
        }



        @Override
        protected List<SimpleArtist> doInBackground(String... params) {

            try {
                SpotifyApi api = new SpotifyApi();
                SpotifyService spotify = api.getService();
                ArtistsPager results = spotify.searchArtists(params[0]);
                list_artist = results.artists.items;


                //putting artist results into parcelable arraylist
                artistParcel = new ArrayList<SimpleArtist>();
                for (Artist i : list_artist){
                    artistParcel.add(new SimpleArtist(
                            i.id,i.name,getImageSize(i,640),getImageSize(i,200)
                    ));
                 }

            } catch (Exception e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            }

            return artistParcel;
        }


        protected String getImageSize(Artist artist_i, int imgSize){
                int targetWidth = imgSize;
                for (int i = 0; i < artist_i.images.size(); i++ ){
                    int currentWidth = artist_i.images.get(i).width;
                    if (targetWidth == currentWidth){
                        return artist_i.images.get(i).url;
                    }
                }
                return null;
        }


    }





}
