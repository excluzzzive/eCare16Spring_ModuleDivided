package ru.simflex.ex.services.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.simflex.ex.annotations.Loggable;
import ru.simflex.ex.constants.Messages;
import ru.simflex.ex.dao.interfaces.*;
import ru.simflex.ex.entities.Contract;
import ru.simflex.ex.entities.Option;
import ru.simflex.ex.entities.PhoneNumber;
import ru.simflex.ex.entities.Tariff;
import ru.simflex.ex.entities.User;
import ru.simflex.ex.exceptions.ContractCreatingException;
import ru.simflex.ex.exceptions.ContractDeletingException;
import ru.simflex.ex.exceptions.ContractReadingException;
import ru.simflex.ex.exceptions.ContractUpdatingException;
import ru.simflex.ex.services.interfaces.ContractService;
import ru.simflex.ex.services.interfaces.OptionService;
import ru.simflex.ex.webservices.entities.WSUser;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Contract service implementation class.
 */
@Service
@Scope(value = "singleton")
public class ContractServiceImplementation implements ContractService {

    /**
     * OptionService instance.
     */
    @Autowired
    private OptionService optionService;

    /**
     * ContractDao instance.
     */
    @Autowired
    private ContractDao contractDao;

    /**
     * OptionDao instance.
     */
    @Autowired
    private OptionDao optionDao;

    /**
     * PhoneNumberDao instance.
     */
    @Autowired
    private PhoneNumberDao phoneNumberDao;

    /**
     * TariffDao instance.
     */
    @Autowired
    private TariffDao tariffDao;

    /**
     * UserDao instance.
     */
    @Autowired
    private UserDao userDao;

    /**
     * Setter for OptionService.
     * @param optionService OptionService object
     */
    public void setOptionService(OptionService optionService) {
        this.optionService = optionService;
    }

    /**
     * Setter for ContractDAO.
     * @param contractDao ContractDao object
     */
    public void setContractDao(ContractDao contractDao) {
        this.contractDao = contractDao;
    }

    /**
     * Setter for OptionDAO.
     * @param optionDao OptionDao object
     */
    public void setOptionDao(OptionDao optionDao) {
        this.optionDao = optionDao;
    }

    /**
     * Setter for PhoneNumberDAO.
     * @param phoneNumberDao PhoneNumberDao object
     */
    public void setPhoneNumberDao(PhoneNumberDao phoneNumberDao) {
        this.phoneNumberDao = phoneNumberDao;
    }

    /**
     * Setter for TariffDao.
     * @param tariffDao TariffDao object
     */
    public void setTariffDao(TariffDao tariffDao) {
        this.tariffDao = tariffDao;
    }

    /**
     * Setter for UserDAO.
     * @param userDao UserDao object
     */
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public List<Contract> getContractList() {
        try {
            return contractDao.getAllEntitiesSorted();
        } catch (Exception e) {
            throw new ContractReadingException(Messages.EXCEPTION_MESSAGE_SOMETHING_GONE_WRONG, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public List<Contract> getContractListByUser(User user) {
        try {
            return contractDao.getAllEntitiesByUser(user);
        } catch (Exception e) {
            throw new ContractReadingException(Messages.EXCEPTION_MESSAGE_SOMETHING_GONE_WRONG, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<Contract> getContractListByTariffId(String idString) {
        try {
            int id = Integer.parseInt(idString);
            return contractDao.getContractListByTariffId(id);
        } catch (Exception e) {
            throw new ContractReadingException(Messages.EXCEPTION_MESSAGE_SOMETHING_GONE_WRONG, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public Contract getContractById(String idString) {
        try {
            int id = Integer.parseInt(idString);
            return contractDao.read(id);
        } catch (Exception e) {
            throw new ContractReadingException(Messages.EXCEPTION_MESSAGE_SOMETHING_GONE_WRONG, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public Contract getContractByUserAndId(User user, String idString) {
        Contract contract;
        try {
            int id = Integer.parseInt(idString);
            contract = contractDao.getContractByUserAndId(user, id);
        } catch (Exception e) {
            throw new ContractReadingException(Messages.EXCEPTION_MESSAGE_SOMETHING_GONE_WRONG, e);
        }
        if (contract == null) {
            throw new ContractReadingException("Contract not found!");
        }

        return contract;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Loggable
    public void changeBlockState(User user, String idString) {

        Contract contract;
        try {
            int id = Integer.parseInt(idString);
            contract = contractDao.read(id);
        } catch (Exception e) {
            throw new ContractReadingException(Messages.EXCEPTION_MESSAGE_SOMETHING_GONE_WRONG, e);
        }

        if (contract == null) {
            throw new ContractReadingException("Contract not found!");
        } else if (contract.isBlocked()) {

            if (user == null && contract.getBlockedByEmployeeId() > 0) {
                throw new ContractUpdatingException("User trying to unblock blocked by employee contract!");
            }

            contract.setBlocked(false);
            contract.setBlockedByEmployeeId(0);
        } else {

            contract.setBlocked(true);
            if (user == null) {
                contract.setBlockedByEmployeeId(0);
            } else {
                contract.setBlockedByEmployeeId(user.getId());
            }
        }

        try {
            contractDao.update(contract);
        } catch (Exception e) {
            throw new ContractUpdatingException(Messages.EXCEPTION_MESSAGE_SOMETHING_GONE_WRONG, e);
        }
    }

    /**
     *
     */
    @Transactional
    @Loggable
    public void updateContract(Contract editedContract) {

        checkContractBlocked(String.valueOf(editedContract.getId()));
        Tariff tariff;
        try {
            tariff = tariffDao.read(editedContract.getTariff().getId());
        } catch (Exception e) {
            throw new ContractUpdatingException(Messages.EXCEPTION_MESSAGE_SOMETHING_GONE_WRONG, e);
        }

        //If some options were deleted or have incompatible or joint options or unchecked from a tariff
        List<Option> chosenOptionList = editedContract.getChosenOptions();
        try {
            checkChosenOptionListOnImpropriety(tariff, chosenOptionList);
        } catch (Exception e) {
            throw new ContractUpdatingException(e.getMessage());
        }

        try {
            contractDao.update(editedContract);
        } catch (Exception e) {
            throw new ContractUpdatingException(Messages.EXCEPTION_MESSAGE_SOMETHING_GONE_WRONG, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public void deleteContractById(String idString) {

        try {
            int id = Integer.parseInt(idString);
            Contract contract = contractDao.read(id);

            if (contract.isBlocked()) {
                throw new ContractDeletingException("Trying to delete blocked contract!");
            }

            PhoneNumber phoneNumber = contract.getPhoneNumber();
            phoneNumber.setAvailable(true);
            contractDao.delete(contract);

        } catch (Exception e) {
            throw new ContractDeletingException(Messages.EXCEPTION_MESSAGE_SOMETHING_GONE_WRONG, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Loggable
    public void createContract(Contract newContract) {
        //If user was deleted
        User user = userDao.read(newContract.getUser().getId());
        if (user == null) {
            throw new ContractCreatingException("User not found!");
        }

        //If phone number was deleted or is not available
        PhoneNumber phoneNumber = phoneNumberDao.read(newContract.getPhoneNumber().getId());
        if (phoneNumber == null || !phoneNumber.isAvailable()) {
            throw new ContractCreatingException("PhoneNumber not found or is already used!");
        }

        //If tariff was deleted
        Tariff tariff = tariffDao.read(newContract.getTariff().getId());
        if (tariff == null) {
            throw new ContractCreatingException("Tariff not found!");
        }

        //If some options were deleted or have incompatible or joint options or unchecked from a tariff
        List<Option> chosenOptionList = newContract.getChosenOptions();
        try {
            checkChosenOptionListOnImpropriety(tariff, chosenOptionList);
        } catch (Exception e) {
            throw new ContractCreatingException(e.getMessage());
        }

        try {
            contractDao.create(newContract);
            phoneNumber.setAvailable(false);


        } catch (Exception e) {
            throw new ContractCreatingException(Messages.EXCEPTION_MESSAGE_SOMETHING_GONE_WRONG, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void checkContractBlocked(String idString) {
        Contract contract = getContractById(idString);
        if (contract.isBlocked()) {
            throw new ContractReadingException("Trying to use blocked contract!");
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<WSUser> getWSUserListByTariffId(String idString) {

        List<WSUser> wsUserList = new ArrayList<WSUser>();

        List<Contract> contractList;
        try {
            int id = Integer.parseInt(idString);
            contractList = contractDao.getContractListByTariffId(id);
        } catch (Exception e) {
            throw new ContractReadingException(Messages.EXCEPTION_MESSAGE_SOMETHING_GONE_WRONG, e);
        }

        if (contractList != null && !contractList.isEmpty()) {
            for (Contract contract : contractList) {
                WSUser wsUser = new WSUser();
                wsUser.setPhoneNumberString(contract.getPhoneNumber().getPhoneNumberString());
                wsUser.setFirstName(contract.getUser().getFirstName());
                wsUser.setLastName(contract.getUser().getLastName());
                wsUser.setEmail(contract.getUser().getEmail());
                wsUser.setTariff(contract.getTariff().getName());

                List<String> chosenOptionList = new ArrayList<String>();
                for (Option option : contract.getChosenOptions()) {
                    chosenOptionList.add(option.getName());
                }
                wsUser.setChosenOptions(chosenOptionList);

                wsUserList.add(wsUser);
            }
        }


        return wsUserList;
    }

    /**
     * {@inheritDoc}
     */
    public void checkChosenOptionListOnImpropriety(Tariff tariff, List<Option> chosenOptionList) {
        if (chosenOptionList != null && !chosenOptionList.isEmpty()) {
            List<Option> updatedChosenOptionList = new ArrayList<Option>();
            for (Option option : chosenOptionList) {

                if (!tariff.getPossibleOptions().contains(option)) {
                    throw new ContractUpdatingException("Maybe some options were deleted or unchecked from a tariff!");
                }

                Option updatedOption = optionDao.read(option.getId());
                if (updatedOption != null) {
                    updatedChosenOptionList.add(updatedOption);
                } else {
                    throw new ContractUpdatingException("Option not found!");
                }
            }
            Set<Option> chosenIncompatibleOptionSet = optionService.checkIncompatibleOptions(updatedChosenOptionList);
            Set<Option> chosenJointOptionSet = optionService.checkJointOptions(updatedChosenOptionList);

            if (chosenIncompatibleOptionSet != null && !chosenIncompatibleOptionSet.isEmpty()) {
                throw new ContractUpdatingException("Some options have incompatible options!");
            }

            if (chosenJointOptionSet != null && !chosenJointOptionSet.isEmpty()) {
                throw new ContractUpdatingException("Some options have joint options!");
            }
        }
    }
}


