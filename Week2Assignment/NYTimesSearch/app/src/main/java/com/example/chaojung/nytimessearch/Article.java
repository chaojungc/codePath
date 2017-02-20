package com.example.chaojung.nytimessearch;

import java.util.ArrayList;

/**
 * Created by ChaoJung on 2017/2/20.
 */

public class Article {

    String webUrl;
    String headline;
    String thumbNail;

    private String mName;
    private boolean mOnline;

    public Article(String name, boolean online) {
        mName = name;
        mOnline = online;
    }

    public String getName() {
        return mName;
    }

    public boolean isOnline() {
        return mOnline;
    }

    private static int lastContactId = 0;

    public static ArrayList<Article> createContactsList(int numContacts) {
        ArrayList<Article> contacts = new ArrayList<Article>();

        for (int i = 1; i <= numContacts; i++) {
            contacts.add(new Article("Person " + ++lastContactId, i <= numContacts / 2));
        }

        return contacts;
    }




}
