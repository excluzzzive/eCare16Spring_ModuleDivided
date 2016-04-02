package ru.simflex.ex.services.interfaces;

import ru.simflex.ex.entities.Tariff;
import ru.simflex.ex.webservices.entities.WSTariff;

import java.util.List;
import java.util.Set;

/**
 * Tariff service interface.
 */
public interface TariffService {

    /**
     * Method that returns a List of Tariff objects.
     *
     * @return List of Tariff objects
     */
    List<Tariff> getTariffList();

    /**
     * Method that returns a Tariff object by ID.
     *
     * @param idString Id of tariff
     * @return Tariff object
     */
    Tariff getTariffById(String idString);

    /**
     * Returns a tariff object from DB by name.
     *
     * @param name Name of tariff
     * @return Tariff object
     */
    Tariff getTariffByName(String name);

    /**
     * Creates a tariff in DB from tariff object.
     *
     * @param newTariff Tariff object
     */
    void createTariff(Tariff newTariff);

    /**
     * Updated a tariff in DB with specified tariff.
     *
     * @param editedTariff Tariff object
     */
    void updateTariff(Tariff editedTariff);

    /**
     * Deletes a tariff by id.
     *
     * @param idString Id of a tariff
     */
    void deleteTariffById(String idString);

    /**
     * Method for web service returning list of WSTariff objects.
     *
     * @return List of WSTariff objects
     */
    List<WSTariff> getWSTariffList();



}
