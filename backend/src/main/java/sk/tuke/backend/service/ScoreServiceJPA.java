package sk.tuke.backend.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sk.tuke.backend.entity.Competitor;
import sk.tuke.backend.entity.Score;

@Service
@Transactional
public class ScoreServiceJPA implements ScoreService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Score addScore(Score score) throws ScoreException {
        try {
            if (!existsAddScoreCompetitorAndStation(score)) {
                entityManager.persist(score);
                entityManager.flush();
                return score;
            } else {
                throw new ScoreException("Score already exists for this competitor and station");
            }
        } catch (PersistenceException e) {
        throw new ScoreException("Error while adding score", e);
        }
    }

    @Override
    public boolean existsAddScoreCompetitorAndStation(Score score) throws ScoreException {
        Long count = entityManager.createQuery(
                        "SELECT COUNT(s) FROM Score s WHERE s.station.id = :stationId AND s.competitor.id = :competitorId", Long.class)
                .setParameter("stationId", score.getStation().getId())
                .setParameter("competitorId", score.getCompetitor().getId())
                .getSingleResult();
        return count > 0;
    }

    @Override
    public void reset() throws ScoreException {
        entityManager.createQuery("DELETE FROM Score").executeUpdate();
    }
}
