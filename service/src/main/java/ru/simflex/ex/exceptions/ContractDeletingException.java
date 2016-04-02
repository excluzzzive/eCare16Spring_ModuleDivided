package ru.simflex.ex.exceptions;

/**
 * Custom exception for Option class.
 */
public class ContractDeletingException extends GenericException {
    public ContractDeletingException(String message) {
        super(message);
    }

    public ContractDeletingException(String message, Exception e) {
        super(message, e);
    }
}
