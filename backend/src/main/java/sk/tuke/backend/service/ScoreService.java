package sk.tuke.backend.service;

import sk.tuke.backend.entity.Score;

public interface ScoreService {
    Score addScore(Score score) throws ScoreException;
    void reset() throws ScoreException;
    boolean existsAddScoreCompetitorAndStation(Score score);
}
