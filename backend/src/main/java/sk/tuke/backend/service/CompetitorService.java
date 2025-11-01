package sk.tuke.backend.service;

import sk.tuke.backend.entity.Competitor;

public interface CompetitorService {
    Competitor register(Competitor competitor) throws CompetitorException;
    Competitor findById(int id)  throws CompetitorException;
    void reset() throws CompetitorException;
}
