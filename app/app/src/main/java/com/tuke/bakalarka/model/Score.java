package com.tuke.bakalarka.model;

public class Score {
    private Competitor competitor;
    private Station station;
    private int points;

    public Score(Station station, Competitor competitor, int points) {
        this.station = station;
        this.competitor = competitor;
        this.points = points;
    }


}
