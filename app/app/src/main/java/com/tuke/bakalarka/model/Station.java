package com.tuke.bakalarka.model;

import java.util.List;

public class Station {

    private Long id;
    private String name;



    public Station(Long id) {
        this.id = id;
    }
    public Station(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
