package ru.simflex.ex.dao.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import ru.simflex.ex.dao.interfaces.ContractDao;
import ru.simflex.ex.entities.Contract;
import ru.simflex.ex.entities.PhoneNumber;
import ru.simflex.ex.entities.Tariff;
import ru.simflex.ex.entities.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

/**
 * Contract DAO implementation class.
 */
@Repository
@Scope(value = "singleton")
public class ContractDaoImplementation extends GenericDaoImplementation<Contract> implements ContractDao {

    /**
     * EntityManager instance.
     */
    @Autowired
    private EntityManager entityManager;

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Contract> getAllEntitiesByUser(User user) {
        Query query = entityManager.createQuery("SELECT e FROM Contract e " +
                "WHERE e.user = :user ORDER BY e.phoneNumber.phoneNumberString ASC");
        query.setParameter("user", user);
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Contract> getContractListByTariffId(int id) {
        Query query = entityManager.createQuery("SELECT c FROM Contract c JOIN c.tariff t " +
                "WHERE t.id = :id ORDER BY c.phoneNumber.phoneNumberString");
        query.setParameter("id", id);
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Contract> getContractListByTariffName(String tariffName) {
        Query query = entityManager.createQuery("SELECT c FROM Contract c JOIN c.tariff t " +
                "WHERE t.name = :name ORDER BY c.phoneNumber.phoneNumberString");
        query.setParameter("name", tariffName);
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Contract getContractByUserAndId(User user, int id) {
        Query query = entityManager.createQuery("SELECT c FROM Contract c JOIN c.user u " +
                "WHERE u.id = :userId AND c.id = :contractId");
        query.setParameter("userId", user.getId());
        query.setParameter("contractId", id);
        List<Contract> contractList = query.getResultList();

        return contractList.isEmpty() ? null : contractList.get(0);
    }

    /**
     * Persists a Contract object into DB.
     *
     * @param contract Contract to be persisted
     */
    public void create(Contract contract) {
        entityManager.persist(contract);
    }

    /**
     * Deletes a contract from DB and puts phone number in available mode.
     *
     * @param contract Contract to be deleted
     */
    public void delete(Contract contract) {
        entityManager.remove(entityManager.merge(contract));
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Contract> getAllEntitiesSorted() {
        String queryString = "SELECT e FROM Contract e ORDER BY e.phoneNumber.phoneNumberString ASC";
        Query query = entityManager.createQuery(queryString);
        return query.getResultList();
    }
}
