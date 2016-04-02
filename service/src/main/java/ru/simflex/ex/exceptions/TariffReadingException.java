package ru.simflex.ex.exceptions;

/**
 * Custom exception for Tariff class.
 */
public class TariffReadingException extends GenericException {
    public TariffReadingException(String message) {
        super(message);
    }

    public TariffReadingException(String message, Exception e) {
        super(message, e);
    }
}
