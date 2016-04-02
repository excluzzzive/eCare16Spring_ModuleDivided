package ru.simflex.ex.exceptions;

/**
 * Custom exception for Option class.
 */
public class ContractReadingException extends GenericException {
    public ContractReadingException(String message) {
        super(message);
    }

    public ContractReadingException(String message, Exception e) {
        super(message, e);
    }
}
