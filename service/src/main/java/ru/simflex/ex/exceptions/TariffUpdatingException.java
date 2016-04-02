package ru.simflex.ex.exceptions;

/**
 * Custom exception for Tariff class.
 */
public class TariffUpdatingException extends GenericException {
    public TariffUpdatingException(String message) {
        super(message);
    }

    public TariffUpdatingException(String message, Exception e) {
        super(message, e);
    }
}
