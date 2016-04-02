package ru.simflex.ex.services.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.simflex.ex.annotations.Loggable;
import ru.simflex.ex.dao.interfaces.OptionDao;
import ru.simflex.ex.dao.interfaces.TariffDao;
import ru.simflex.ex.entities.Contract;
import ru.simflex.ex.entities.Option;
import ru.simflex.ex.entities.Tariff;
import ru.simflex.ex.exceptions.OptionCreatingException;
import ru.simflex.ex.exceptions.OptionDeletingException;
import ru.simflex.ex.exceptions.OptionReadingException;
import ru.simflex.ex.exceptions.OptionUpdatingException;
import ru.simflex.ex.services.interfaces.OptionService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Option service implementation class.
 */
@Service
@Scope(value = "singleton")
public class OptionServiceImplementation implements OptionService {

    /**
     * Instance of OptionDao.
     */
    @Autowired
    private OptionDao optionDao;

    /**
     * Instance of TariffDao.
     */
    @Autowired
    private TariffDao tariffDao;

    /**
     * Setter for OptionDao field.
     *
     * @param optionDao OptionDao object
     */
    public void setOptionDao(OptionDao optionDao) {
        this.optionDao = optionDao;
    }

    /**
     * Setter for TariffDao field.
     *
     * @param tariffDao TariffDao object
     */
    public void setTariffDao(TariffDao tariffDao) {
        this.tariffDao = tariffDao;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public List<Option> getOptionList() {
        try {
            return optionDao.getAllEntities();
        } catch (Exception e) {
            throw new OptionReadingException("Something gone wrong, try again or contact system administrator!", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Loggable
    public void createOption(Option newOption) {

        try {
            optionDao.create(newOption);
            List<Option> incompatibleList = newOption.getIncompatibleOptions();

            if (incompatibleList != null && !incompatibleList.isEmpty()) {
                for (Option option : incompatibleList) {
                    option.getIncompatibleOptions().add(newOption);
                    optionDao.update(option);
                }
            }
        } catch (Exception e) {
            throw new OptionCreatingException("Something gone wrong, try again or contact system administrator!", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Loggable
    public void updateOption(Option updatedOption) {

        List<Contract> optionUsedByContractList;
        Option originalOption;
        try {
            optionUsedByContractList = optionDao.getOptionUsedByContractList(updatedOption.getId());
            originalOption = optionDao.read(updatedOption.getId());
        } catch (Exception e) {
            throw new OptionReadingException("Something gone wrong, try again or contact system administrator!", e);
        }

        /*If someone trying to fabricate request and to update option incompatible or joint lists which
        could not be updated, then throw exception.*/
        if (optionUsedByContractList != null && !optionUsedByContractList.isEmpty()) {

            for (Option option : updatedOption.getIncompatibleOptions()) {
                if (!originalOption.getIncompatibleOptions().contains(option)) {
                    throw new OptionUpdatingException("Trying to update option used by contract!");
                }
            }
            for (Option option : originalOption.getIncompatibleOptions()) {
                if (!updatedOption.getIncompatibleOptions().contains(option)) {
                    throw new OptionUpdatingException("Trying to update option used by contract!");
                }
            }
            for (Option option : updatedOption.getJointOptions()) {
                if (!originalOption.getJointOptions().contains(option)) {
                    throw new OptionUpdatingException("Trying to update option used by contract!");
                }
            }
            for (Option option : originalOption.getJointOptions()) {
                if (!updatedOption.getJointOptions().contains(option)) {
                    throw new OptionUpdatingException("Trying to update option used by contract!");
                }
            }

        }

        try {
            List<Option> incompatibleList = updatedOption.getIncompatibleOptions();
            List<Option> oldIncompatibleList = getOptionById(String.valueOf(updatedOption.getId()))
                    .getIncompatibleOptions();

            for (Option oldOption : oldIncompatibleList) {
                if (!incompatibleList.contains(oldOption)) {
                    oldOption.getIncompatibleOptions().remove(updatedOption);
                    optionDao.update(oldOption);
                }
            }

            for (Option newOption : incompatibleList) {
                if (!oldIncompatibleList.contains(newOption)) {
                    newOption.getIncompatibleOptions().add(updatedOption);
                    optionDao.update(newOption);
                }
            }
            optionDao.update(updatedOption);

        } catch (Exception e) {
            throw new OptionUpdatingException("Something gone wrong, try again or contact system administrator!", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Loggable
    public void deleteOption(Option editedOption) {

        List<Tariff> optionUsedByTariffList;
        List<Contract> optionUsedByContractList;
        List<Option> optionUsedAsJointOptionList;

        Integer editedOptionId = editedOption.getId();

        try {
            optionUsedByTariffList = optionDao.getOptionUsedByTariffList(editedOptionId);
            optionUsedByContractList = optionDao.getOptionUsedByContractList(editedOptionId);
            optionUsedAsJointOptionList = optionDao.getOptionUsedAsJointOptionList(editedOptionId);
        } catch (Exception e) {
            throw new OptionReadingException("Something gone wrong, try again or contact system administrator!", e);
        }

        Boolean isOptionDeletable = optionUsedByContractList.isEmpty() && optionUsedAsJointOptionList.isEmpty();

        if (isOptionDeletable) {

            try {
                if (optionUsedByTariffList != null && !optionUsedByTariffList.isEmpty()) {
                    for (Tariff tariff : optionUsedByTariffList) {
                        tariff.getPossibleOptions().remove(editedOption);
                        tariffDao.update(tariff);
                    }
                }

                List<Option> optionList = editedOption.getIncompatibleOptions();

                if (optionList != null && !optionList.isEmpty()) {
                    for (Option option : optionList) {
                        option.getIncompatibleOptions().remove(editedOption);
                        optionDao.update(option);
                    }
                }
            } catch (Exception e) {
                throw new OptionDeletingException("Something gone wrong, " +
                        "try again or contact system administrator!", e);
            }

            optionDao.delete(editedOption);

        } else {
            throw new OptionDeletingException("Illegal access to Delete Option action, which could not be completed!");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Loggable
    public void deleteOptionById(String idString) {
        deleteOption(getOptionById(idString));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public List<Option> optionIdsArrayToListOfOption(String[] idsArray) {
        List<Option> list = new ArrayList<Option>();
        if (idsArray != null && idsArray.length != 0) {
            for (String elem : idsArray) {
                try {
                    Integer id = Integer.parseInt(elem);
                    Option option = optionDao.read(id);
                    list.add(option);
                } catch (Exception e) {
                    throw new OptionReadingException("Something gone wrong, " +
                            "try again or contact system administrator!", e);
                }
            }
        }

        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public Option getOptionById(String idString) {

        int id = Integer.parseInt(idString);

        Option result = optionDao.read(id);
        if (result == null) {
            throw new OptionReadingException("Option not found!");
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public Option getOptionByName(String name) {
        try {
            return optionDao.getOptionByName(name);
        } catch (Exception e) {
            throw new OptionReadingException("Something gone wrong, try again or contact system administrator!", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public List<Tariff> getOptionUsedByTariffList(String idString) {
        try {
            int id = Integer.parseInt(idString);
            return optionDao.getOptionUsedByTariffList(id);
        } catch (Exception e) {
            throw new OptionReadingException("Something gone wrong, try again or contact system administrator!", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public List<Contract> getOptionUsedByContractList(String idString) {
        try {
            int id = Integer.parseInt(idString);
            return optionDao.getOptionUsedByContractList(id);
        } catch (Exception e) {
            throw new OptionReadingException("Something gone wrong, try again or contact system administrator!", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public List<Option> getOptionUsedAsJointOptionList(String idString) {
        try {
            int id = Integer.parseInt(idString);
            return optionDao.getOptionUsedAsJointOptionList(id);
        } catch (Exception e) {
            throw new OptionReadingException("Something gone wrong, try again or contact system administrator!", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Set<Option> checkIncompatibleOptions(List<Option> chosenOptions) {

        Set<Option> incompatibleOptions = new HashSet<Option>();

        if (chosenOptions != null && !chosenOptions.isEmpty()) {
            for (Option option : chosenOptions) {
                for (Option incompatibleOption : option.getIncompatibleOptions()) {
                    if (chosenOptions.contains(incompatibleOption)) {
                        incompatibleOptions.add(option);
                        incompatibleOptions.add(incompatibleOption);
                    }
                }
            }
        }
        return incompatibleOptions;
    }

    /**
     * {@inheritDoc}
     */
    public Set<Option> checkJointOptions(List<Option> chosenOptions) {

        Set<Option> jointOptionsSet = new HashSet<Option>();

        if (chosenOptions != null && !chosenOptions.isEmpty()) {
            for (Option option : chosenOptions) {
                List<Option> jointOptionList = option.getJointOptions();

                if (jointOptionList != null && !jointOptionList.isEmpty()) {
                    Boolean hasJoint = false;

                    for (Option jointOption : jointOptionList) {
                        if (chosenOptions.contains(jointOption)) {
                            hasJoint = true;
                        }
                    }

                    if (!hasJoint) {
                        jointOptionsSet.add(option);
                    }
                }
            }
        }

        return jointOptionsSet;
    }
}
