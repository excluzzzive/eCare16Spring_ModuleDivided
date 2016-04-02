package ru.simflex.ex.dao.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.simflex.ex.dao.interfaces.UserDao;
import ru.simflex.ex.entities.Contract;
import ru.simflex.ex.entities.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * User DAO implementation class.
 */
@Repository
@Scope(value = "singleton")
public class UserDaoImplementation extends GenericDaoImplementation<User> implements UserDao {

    /**
     * EntityManager instance.
     */
    @Autowired
    private EntityManager entityManager;

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public User authUser(String email, String password) {
        User result = null;
        String queryString = "SELECT u FROM User u WHERE u.email=:email AND u.password=:password";
        Query query = entityManager.createQuery(queryString);
        query.setParameter("email", email);
        query.setParameter("password", password);
        List<User> resultList = query.getResultList();
        if (!resultList.isEmpty()) {
            result = resultList.get(0);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<User> getAllEntitiesSorted() {
        String queryString = "SELECT e FROM User e ORDER BY e.firstName ASC";
        Query query = entityManager.createQuery(queryString);
        List<User> result = query.getResultList();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Boolean isUserUsed(Integer id) {
        Query query = entityManager.createQuery("SELECT c FROM Contract c JOIN c.user u WHERE u.id = :id");
        query.setParameter("id", id);
        List<User> userList = query.getResultList();
        return !userList.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Contract> getUserContractListById(int id) {
        Query query = entityManager.createQuery("SELECT c FROM Contract c JOIN c.user u WHERE u.id = :id");
        query.setParameter("id", id);
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    public User getUserByPhoneNumber(String phoneNumberString) {
        Query query = entityManager.createQuery("SELECT c FROM Contract c JOIN c.phoneNumber p "
                + "WHERE p.phoneNumberString = :phoneNumberString");
        query.setParameter("phoneNumberString", phoneNumberString);
        Contract contract = (Contract) query.getSingleResult();
        return contract.getUser();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public User getUserByPassportData(String passportData) {
        User result;
        Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.passportData = :passportData");
        query.setParameter("passportData", passportData);
        List<User> userList = query.getResultList();
        if (userList.isEmpty()) {
            result = null;
        } else {
            result = userList.get(0);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public User getUserByEmail(String email) {
        User result;
        Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email");
        query.setParameter("email", email);
        List<User> userList = query.getResultList();
        if (userList.isEmpty()) {
            result = null;
        } else {
            result = userList.get(0);
        }
        return result;
    }
}
