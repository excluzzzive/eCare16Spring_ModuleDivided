package ru.simflex.ex.exceptions;

/**
 * Custom exception for Tariff class.
 */
public class TariffCreatingException extends GenericException {
    public TariffCreatingException(String message) {
        super(message);
    }

    public TariffCreatingException(String message, Exception e) {
        super(message, e);
    }
}
