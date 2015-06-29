package com.bungbagong.spotify_steamer;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;


/**
 * A placeholder fragment containing a simple view.
 */
public class SpotifyActivityFragment extends Fragment {

    private static final String CLIENT_ID = "8fd3c936235a427dae86000fe48d5cc8";

    public final String LOG_CAT = this.getClass().getSimpleName();
    ArrayAdapter<String> mForecastAdapter;
    ArtistArrayAdapter artistArrayAdapter;


    public SpotifyActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_spotify, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        //ListView listView = (ListView) rootView.findViewById(R.id.list_view_item_artist);
        //listView.setAdapter(mForecastAdapter);


        SearchView search = (SearchView)rootView.findViewById(R.id.search_artist_name);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String artist_name = query;
                SpotifyQueryTask spotifyQuery = new SpotifyQueryTask();
                spotifyQuery.execute(artist_name);
                getView().clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



        return rootView;
    }

    public class SpotifyQueryTask extends AsyncTask<String, Void, List<Artist>> {

        protected List<Artist> list_artist;
        private final String LOG_TAG = SpotifyQueryTask.class.getSimpleName();

        @Override
        protected void onPostExecute(List<Artist> artistResult) {

            if(artistResult == null || artistResult.isEmpty()) {
                Context context = getActivity();

                if(context!=null) {
                    Toast.makeText
                            (context,"There is no data for this artist.",Toast.LENGTH_LONG).show();
                }
            }
            else {
                if (artistArrayAdapter == null) {
                    artistArrayAdapter = new ArtistArrayAdapter(
                            getActivity(), R.layout.list_item_artist_spotify, artistResult);
                    ListView listView = (ListView) getView().
                            findViewById(R.id.list_view_item_artist);
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
        protected List<Artist> doInBackground(String... params) {

            try {
                SpotifyApi api = new SpotifyApi();

                SpotifyService spotify = api.getService();

                ArtistsPager results = spotify.searchArtists(params[0]);
                //Log.v(LOG_CAT,"bungbagong1");
                list_artist = results.artists.items;

                for (Artist i : list_artist){
                    String name = i.name;
                    Log.v(LOG_CAT, name);
                 }

                //Trial get toptrack artis

                String id = list_artist.get(0).id;
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("country","SG");
                Tracks tracks = spotify.getArtistTopTrack(id,map);
                List<Track> trackList = tracks.tracks;
                for(Track i : trackList){
                    String name = i.name;
                    String album = i.album.name;

                    Log.v(LOG_CAT,name + " : " +album);
                }


            } catch (Exception e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            }
            return list_artist;
        }
    }


    public class Listener implements EditText.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_NEXT ||
                    actionId == EditorInfo.IME_ACTION_DONE ) {

                String artist_name = v.getText().toString();
                SpotifyQueryTask spotifyQuery = new SpotifyQueryTask();
                spotifyQuery.execute(artist_name);

            }
            return false;
        }
    }


}
