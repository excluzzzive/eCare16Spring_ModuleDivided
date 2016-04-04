package ru.simflex.ex.dao.interfaces;

import ru.simflex.ex.entities.Tariff;

import java.util.List;

/**
 * Tariff DAO interface.
 */
public interface TariffDao extends GenericDao<Tariff> {

    /**
     * Checks whether the tariff is blocked.
     *
     * @param id Id of tariff
     * @return Boolean flag
     */
    boolean isTariffUsed(Integer id);

    /**
     * Returns a tariff object from DB by name.
     * @param name Name of tariff
     * @return Tariff object
     */
    Tariff getTariffByName(String name);

}
