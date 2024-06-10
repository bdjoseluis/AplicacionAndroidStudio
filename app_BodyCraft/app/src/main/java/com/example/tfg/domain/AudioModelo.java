package com.example.tfg.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class AudioModelo implements Serializable, Parcelable  {
    String path;
    String title;
    String duration;

    public AudioModelo(String path, String title, String duration) {
        this.path = path;
        this.title = title;
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    protected AudioModelo(Parcel in) {
        path = in.readString();
        title = in.readString();
        duration = in.readString();
    }
    public static final Parcelable.Creator<AudioModelo> CREATOR = new Parcelable.Creator<AudioModelo>() {
        @Override
        public AudioModelo createFromParcel(Parcel in) {
            return new AudioModelo(in);
        }

        @Override
        public AudioModelo[] newArray(int size) {
            return new AudioModelo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeString(title);
        dest.writeString(duration);
    }

}
