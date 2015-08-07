package com.bungbagong.spotify_streamer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.bungbagong.spotify_steamer.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.RetrofitError;


/**
 * A placeholder fragment containing a simple view.
 */
public class TopTrackActivityFragment extends Fragment {

    public String artist_id;
    public String artist_name;
    public TopTrackArrayAdapter topTracksArrayAdapter;
    public ArrayList<SimpleTrack> trackParcel;
    private final String LOG_TAG = this.getClass().getSimpleName();

    public static interface TracksCallback {
        public void onTrackSelected(ArrayList<SimpleTrack> simpleTracks, int position);
    }


    public TopTrackActivityFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        if(trackParcel!=null){
            outState.putParcelableArrayList("topTracks", trackParcel);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_top_track, container, false);

        if(savedInstanceState!=null){ //rebuild the View
            trackParcel = savedInstanceState.getParcelableArrayList("topTracks");
            artist_id = trackParcel.get(0).getId();
            artist_name = trackParcel.get(0).getArtist();
            buildAdapter(rootView, trackParcel);
        }
        else { //if new fragment is created

            Bundle arguments = getArguments();
            if (arguments != null) {


                artist_id = arguments.getString(TopTrackActivity.ARTIST_ID);
                artist_name = arguments.getString(TopTrackActivity.ARTIST_NAME);
                TopTracksQueryTask trackQuery = new TopTracksQueryTask();
                trackQuery.execute(artist_id);
            }
        }

        return rootView;
    }

    //Build the View based on new query result
    public void buildAdapter(View rootView, List<SimpleTrack> topTracksResult){

        if (topTracksArrayAdapter == null) {
            topTracksArrayAdapter = new TopTrackArrayAdapter(
                    getActivity(), R.layout.list_item_top_tracks, topTracksResult);
            ListView listView = (ListView)
                    rootView.findViewById(R.id.list_view_top_tracks);
            listView.setAdapter(topTracksArrayAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    ((TracksCallback) getActivity()).onTrackSelected(trackParcel, position);
                }
            });
        }

        else {
            topTracksArrayAdapter.clear();
            topTracksArrayAdapter.addAll(topTracksResult);
            topTracksArrayAdapter.notifyDataSetChanged();
        }
    }




    public class TopTracksQueryTask extends AsyncTask<String, Void, List<SimpleTrack>> {

        protected List<Track> list_tracks;


        @Override
        protected void onPostExecute(List<SimpleTrack> topTracksResult) {

            if (topTracksResult == null || topTracksResult.isEmpty()){
                Toast.makeText(getActivity(),getString(R.string.no_top_track_error),
                                                                Toast.LENGTH_LONG).show();

                //NavUtils.navigateUpTo(getActivity(), new Intent
                //        (getActivity(), SpotifyActivity.class).setFlags
                //        (Intent.FLAG_ACTIVITY_CLEAR_TOP));  //returning to Main Activity

            } else {
                buildAdapter(getView(), topTracksResult); //rebuild the view based on result
            }
        }



        @Override
        protected List<SimpleTrack> doInBackground(String... params) {

            String artistName ="";

            try {
                SpotifyApi api = new SpotifyApi();
                SpotifyService spotify = api.getService();

                //Build a Map for option pair: country = "XX" based on Locale setting
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("country", getLocalCountry());

                Tracks tracks = spotify.getArtistTopTrack(artist_id, map);
                artistName = (spotify.getArtist(artist_id)).name;
                list_tracks = tracks.tracks;

            } catch (RetrofitError e) {
                cancel(true); //cancelled= true, trigger onCancelled() instead of onPostExecute()
            }

            if (!isCancelled()) {                               //if no Retrofit error
                trackParcel = new ArrayList<SimpleTrack>();     //put required data into List
                for (Track i : list_tracks) {
                    trackParcel.add(new SimpleTrack(
                            artistName,
                            artist_id,
                            i.album.name,
                            i.name,
                            i.preview_url,
                            i.duration_ms,
                            getImageByWidth(i, 640),
                            getImageByWidth(i, 200)
                    ));
                }
            }
            return trackParcel;
        }

        //Called if cancel() was called. Toast and return to MainActivity
        @Override
        protected void onCancelled(List<SimpleTrack> simpleTracks) { //called if Retrofit error
            Log.d("nanda", "error no internet connection on artist list");
            Toast.makeText(getActivity(), getString(R.string.no_internet_error),
                                                                Toast.LENGTH_LONG).show();

            //NavUtils.navigateUpTo(getActivity(), new Intent
            //        (getActivity(), SpotifyActivity.class).setFlags
            //        (Intent.FLAG_ACTIVITY_CLEAR_TOP));  //returning to Main Activity
        }

        //getting url of image with equal targetWidth or one step smaller
        protected String getImageByWidth(Track track_i, int targetWidth){
            for (int i = 0; i < track_i.album.images.size(); i++ ){
                int currentWidth = track_i.album.images.get(i).width;
                if (targetWidth == currentWidth){ //try to get equal width as target
                    return track_i.album.images.get(i).url;
                } else if(targetWidth > currentWidth) { //if fail, get any image one size smaller
                    return track_i.album.images.get(i).url;
                }
            }
            return null;

        }

        private String getLocalCountry(){
            String country = Locale.getDefault().getCountry();
            if (country.equals("")){
                country = "US";
            }
            return country;
        }
        
        
    }
}
