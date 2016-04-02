package ru.simflex.ex.dao.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import ru.simflex.ex.dao.interfaces.GenericDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Generic DAO implementation abstract class.
 */
public abstract class GenericDaoImplementation<E> implements GenericDao<E> {

    /**
     * EntityManager instance.
     */
    @Autowired
    private EntityManager entityManager;

    /**
     * Type of entity.
     * @param E E generic entity
     */
    protected Class<E> entityType;

    /**
     * public GenericDaoImplementation() constructor.
     */
    @SuppressWarnings("unchecked")
    public GenericDaoImplementation() {
        ParameterizedType type = ((ParameterizedType) getClass().getGenericSuperclass());
        entityType = (Class<E>) type.getActualTypeArguments()[0];
    }

    /**
     * {@inheritDoc}
     */
    public void create(E entity) {
        entityManager.persist(entity);
    }

    /**
     * {@inheritDoc}
     */
    public E read(Integer id) {
        E result = entityManager.find(entityType, id);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public void update(E entity) {
        entityManager.merge(entity);
    }

    /**
     * {@inheritDoc}
     */
    public void delete(E entity) {
        entityManager.remove(entityManager.merge(entity));
    }

    /**
     * {@inheritDoc}
     */
    public void deleteById(Integer id) {
        E entity = read(id);
        delete(entity);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<E> getAllEntities() {
        Query query = entityManager.createQuery("SELECT e FROM " + entityType.getSimpleName() + " e");
        List<E> result = query.getResultList();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public long getEntityCount() {
        Query query = entityManager.createQuery("SELECT count(e) FROM " + entityType.getSimpleName() + " e");
        List resultList = query.getResultList();
        Long result = (Long) resultList.get(0);
        return result;
    }
}
