package ru.simflex.ex.services.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.simflex.ex.annotations.Loggable;
import ru.simflex.ex.dao.interfaces.UserDao;
import ru.simflex.ex.entities.Contract;
import ru.simflex.ex.entities.User;
import ru.simflex.ex.exceptions.UserCreatingException;
import ru.simflex.ex.exceptions.UserDeletingException;
import ru.simflex.ex.exceptions.UserReadingException;
import ru.simflex.ex.exceptions.UserUpdatingException;
import ru.simflex.ex.services.interfaces.UserService;

import java.util.*;

/**
 * User service implementation class.
 */
@Service
@Scope(value = "singleton")
public class UserServiceImplementation implements UserService {

    /**
     * UserDao instance.
     */
    @Autowired
    private UserDao userDao;

    /**
     * UserDao setter.
     *
     * @param userDao UserDao object
     */
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public User authUser(String email, String password) {
        try {
            if (email != null) {
                email = email.toLowerCase();
            }
            return userDao.authUser(email, password);
        } catch (Exception e) {
            throw new UserReadingException("Something gone wrong, try again or contact system administrator!", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<User> getUserList() {

        List<User> userList;
        try {
            userList = userDao.getAllEntitiesSorted();

            for (User user : userList) {
                user.setPassword("");
            }
        } catch (Exception e) {
            throw new UserReadingException("Something gone wrong, try again or contact system administrator!", e);
        }
        return userList;
    }

    /**
     * {@inheritDoc}
     */
    public User getUserById(String idString) {

        User user;
        try {
            int id = Integer.parseInt(idString);
            user = userDao.read(id);
        } catch (Exception e) {
            throw new UserReadingException("User not found!", e);
        }

        return user;

    }

    /**
     * {@inheritDoc}
     */
    public User getUserByPhoneNumber(String phoneNumberString) {

        User user;
        try {
            user = userDao.getUserByPhoneNumber(phoneNumberString);
        } catch (Exception e) {
            throw new UserReadingException("User not found or incorrect phone number!", e);
        }

        return user;
    }

    /**
     * {@inheritDoc}
     */
    public List<Contract> getUserContractListById(String idString) {
        try {
            int id = Integer.parseInt(idString);
            return userDao.getUserContractListById(id);
        } catch (Exception e) {
            throw new UserReadingException("Something gone wrong, try again or contact system administrator!", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public boolean isUserUsed(Integer id) {
        try {
            return userDao.isUserUsed(id);
        } catch (Exception e) {
            throw new UserReadingException("Something gone wrong, try again or contact system administrator!", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Loggable
    public void createUser(User newUser) {

        newUser.setEmail(newUser.getEmail().toLowerCase());

        try {
            userDao.create(newUser);
        } catch (Exception e) {
            throw new UserCreatingException("Something gone wrong, try again or contact system administrator!", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Loggable
    public void updateUser(User employee, User editedUser) {

        editedUser.setEmail(editedUser.getEmail().toLowerCase());
        User originalUser;

        try {
            originalUser = userDao.read(editedUser.getId());
        } catch (Exception e) {
            throw new UserReadingException("Something gone wrong, try again or contact system administrator!");
        }

        if (originalUser == null) {
            throw new UserUpdatingException("User not found!");
        }

        //User cannot disable his own employee functions
        if (employee.getId() == editedUser.getId() && editedUser.isEmployee()) {
            throw new UserUpdatingException("Trying to disable your own employee functions!");
        }

        //If password is empty leave it as in original one.
        if (editedUser.getPassword() == null || editedUser.getPassword().isEmpty()) {
            editedUser.setPassword(originalUser.getPassword());
        }

        try {
            userDao.update(editedUser);
        } catch (Exception e) {
            throw new UserUpdatingException("Something gone wrong, try again or contact system administrator!", e);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Loggable
    public void deleteUserById(User employee, String editedUserIdString) {

        List<Contract> userContractList = getUserContractListById(editedUserIdString);
        Integer editedUserId = Integer.parseInt(editedUserIdString);

        //User cannot delete himself
        if (employee.getId() != editedUserId) {

            //And user cannot be deleted if he own any contract
            if (userContractList == null || userContractList.isEmpty()) {
                try {
                    userDao.deleteById(editedUserId);
                } catch (Exception e) {
                    throw new UserDeletingException("Trying to delete invalid user!", e);
                }
            } else {
                throw new UserDeletingException("Trying to delete used user!");
            }
        } else {
            throw new UserDeletingException("Trying to delete yourself!");
        }
    }

    /**
     * {@inheritDoc}
     */
    public User getUserByPassportData(String passportData) {
        try {
            return userDao.getUserByPassportData(passportData);
        } catch (Exception e) {
            throw new UserReadingException("Something gone wrong, try again or contact system administrator!", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public User getUserByEmail(String email) {
        try {
            return userDao.getUserByEmail(email);
        } catch (Exception e) {
            throw new UserReadingException("Something gone wrong, try again or contact system administrator!", e);
        }
    }
}

