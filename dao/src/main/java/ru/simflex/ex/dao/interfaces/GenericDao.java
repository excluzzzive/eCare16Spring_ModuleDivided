package ru.simflex.ex.dao.interfaces;

import java.util.List;

/**
 * Generic DAO interface.
 * @param <E> Generic entity type.
 */
public interface GenericDao<E> {



    /**
     * Persists an entity to DB.
     * @param entity object to persist
     */
    void create(E entity);

    /**
     * Reads an entity from DB by id.
     * @param id Id of object to find
     * @return Returns generic object
     */
    E read(Integer id);

    /**
     * Updates an entity in DB.
     * @param entity object to be updated
     */
    void update(E entity);

    /**
     * Deletes an entity from DB.
     * @param entity object to delete
     */
    void delete(E entity);

    /**
     * Deletes an entity by object id.
     * @param id Id of object to delete
     */
    void deleteById(Integer id);

    /**
     * Get List of all <E> entities from DB.
     * @return List of <E>
     */
    List<E> getAllEntities();

    /**
     * Returns a quantity of entities in DB.
     * @return Quantity of entities.
     */
    long getEntityCount();

}
