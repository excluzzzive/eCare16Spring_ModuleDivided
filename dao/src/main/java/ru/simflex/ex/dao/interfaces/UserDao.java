package ru.simflex.ex.dao.interfaces;

import ru.simflex.ex.entities.Contract;
import ru.simflex.ex.entities.User;

import java.util.List;

/**
 * User DAO interface.
 */
public interface UserDao extends GenericDao<User> {

    /**
     * Authentication method for a login operation.
     *
     * @param email    Email represents a login which was entered by a client
     * @param password Password, which was entered by a client
     * @return User from DB or null if User does not exist
     */
    User authUser(String email, String password);

    /**
     * Returns sorted list of PhoneNumbers. Sorted by phoneNumberString field.
     *
     * @return Sorted list of PhoneNumbers
     */
    List<User> getAllEntitiesSorted();

    /**
     * Checks whether the User has a contract.
     *
     * @param id User id
     * @return Boolean flag
     */
    Boolean isUserUsed(Integer id);

    /**
     * Returns list of contracts belongs to a User.
     * @param id id of User
     * @return List of Contract objects
     */
    List<Contract> getUserContractListById(int id);



    /**
     * Returns an owner of the PhoneNumber by phoneNumberString.
     *
     * @param phoneNumberString Phone number text form that belongs to user
     * @return User - owner of PhoneNumber
     */
    User getUserByPhoneNumber(String phoneNumberString);

    /**
     * Checks whether email is present in DB or not.
     * @param email Email to check
     * @return User
     */
    User getUserByEmail(String email);

    /**
     * Checks whether passport data is present in DB or not.
     * @param passportData Data to check
     * @return User
     */
    User getUserByPassportData(String passportData);

}
