package com.tuke.bakalarka;

import android.app.Application;

public class StationId extends Application {
    private int id = 1;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
