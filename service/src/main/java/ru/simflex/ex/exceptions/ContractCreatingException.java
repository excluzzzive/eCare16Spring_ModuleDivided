package ru.simflex.ex.exceptions;

/**
 * Custom exception for Option class.
 */
public class ContractCreatingException extends GenericException {
    public ContractCreatingException(String message) {
        super(message);
    }

    public ContractCreatingException(String message, Exception e) {
        super(message, e);
    }
}
