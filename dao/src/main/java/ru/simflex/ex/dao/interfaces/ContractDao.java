package ru.simflex.ex.dao.interfaces;

import ru.simflex.ex.entities.Contract;
import ru.simflex.ex.entities.Tariff;
import ru.simflex.ex.entities.User;

import java.util.List;

/**
 * Contract DAO interface.
 */
public interface ContractDao extends GenericDao<Contract> {

    /**
     * Returns a list of Contracts that belong to a User.
     *
     * @param user Owner of contracts
     * @return List of contracts owned by a User
     */
    List<Contract> getAllEntitiesByUser(User user);

    /**
     * Method that return all instances of Contracts.
     *
     * @param id Tariff id to search contracts
     * @return List of Contract objects
     */
    List<Contract> getContractListByTariffId(int id);

    /**
     * Method that return all instances of Contracts.
     *
     * @param tariffName Tariff name to search contracts
     * @return List of Contract objects
     */
    List<Contract> getContractListByTariffName(String tariffName);

    /**
     * Method that returns a Contract object by User and ID.
     *
     * @param user User - owner of contract
     * @param id Id of contract
     * @return Tariff object
     */
    Contract getContractByUserAndId(User user, int id);

    /**
     * Returns sorted list of contracts.
     *
     * @return Contract list
     */
    List<Contract> getAllEntitiesSorted();
}
