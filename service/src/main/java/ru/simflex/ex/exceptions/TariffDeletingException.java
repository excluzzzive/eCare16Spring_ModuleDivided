package ru.simflex.ex.exceptions;

/**
 * Custom exception for Tariff class.
 */
public class TariffDeletingException extends GenericException {
    public TariffDeletingException(String message) {
        super(message);
    }

    public TariffDeletingException(String message, Exception e) {
        super(message, e);
    }
}
