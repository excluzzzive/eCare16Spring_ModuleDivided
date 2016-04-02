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
import ru.simflex.ex.exceptions.TariffCreatingException;
import ru.simflex.ex.exceptions.TariffDeletingException;
import ru.simflex.ex.exceptions.TariffReadingException;
import ru.simflex.ex.exceptions.TariffUpdatingException;
import ru.simflex.ex.services.interfaces.ContractService;
import ru.simflex.ex.services.interfaces.TariffService;
import ru.simflex.ex.webservices.entities.WSTariff;

import java.util.*;

/**
 * Tariff service implementation class.
 */
@Service
@Scope(value = "singleton")
public class TariffServiceImplementation implements TariffService {

    /**
     * TariffDao instance.
     */
    @Autowired
    private TariffDao tariffDao;

    /**
     * OptionDao instance.
     */
    @Autowired
    private OptionDao optionDao;

    /**
     * ContractService instance.
     */
    @Autowired
    private ContractService contractService;

    /**
     * Setter for tariffDao.
     *
     * @param tariffDao TariffDao object
     */
    public void setTariffDao(TariffDao tariffDao) {
        this.tariffDao = tariffDao;
    }

    /**
     * Setter for optionDao.
     *
     * @param optionDao OptionDao object
     */
    public void setOptionDao(OptionDao optionDao) {
        this.optionDao = optionDao;
    }

    /**
     * Setter for contractService.
     *
     * @param contractService ContractService object
     */
    public void setContractService(ContractService contractService) {
        this.contractService = contractService;
    }

    /**
     * Utility method that adds array of Options ids to a tariff.
     *
     * @param tariff          Tariff to add options
     * @param possibleOptions Options that are added to tariff
     */
    @Transactional
    public void addOptionsToTariff(Tariff tariff, String[] possibleOptions) {
        try {
            if (possibleOptions != null && possibleOptions.length != 0) {
                for (String elem : possibleOptions) {
                    Integer id = Integer.parseInt(elem);
                    Option option = optionDao.read(id);
                    tariff.getPossibleOptions().add(option);
                }
            }
        } catch (Exception e) {
            throw new TariffUpdatingException("Something gone wrong, try again or contact system administrator!", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public List<Tariff> getTariffList() {
        try {
            return tariffDao.getAllEntities();
        } catch (Exception e) {
            throw new TariffReadingException("Something gone wrong, try again or contact system administrator!", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public Tariff getTariffById(String idString) {

        Tariff tariff;
        try {
            int id = Integer.parseInt(idString);
            tariff = tariffDao.read(id);
        } catch (Exception e) {
            throw new TariffReadingException("Something gone wrong, try again or contact system administrator!", e);
        }

        if (tariff == null) {
            throw new TariffReadingException("Tariff not found!");
        }

        return tariff;
    }

    public Tariff getTariffByName(String name) {
        try {
            return tariffDao.getTariffByName(name);
        } catch (Exception e) {
            throw new TariffReadingException("Something gone wrong, try again or contact system administrator!", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Loggable
    public void createTariff(Tariff newTariff) {

        try {
            tariffDao.create(newTariff);
        } catch (Exception e) {
            throw new TariffCreatingException("Something gone wrong, try again or contact system administrator!", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Loggable
    public void updateTariff(Tariff editedTariff) {

        if (tariffDao.read(editedTariff.getId()) == null) {
            throw new TariffUpdatingException("Tariff not found!");
        }

        try {
            tariffDao.update(editedTariff);
        } catch (Exception e) {
            throw new TariffUpdatingException("Something gone wrong, try again or contact system administrator!", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Loggable
    public void deleteTariffById(String idString) {

        List<Contract> contractList;
        try {
            contractList = contractService.getContractListByTariffId(idString);
        } catch (Exception e) {
            throw new TariffDeletingException("Something gone wrong, try again or contact system administrator!", e);
        }

        if (contractList.isEmpty()) {
            try {
                int id = Integer.parseInt(idString);
                tariffDao.deleteById(id);
            } catch (Exception e) {
                throw new TariffDeletingException("Something gone wrong," +
                        " try again or contact system administrator! ", e);
            }
        } else {
            throw new TariffDeletingException("Illegal access to Delete Tariff action, which could not be completed!");
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<WSTariff> getWSTariffList() {

        List<WSTariff> wsTariffList = new ArrayList<WSTariff>();

        List<Tariff> tariffList;
        try {
            tariffList = tariffDao.getAllEntities();
        } catch (Exception e) {
            throw new TariffReadingException("Something gone wrong, try again or contact system administrator!", e);
        }

        if (tariffList != null && !tariffList.isEmpty()) {
            for (Tariff tariff : tariffList) {
                wsTariffList.add(new WSTariff(tariff.getId(), tariff.getName()));
            }
        }

        return wsTariffList;
    }
}
