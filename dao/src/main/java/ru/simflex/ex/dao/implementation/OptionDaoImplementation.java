package ru.simflex.ex.dao.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import ru.simflex.ex.dao.interfaces.OptionDao;
import ru.simflex.ex.entities.Contract;
import ru.simflex.ex.entities.Option;
import ru.simflex.ex.entities.Tariff;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Option DAO implementation class.
 */
@Repository
@Scope(value = "singleton")
public class OptionDaoImplementation extends GenericDaoImplementation<Option> implements OptionDao {

    /**
     * EntityManager instance.
     */
    @Autowired
    private EntityManager entityManager;

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Option getOptionByName(String name) {
        Option result;
        Query query = entityManager.createQuery("SELECT o FROM Option o WHERE o.name = :name");
        query.setParameter("name", name);
        List<Option> optionList = query.getResultList();
        if (optionList.isEmpty()) {
            result = null;
        } else {
            result = optionList.get(0);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Tariff> getOptionUsedByTariffList(Integer id) {
        Query query = entityManager.createQuery("SELECT t FROM Tariff t JOIN t.possibleOptions o "
                + "WHERE o.id = :id ");
        query.setParameter("id", id);
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Contract> getOptionUsedByContractList(Integer id) {
        Query query = entityManager.createQuery("SELECT c FROM Contract c JOIN c.chosenOptions o "
                + "WHERE o.id = :id ");
        query.setParameter("id", id);
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Option> getOptionUsedAsJointOptionList(Integer id) {
        Query query = entityManager.createQuery("SELECT o FROM Option o JOIN o.jointOptions j "
                + "WHERE j.id = :id ");
        query.setParameter("id", id);
        return query.getResultList();
    }
}
