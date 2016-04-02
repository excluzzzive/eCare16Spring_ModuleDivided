package ru.simflex.ex.services.interfaces;


import ru.simflex.ex.entities.Contract;
import ru.simflex.ex.entities.Option;
import ru.simflex.ex.entities.PhoneNumber;
import ru.simflex.ex.entities.Tariff;
import ru.simflex.ex.entities.User;
import ru.simflex.ex.webservices.entities.WSUser;

import java.util.List;
import java.util.Set;

/**
 * Contract service interface.
 */
public interface ContractService {

    /**
     * Method that return all instances of Contracts.
     *
     * @return List of Contract objects
     */
    List<Contract> getContractList();

    /**
     * Method that return all instances of Contracts.
     *
     * @param user Owner of a contract
     * @return List of Contract objects
     */
    List<Contract> getContractListByUser(User user);

    /**
     * Method that return all instances of Contracts.
     *
     * @param idString Tariff id to search contracts
     * @return List of Contract objects
     */
    List<Contract> getContractListByTariffId(String idString);


    /**
     * Method that returns a Contract object by ID.
     *
     * @param idString Id of contract
     * @return Tariff object
     */
    Contract getContractById(String idString);

    /**
     * Method that returns a Contract object by User and ID.
     *
     * @param user     User - owner of contract
     * @param idString Id of contract
     * @return Tariff object
     */
    Contract getContractByUserAndId(User user, String idString);

    /**
     * Reverses block state.
     *
     * @param user     User who blocked contract
     * @param idString Id of contract which will be changed block state
     */
    void changeBlockState(User user, String idString);

    /**
     * Creates a contract in DB with specified Contract object.
     *
     * @param newContract Contract object
     */
    void createContract(Contract newContract);

    /**
     * @param editedContract Contract object to be updated
     */
    void updateContract(Contract editedContract);

    /**
     * Deletes a contract from DB and puts phone number in available mode.
     *
     * @param idString Id of a contract to delete
     */
    void deleteContractById(String idString);

    /**
     * Checks whether contract is blocked. If it is - throws an exception.
     *
     * @param idString Id of contract
     */
    void checkContractBlocked(String idString);

    /**
     * Method for web service. Return list of special web service user objects by tariff name.
     * @param idString Id of tariff
     * @return List of WSUser object
     */
    List<WSUser> getWSUserListByTariffId(String idString);
}
