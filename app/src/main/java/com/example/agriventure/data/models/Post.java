package com.example.agriventure.data.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Post implements Parcelable {
    public String title;
    public String date;
    public String body;

    public Post() {
    }

    protected Post(Parcel in) {
        title = in.readString();
        date = in.readString();
        body = in.readString();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public String getPost_title() {
        return title;
    }

    public String getPost_date() {
        return date;
    }

    public String getPost_body() {
        return body;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(date);
        dest.writeString(body);
    }
}
