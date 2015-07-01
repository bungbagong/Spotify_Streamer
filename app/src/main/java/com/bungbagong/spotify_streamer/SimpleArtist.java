package com.bungbagong.spotify_streamer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bungbagong on 1/7/2015.
 */
public class SimpleArtist implements Parcelable {
    private String id;
    private String name;
    private String image_640px;
    private String image_200px;


    public SimpleArtist(String id, String name, String image_640px, String image_200px){
        this.id = id;
        this.name = name;
        this.image_640px = image_640px;
        this.image_200px = image_200px;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(image_640px);
        dest.writeString(image_200px);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<SimpleArtist> CREATOR
            = new Parcelable.Creator<SimpleArtist>() {
        public SimpleArtist createFromParcel(Parcel in) {
            return new SimpleArtist(in);
        }

        public SimpleArtist[] newArray(int size) {
            return new SimpleArtist[size];
        }
    };

    private SimpleArtist(Parcel in) {
        id = in.readString();
        name = in.readString();
        image_640px = in.readString();
        image_200px = in.readString();
    }
}
