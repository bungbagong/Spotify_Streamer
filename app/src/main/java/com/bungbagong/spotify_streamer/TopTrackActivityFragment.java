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

import java.util.ArrayList;
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
    public TopTrackArrayAdapter topTracksArrayAdapter;
    public ArrayList<SimpleTrack> trackParcel;

    public TopTrackActivityFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        if(trackParcel!=null){
            outState.putParcelableArrayList("topTracks",trackParcel);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_top_track, container, false);

        if(savedInstanceState!=null){
            trackParcel = savedInstanceState.getParcelableArrayList("topTracks");
            artist_id = trackParcel.get(0).getId();
            buildAdapter(rootView, trackParcel);
        }
        else {  //if Fragment is triggered by intent, query top tracks
            Intent intent = getActivity().getIntent();
            artist_id = intent.getStringExtra(TopTrackActivity.ARTIST_ID);
            TopTracksQueryTask trackQuery = new TopTracksQueryTask();
            trackQuery.execute(artist_id);
        }

        return rootView;
    }

    public void buildAdapter(View rootView, List<SimpleTrack> topTracksResult){


        if (topTracksArrayAdapter == null) {
            topTracksArrayAdapter = new TopTrackArrayAdapter(
                    getActivity(), R.layout.list_item_top_tracks, topTracksResult);
            ListView listView = (ListView)
                    rootView.findViewById(R.id.list_view_top_tracks);
            listView.setAdapter(topTracksArrayAdapter);
        }

        else {
            topTracksArrayAdapter.clear();
            topTracksArrayAdapter.addAll(topTracksResult);
            topTracksArrayAdapter.notifyDataSetChanged();
        }
    }

    public class TopTracksQueryTask extends AsyncTask<String, Void, List<SimpleTrack>> {

        protected List<Track> list_tracks;
        private final String LOG_TAG = TopTracksQueryTask.class.getSimpleName();

        @Override
        protected void onPostExecute(List<SimpleTrack> topTracksResult) {
            buildAdapter(getView(), topTracksResult);
        }



        @Override
        protected List<SimpleTrack> doInBackground(String... params) {

            try {
                SpotifyApi api = new SpotifyApi();

                SpotifyService spotify = api.getService();

                Map<String,Object> map = new HashMap<String,Object>();
                map.put("country","SG");
                Tracks tracks = spotify.getArtistTopTrack(artist_id,map);
                String artistName = (spotify.getArtist(artist_id)).name;
                list_tracks = tracks.tracks;

                trackParcel = new ArrayList<SimpleTrack>();
                for (Track i : list_tracks){
                    trackParcel.add(new SimpleTrack(
                            artistName,
                            artist_id,
                            i.album.name,
                            i.name,
                            getImageByWidth(i, 640),
                            getImageByWidth(i,200)
                    ));
                }


            } catch (Exception e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            }
            return trackParcel;
        }

        protected String getImageByWidth(Track track_i, int targetWidth){
            for (int i = 0; i < track_i.album.images.size(); i++ ){
                int currentWidth = track_i.album.images.get(i).width;
                if (targetWidth == currentWidth){
                    return track_i.album.images.get(i).url;
                } else if(targetWidth > currentWidth) { //get any image one size smaller
                    return track_i.album.images.get(i).url;
                }
            }
            return null;

        }
        
        
    }
}
