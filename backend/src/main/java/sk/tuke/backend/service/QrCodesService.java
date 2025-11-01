package sk.tuke.backend.service;

public interface QrCodesService {
    boolean isUsed(int id) throws QrCodesException;
}
