package com.bungbagong.spotify_steamer;

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

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;


/**
 * A placeholder fragment containing a simple view.
 */
public class SpotifyActivityFragment extends Fragment {

    private static final String CLIENT_ID = "8fd3c936235a427dae86000fe48d5cc8";

    public final String LOG_CAT = this.getClass().getSimpleName();
    ArrayAdapter<String> mForecastAdapter;


    public SpotifyActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {





        // Create some dummy data for the ListView.  Here's a sample weekly forecast
        String[] data = {
                "Mon 6/23 - Sunny - 31/17",
                "Tue 6/24 - Foggy - 21/8",
                "Wed 6/25 - Cloudy - 22/17",
                "Thurs 6/26 - Rainy - 18/11",
                "Fri 6/27 - Foggy - 21/10",
                "Sat 6/28 - TRAPPED IN WEATHERSTATION - 23/18",
                "Sun 6/29 - Sunny - 20/7"
        };
        List<String> weekForecast = new ArrayList<String>(Arrays.asList(data));


        // Now that we have some dummy forecast data, create an ArrayAdapter.
        // The ArrayAdapter will take data from a source (like our dummy forecast) and
        // use it to populate the ListView it's attached to.
        mForecastAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_artist_spotify, // The name of the layout ID.
                        R.id.list_item_text_view_artist, // The ID of the textview to populate.
                        weekForecast);

        View rootView = inflater.inflate(R.layout.fragment_spotify, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.list_view_item_artist);
        listView.setAdapter(mForecastAdapter);


        SearchView search = (SearchView)rootView.findViewById(R.id.edit_text_artist_name);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
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
            //super.onPostExecute(strings);

            //Log.v(LOG_TAG, strings.toString());
            //List<Artist> strings = artistResult;
            //List<String> results = new ArrayList<String>(Arrays.asList(strings));
            mForecastAdapter.clear();
            for(int i = 0; i < artistResult.size(); i++){
                mForecastAdapter.add(artistResult.get(i).name);
            }

            mForecastAdapter.notifyDataSetChanged();

        }



        @Override
        protected List<Artist> doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;





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
