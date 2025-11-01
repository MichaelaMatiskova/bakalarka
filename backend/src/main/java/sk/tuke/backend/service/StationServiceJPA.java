package sk.tuke.backend.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sk.tuke.backend.entity.Competitor;
import sk.tuke.backend.entity.Station;

@Service
@Transactional
public class StationServiceJPA implements StationService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addStation(Station station) throws StationException {
        entityManager.persist(station);
    }

    @Override
    public void reset() throws StationException {
        entityManager.createQuery("DELETE FROM Station").executeUpdate();
    }

    @Override
    public Station findById(int id) throws CompetitorException {
        try {
            return entityManager.createQuery(
                            "SELECT s FROM Station s WHERE s.id = :id", Station.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new StationException("Station not found with id: " + id);
        }
    }

}
