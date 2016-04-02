package ru.simflex.ex.services.interfaces;

import ru.simflex.ex.entities.Contract;
import ru.simflex.ex.entities.Option;
import ru.simflex.ex.entities.Tariff;

import java.util.List;
import java.util.Set;

/**
 * Option service interface.
 */
public interface OptionService {

    /**
     * Method that return all instancs of Options.
     *
     * @return List of Option objects
     */
    List<Option> getOptionList();

    /**
     * Create option by new option object.
     *
     * @param newOption Object to be persisted
     */
    void createOption(Option newOption);

    /**
     * Updates existing option by option object.
     *
     * @param updatedOption Object to be persisted
     */
    void updateOption(Option updatedOption);

    /**
     * Deletes Option from database.
     *
     * @param editedOption option to be deleted
     */
    void deleteOption(Option editedOption);

    /**
     * Deletes Option from database.
     *
     * @param idString Id of Option
     */
    void deleteOptionById(String idString);

    /**
     * Method converts array of ids of options into List of option objects.
     *
     * @param idsArray array of ids
     * @return List of Option objects
     */
    List<Option> optionIdsArrayToListOfOption(String[] idsArray);


    /**
     * Returns an Option object by id.
     *
     * @param idString Id of option
     * @return Option object
     */
    Option getOptionById(String idString);

    /**
     * Returns an Option object by name.
     *
     * @param name Option name
     * @return Option object
     */
    Option getOptionByName(String name);

    /**
     * Checks whether the option is used by any tariff.
     *
     * @param idString Id of option
     * @return List of Tariff option is used by
     */
    List<Tariff> getOptionUsedByTariffList(String idString);

    /**
     * Checks whether the option is used by any contract.
     *
     * @param idString Id of option
     * @return List of Contracts option is used by
     */
    List<Contract> getOptionUsedByContractList(String idString);

    /**
     * Checks whether the option is used by any other option as joint option.
     *
     * @param idString Id of option
     * @return List of Options
     */
    List<Option> getOptionUsedAsJointOptionList(String idString);

    /**
     * Check if list of options contains incompatible to each other option.
     *
     * @param chosenOptions list of options
     * @return Set of incompatible options
     */
    Set<Option> checkIncompatibleOptions(List<Option> chosenOptions);

    /**
     * Check if list of options contains option that need joint option.
     *
     * @param chosenOptions list of options
     * @return Set of need joint options
     */
    Set<Option> checkJointOptions(List<Option> chosenOptions);


}
