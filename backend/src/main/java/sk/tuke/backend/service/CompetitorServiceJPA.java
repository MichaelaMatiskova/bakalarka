package sk.tuke.backend.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.tuke.backend.entity.Competitor;
import sk.tuke.backend.entity.QrCodes;

@Service
@Transactional
@Primary
public class CompetitorServiceJPA implements  CompetitorService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Competitor register(Competitor competitor) throws CompetitorException {
        QrCodes qrCodes = entityManager.createQuery("SELECT qr FROM QrCodes qr where qr.id=:id", QrCodes.class)
                .setParameter("id", competitor.getId()).getSingleResult();

        qrCodes.setUsed(true);

        entityManager.persist(competitor);
        return competitor;
    }

    @Override
    public Competitor findById(int id) throws CompetitorException {
        try {
            return entityManager.createQuery(
                            "SELECT c FROM Competitor c WHERE c.id = :id", Competitor.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new CompetitorException("Competitor not found with id: " + id);
        }
    }

    @Override
    public void reset() throws CompetitorException {
        entityManager.createQuery("DELETE FROM Competitor").executeUpdate();
    }
}
