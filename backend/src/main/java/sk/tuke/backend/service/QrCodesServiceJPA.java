package sk.tuke.backend.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import sk.tuke.backend.entity.QrCodes;

public class QrCodesServiceJPA implements QrCodesService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean isUsed(int id) throws QrCodesException {
        try {
            QrCodes qrCodes = entityManager.createQuery("SELECT qr FROM QrCodes qr where qr.id=:id", QrCodes.class)
                    .setParameter("id", id).getSingleResult();

            return qrCodes.isUsed();

        } catch (NoResultException e) {
            throw new CompetitorException("ID not found");
        }
    }
}
