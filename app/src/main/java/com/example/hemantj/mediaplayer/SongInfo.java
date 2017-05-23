package com.example.hemantj.mediaplayer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HemantJ on 21/05/17.
 */

class SongInfo implements Parcelable {

    private String songPath;
    private String songName;
    private String albumName;
    private String artistName;
    private String a1,a2,a3,a4;

    public SongInfo(String songPath, String songName, String albumName, String artistName) {
        this.songPath = songPath;
        this.songName = songName;
        this.albumName = albumName;
        this.artistName = artistName;
    }

    public String getSongName(){
        return songName;
    }

    public String getArtistName(){
        return artistName;
    }

    public String getSongPath(){
        return songPath;
    }

    public String getAlbumName() {
        return albumName;
    }

    //The following methods are required for Parcelable implementation of an ArrayList

    private SongInfo(Parcel parcel){
        a1 = parcel.readString();
        a2 = parcel.readString();
        a3 = parcel.readString();
        a4 = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(a1);
        dest.writeString(a2);
        dest.writeString(a3);
        dest.writeString(a4);
    }

    public static final Parcelable.Creator<SongInfo> CREATOR = new Parcelable.Creator<SongInfo>() {
        public SongInfo createFromParcel(Parcel in) {
            return new SongInfo(in);
        }

        public SongInfo[] newArray(int size) {
            return new SongInfo[size];
        }
    };
}
