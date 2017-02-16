package com.example.chaojung.todolist;

import java.io.Serializable;

/**
 * Created by ChaoJung on 2017/1/13.
 */

public class Item implements Serializable {

    private static final long serialVersionUID = 1L;
    private String text = "";
    private int position;

    public Item(String text, int position) {
        this.text = text;
        this.position = position;
    }

    public String getText() {
        return text;
    }

    public int getPosition() {
        return position;
    }
}
