package ru.simflex.ex.dao.interfaces;

import ru.simflex.ex.entities.Contract;
import ru.simflex.ex.entities.Option;
import ru.simflex.ex.entities.Tariff;

import java.util.List;

/**
 * Option DAO interface.
 */
public interface OptionDao extends GenericDao<Option> {

    /**
     * Returns an Option object by name.
     * @param name Option name
     * @return Option object
     */
    Option getOptionByName(String name);

    /**
     * Checks whether the option is used by any tariff.
     * @param id Id of option
     * @return List of Tariffs option is used by
     */
    List<Tariff> getOptionUsedByTariffList(Integer id);

    /**
     * Checks whether the option is used by any contract.
     * @param id Id of option
     * @return List of Contracts option is used by
     */
    List<Contract> getOptionUsedByContractList(Integer id);

    /**
     * Checks whether the option is used by any other option as joint option.
     * @param id Id of option
     * @return List of Options
     */
    List<Option> getOptionUsedAsJointOptionList(Integer id);

}
