package ru.simflex.ex.dao.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import ru.simflex.ex.dao.interfaces.TariffDao;
import ru.simflex.ex.entities.Contract;
import ru.simflex.ex.entities.Tariff;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Tariff DAO implementation class.
 */
@Repository
@Scope(value = "singleton")
public class TariffDaoImplementation extends GenericDaoImplementation<Tariff> implements TariffDao {

    /**
     * EntityManager instance.
     */
    @Autowired
    private EntityManager entityManager;

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Boolean isTariffUsed(Integer id) {
        Query query = entityManager.createQuery("SELECT e FROM Contract e JOIN e.tariff t WHERE t.id = :id");
        query.setParameter("id", id);
        List<Contract> contractList = query.getResultList();
        return !contractList.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Tariff getTariffByName(String name) {
        Tariff result;
        Query query = entityManager.createQuery("SELECT t FROM Tariff t WHERE t.name = :name");
        query.setParameter("name", name);
        List<Tariff> tariffList = query.getResultList();
        if (tariffList.isEmpty()) {
            result = null;
        } else {
            result = tariffList.get(0);
        }
        return result;
    }
}
