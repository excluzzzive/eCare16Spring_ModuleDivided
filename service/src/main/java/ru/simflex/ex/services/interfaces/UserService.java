package ru.simflex.ex.services.interfaces;

import ru.simflex.ex.entities.Contract;
import ru.simflex.ex.entities.User;

import java.util.List;

/**
 * User service interface.
 */
public interface UserService {

    /**
     * Authenticate user by email and password.
     *
     * @param email    User email (login)
     * @param password User password
     * @return User if authenticate succeeded or null if not
     */
    User authUser(String email, String password);

    /**
     * Returns list of User objects.
     *
     * @return List of User objects
     */
    List<User> getUserList();

    /**
     * Returns a User object from DB found by ID.
     *
     * @param idString User id
     * @return User object
     */
    User getUserById(String idString);

    /**
     * Returns an owner of the PhoneNumber by phoneNumberString.
     *
     * @param phoneNumberString Text form of phone number
     * @return User - owner of PhoneNumber
     */
    User getUserByPhoneNumber(String phoneNumberString);

    /**
     * Returns list of contracts belongs to a User.
     * @param idString id of User
     * @return List of Contract objects
     */
    List<Contract> getUserContractListById(String idString);

    /**
     * Checks whether the User has a contract.
     *
     * @param id User id
     * @return Boolean flag
     */
    boolean isUserUsed(Integer id);

    /**
     * Create a user with entries.
     *
     * @param newUser User entity to persist in DB
     */
    void createUser(User newUser);

    /**
     * Update a user with entries. Can change employee flag only if it is another user.
     *
     * @param editedUser User entity to persist in DB
     */
    void updateUser(User employee, User editedUser);

    /**
     * Deletes a User from database.
     *
     * @param employee   User who make changes
     * @param editedUserIdString User id to delete
     */
    void deleteUserById(User employee, String editedUserIdString);

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
