package ru.simflex.ex.exceptions;

/**
 * Custom exception for Option class.
 */
public class ContractUpdatingException extends GenericException {
    public ContractUpdatingException(String message) {
        super(message);
    }

    public ContractUpdatingException(String message, Exception e) {
        super(message, e);
    }
}
