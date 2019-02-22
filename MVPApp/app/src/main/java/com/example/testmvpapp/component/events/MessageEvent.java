package com.example.testmvpapp.component.events;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class MessageEvent {

    public ArrayList<Bitmap> obj;

    public MessageEvent(ArrayList<Bitmap> origalBmp) {
        this.obj = origalBmp;
    }

    public void setObj(ArrayList<Bitmap> obj) {
        this.obj = obj;
    }

    public ArrayList<Bitmap> getObj() {
        return obj;
    }

}
