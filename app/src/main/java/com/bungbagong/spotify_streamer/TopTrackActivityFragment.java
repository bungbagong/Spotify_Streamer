package com.bungbagong.spotify_streamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.bungbagong.spotify_steamer.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;


/**
 * A placeholder fragment containing a simple view.
 */
public class TopTrackActivityFragment extends Fragment {

    public String artist_id;
    TopTrackArrayAdapter topTracksArrayAdapter;

    public TopTrackActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        artist_id = intent.getStringExtra(TopTrackActivity.ARTIST_ID);
        Log.v("artist_ID", "artist id = " + artist_id);

        TopTracksQueryTask trackQuery = new TopTracksQueryTask();
        trackQuery.execute(artist_id);


        return inflater.inflate(R.layout.fragment_top_track, container, false);
    }

    public class TopTracksQueryTask extends AsyncTask<String, Void, List<Track>> {

        protected List<Track> list_tracks;
        private final String LOG_TAG = TopTracksQueryTask.class.getSimpleName();

        @Override
        protected void onPostExecute(List<Track> topTracksResult) {


                if (topTracksArrayAdapter == null) {
                    topTracksArrayAdapter = new TopTrackArrayAdapter(
                            getActivity(), R.layout.list_item_top_tracks, topTracksResult);
                    ListView listView = (ListView) getView().
                            findViewById(R.id.list_view_top_tracks);
                    listView.setAdapter(topTracksArrayAdapter);
                }

                else {
                    topTracksArrayAdapter.clear();
                    topTracksArrayAdapter.addAll(topTracksResult);
                    topTracksArrayAdapter.notifyDataSetChanged();
                }


        }



        @Override
        protected List<Track> doInBackground(String... params) {

            try {
                SpotifyApi api = new SpotifyApi();

                SpotifyService spotify = api.getService();

                Map<String,Object> map = new HashMap<String,Object>();
                map.put("country","SG");
                Tracks tracks = spotify.getArtistTopTrack(artist_id,map);
                list_tracks = tracks.tracks;



            } catch (Exception e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            }
            return list_tracks;
        }
    }
}
