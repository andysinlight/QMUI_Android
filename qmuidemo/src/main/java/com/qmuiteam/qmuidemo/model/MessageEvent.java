package com.qmuiteam.qmuidemo.model;

/**
 * Created by andy on 2017/9/1.
 */

public class MessageEvent {
    public static final int NEW_NOTIFICATION = 1000;

    private int type;
    private Object mObjects;

    public MessageEvent(int type, Object objects) {
        this.type = type;
        mObjects = objects;
    }

    public int getType() {
        return type;
    }

    public Object getObject() {
        return mObjects;
    }
}

