package com.bungbagong.spotify_streamer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bungbagong on 2/7/2015.
 */
public class SimpleTrack implements Parcelable{
    
    private String artist;
    private String id;
    private String album;
    private String track;



    private String previewUrl;
    private Long msDuration;
    private String image_200px;
    private String image_640px;

    public SimpleTrack(String artist, String id, String album, String track,
                       String previewUrl, long msDuration, String image_640px, String image_200px){
        this.artist = artist;
        this.id = id;
        this.album = album;
        this.track = track;
        this.previewUrl = previewUrl;
        this.msDuration = msDuration;
        this.image_640px = image_640px;
        this.image_200px = image_200px;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(artist);
        dest.writeString(id);
        dest.writeString(album);
        dest.writeString(track);
        dest.writeString(previewUrl);
        dest.writeLong(msDuration);
        dest.writeString(image_640px);
        dest.writeString(image_200px);
    }

    public static final Parcelable.Creator<SimpleTrack> CREATOR
            = new Parcelable.Creator<SimpleTrack>() {
        public SimpleTrack createFromParcel(Parcel in) {
            return new SimpleTrack(in);
        }

        public SimpleTrack[] newArray(int size) {
            return new SimpleTrack[size];
        }
    };

    private SimpleTrack(Parcel in) {
        artist = in.readString();
        id = in.readString();
        album = in.readString();
        track = in.readString();
        previewUrl = in.readString();
        msDuration = in.readLong();
        image_640px = in.readString();
        image_200px = in.readString();
    }

    public String getArtist() {
        return artist;
    }

    public String getId() {
        return id;
    }

    public String getAlbum() {
        return album;
    }

    public String getTrack() {
        return track;
    }

    public String getImage_200px() {
        return image_200px;
    }

    public String getImage_640px() {
        return image_640px;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public Long getMsDuration() {
        return msDuration;
    }
}
