package sk.tuke.backend.service;

public class CompetitorException extends RuntimeException {
    public CompetitorException(String message) {
        super(message);
    }

    public CompetitorException(String message, Throwable cause) {
        super(message, cause);
    }
}
