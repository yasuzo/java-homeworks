package hr.fer.zemris.java.hw15.dao;

public class DAOException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }

    public DAOException(String message) {
        super(message);
    }
}