package sk.tuke.backend.service;

import sk.tuke.backend.entity.Station;

public interface StationService {
    void addStation(Station station) throws StationException;
    Station findById(int id) throws CompetitorException;
    void reset() throws StationException;
}
