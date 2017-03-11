package com.codepath.apps.simpletweets.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by ChaoJung on 2017/3/11.
 */

@Parcel
public class Users {

    public Users() {
    }

    public List<User> getUsers() {
        return users;
    }

    public long getNextCursor() {
        return nextCursor;
    }

    @SerializedName("users")
    @Expose
    public List<User> users = null;
    @SerializedName("next_cursor")
    @Expose
    public long nextCursor;
}
