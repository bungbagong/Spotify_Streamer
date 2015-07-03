package com.bungbagong.spotify_streamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import retrofit.RetrofitError;


/**
 * Fragment Search Artist Name
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_spotify, container, false);

        //Setup text listener for search view
        SearchView search = (SearchView)rootView.findViewById(R.id.search_artist_name);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SpotifyQueryTask spotifyQuery = new SpotifyQueryTask();
                spotifyQuery.execute(query);
                rootView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        if(savedInstanceState != null) {
            artistParcel = savedInstanceState.getParcelableArrayList("artist");
        }
        if (artistParcel != null) {
            buildAdapter(rootView,artistParcel);
        }

        return rootView;
    }

    public void buildAdapter(View view, List<SimpleArtist> artistResult){


        if (artistArrayAdapter == null) {
            artistArrayAdapter = new ArtistArrayAdapter(
                    getActivity(), R.layout.list_item_artist_spotify, artistResult);
            ListView listView = (ListView) view.findViewById(R.id.list_view_item_artist);
            listView.setAdapter(artistArrayAdapter);

            //set onItemClickListener, on click open Top Track Activity, send = artist id and name
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), TopTrackActivity.class);
                    artist_id = artistParcel.get(position).getId();
                    String artist_name = artistParcel.get(position).getName();
                    intent.putExtra(TopTrackActivity.ARTIST_ID, artist_id);
                    intent.putExtra(TopTrackActivity.ARTIST_NAME,artist_name);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
            });
        }

        else {
            artistArrayAdapter.clear();
            artistArrayAdapter.addAll(artistResult);
            artistArrayAdapter.notifyDataSetChanged();
        }
    }

    public class SpotifyQueryTask extends AsyncTask<String, Void, List<SimpleArtist>> {


        @Override
        protected void onPostExecute(List<SimpleArtist> artistResult) {

            if(artistResult == null || artistResult.isEmpty()) {
                    Toast.makeText
                            (getActivity(),getString(R.string.no_artist_error),
                                    Toast.LENGTH_LONG).show();
                    buildAdapter(getView(),new ArrayList<SimpleArtist>()); //empty listView
            }
            else {
                buildAdapter(getView(),artistResult);
            }
        }

        @Override
        protected List<SimpleArtist> doInBackground(String... params) {

            try {
                SpotifyApi api = new SpotifyApi();
                SpotifyService spotify = api.getService();
                ArtistsPager results = spotify.searchArtists(params[0]);
                list_artist = results.artists.items;

            } catch (RetrofitError e) {
                cancel(true); //cancelled= true, trigger onCancelled() instead of onPostExecute()
            }

            if(!isCancelled()) {
                //putting artist results into parcelable arraylist
                artistParcel = new ArrayList<SimpleArtist>();
                for (Artist i : list_artist) {
                    artistParcel.add(new SimpleArtist(
                            i.id, i.name, getImageSize(i, 640), getImageSize(i, 200)
                    ));
                }
            }

            return artistParcel;
        }


        //called if isCancelled = true after doInBackground()
        @Override
        protected void onCancelled(List<SimpleArtist> simpleArtists) {
            Toast.makeText(getActivity(), getString(R.string.no_internet_error) ,Toast.LENGTH_LONG).show();
        }

        //getting url of image with equal targetWidth or one step smaller
        protected String getImageSize(Artist artist_i, int targetWidth){
                for (int i = 0; i < artist_i.images.size(); i++ ){
                    int currentWidth = artist_i.images.get(i).width;
                    if (targetWidth == currentWidth){ //try to get target Width
                        return artist_i.images.get(i).url;
                    } else if(targetWidth > currentWidth) { //get any size smaller than target
                        return artist_i.images.get(i).url;
                    }

                }
                return null;
        }


    }





}
