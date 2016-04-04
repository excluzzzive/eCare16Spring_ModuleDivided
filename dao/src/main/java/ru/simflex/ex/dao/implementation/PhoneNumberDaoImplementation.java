package ru.simflex.ex.dao.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import ru.simflex.ex.dao.interfaces.PhoneNumberDao;
import ru.simflex.ex.entities.PhoneNumber;
import ru.simflex.ex.constants.Attributes;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Phone number DAO implementation class.
 */
@Repository
@Scope(value = "singleton")
public class PhoneNumberDaoImplementation extends GenericDaoImplementation<PhoneNumber> implements PhoneNumberDao {

    /**
     * EntityManager instance.
     */
    @Autowired
    private EntityManager entityManager;

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<PhoneNumber> getAllEntitiesSorted() {
        String queryString = "SELECT e FROM PhoneNumber e ORDER BY e.phoneNumberString ASC";
        Query query = entityManager.createQuery(queryString);
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<PhoneNumber> getAvailablePhoneNumberList() {
        String queryString = "SELECT e FROM PhoneNumber e WHERE e.available = true ORDER BY e.phoneNumberString ASC";
        Query query = entityManager.createQuery(queryString);
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public PhoneNumber getPhoneNumberByString(String phoneNumberString) {
        Query query = entityManager.createQuery("SELECT p FROM PhoneNumber p " +
                "WHERE p.phoneNumberString = :phoneNumberString");
        query.setParameter("phoneNumberString", phoneNumberString);
        List<PhoneNumber> phoneNumberList = query.getResultList();

        return phoneNumberList.isEmpty() ? null : phoneNumberList.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<PhoneNumber> getPhoneNumberListByPage(int page) {
        Query query = entityManager.createQuery("SELECT p FROM PhoneNumber p ORDER BY p.phoneNumberString ASC");
        query.setFirstResult(page * Attributes.LIMIT_PHONE_NUMBERS_PER_PAGE);
        query.setMaxResults(Attributes.LIMIT_PHONE_NUMBERS_PER_PAGE);
        return query.getResultList();
    }
}
