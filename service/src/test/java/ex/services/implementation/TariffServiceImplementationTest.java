package ex.services.implementation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.simflex.ex.dao.interfaces.OptionDao;
import ru.simflex.ex.dao.interfaces.TariffDao;
import ru.simflex.ex.entities.Contract;
import ru.simflex.ex.entities.Option;
import ru.simflex.ex.entities.Tariff;
import ru.simflex.ex.exceptions.TariffCreatingException;
import ru.simflex.ex.exceptions.TariffDeletingException;
import ru.simflex.ex.exceptions.TariffReadingException;
import ru.simflex.ex.exceptions.TariffUpdatingException;
import ru.simflex.ex.services.implementation.TariffServiceImplementation;
import ru.simflex.ex.services.interfaces.ContractService;
import ru.simflex.ex.webservices.entities.WSTariff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Test class for TariffServiceImplementation
 */
public class TariffServiceImplementationTest {

    private TariffServiceImplementation tariffService;

    @Mock
    private ContractService contractService;

    @Mock
    private OptionDao optionDao;

    @Mock
    private TariffDao tariffDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        tariffService = new TariffServiceImplementation();
        tariffService.setContractService(contractService);
        tariffService.setOptionDao(optionDao);
        tariffService.setTariffDao(tariffDao);
    }

    @After
    public void destroy() {
        contractService = null;
    }

    /**
     * Special utility method for creating contracts.
     */
    private Contract getContract(int id) {
        Contract contract = new Contract();
        contract.setId(id);
        return contract;
    }

    /**
     * Special utility method for creating options.
     */
    private Option getOption(int id) {
        Option option = new Option();
        option.setId(id);
        return option;
    }

    /**
     * Special utility method for creating tariffs.
     */
    private Tariff getTariff(int id) {
        Tariff tariff = new Tariff();
        tariff.setId(id);
        return tariff;
    }

    @Test
    public void addOptionsToTariff_Correct() {
        Tariff tariff = getTariff(0);
        Option option1 = getOption(1);
        Option option2 = getOption(2);
        String[] optionIds = {"1", "2"};
        List<Option> expectedList = new ArrayList<Option>(Arrays.asList(option1, option2));

        when(optionDao.read(1)).thenReturn(option1);
        when(optionDao.read(2)).thenReturn(option2);

        tariffService.addOptionsToTariff(tariff, optionIds);
        assertEquals(expectedList, tariff.getPossibleOptions());
        verify(optionDao, times(1)).read(1);
        verify(optionDao, times(1)).read(2);
    }

    @Test(expected = TariffUpdatingException.class)
    @SuppressWarnings("unchecked")
    public void addOptionsToTariff_Must_Throw_Exception() {
        Tariff tariff = getTariff(0);
        String[] optionIds = {"1", "2"};
        when(optionDao.read(2)).thenThrow(Exception.class);
        tariffService.addOptionsToTariff(tariff, optionIds);
    }

    @Test
    public void getTariffList_Correct() {
        Tariff tariff1 = getTariff(1);
        Tariff tariff2 = getTariff(2);
        List<Tariff> expectedList = new ArrayList<Tariff>(Arrays.asList(tariff1, tariff2));

        when(tariffDao.getAllEntities()).thenReturn(expectedList);
        List<Tariff> resultList = tariffService.getTariffList();

        assertEquals(expectedList, resultList);
        verify(tariffDao, times(1)).getAllEntities();
    }

    @Test(expected = TariffReadingException.class)
    @SuppressWarnings("unchecked")
    public void getTariffList_Must_Throw_Exception() {
        when(tariffDao.getAllEntities()).thenThrow(Exception.class);
        tariffService.getTariffList();
    }

    @Test
    public void getTariffById_Correct() {
        Tariff expectedTariff = getTariff(0);
        when(tariffDao.read(0)).thenReturn(expectedTariff);
        Tariff result = tariffService.getTariffById("0");
        assertEquals(expectedTariff, result);
        verify(tariffDao, times(1)).read(0);
    }

    @Test(expected = TariffReadingException.class)
    public void getTariffById_Must_Throw_Exception_If_Id_Is_Incorrect() {
        tariffService.getTariffById("dsa");
    }

    @Test(expected = TariffReadingException.class)
    public void getTariffById_Must_Throw_Exception_If_Tariff_Is_Null() {
        when(tariffDao.read(0)).thenReturn(null);
        tariffService.getTariffById("0");
    }

    @Test(expected = TariffReadingException.class)
    @SuppressWarnings("unchecked")
    public void getTariffById_Must_Throw_Exception() {
        when(tariffDao.read(0)).thenThrow(Exception.class);
        tariffService.getTariffById("0");
    }

    @Test
    public void getTariffByName_Correct() {
        String name = "Tariff Name";
        Tariff expectedTariff = getTariff(0);
        expectedTariff.setName(name);
        when(tariffDao.getTariffByName(name)).thenReturn(expectedTariff);
        Tariff result = tariffService.getTariffByName(name);
        assertEquals(expectedTariff, result);
        verify(tariffDao, times(1)).getTariffByName(name);
    }

    @Test(expected = TariffReadingException.class)
    @SuppressWarnings("unchecked")
    public void getTariffByName_Must_Throw_Exception() {
        String name = "Tariff Name";
        when(tariffDao.getTariffByName(name)).thenThrow(Exception.class);
        tariffService.getTariffByName(name);
    }

    @Test
    public void createTariff_Correct() {
        Tariff tariff = getTariff(0);
        doNothing().when(tariffDao).create(tariff);
        tariffService.createTariff(tariff);
        verify(tariffDao, times(1)).create(tariff);
    }

    @Test(expected = TariffCreatingException.class)
    public void createTariff_Must_Throw_Exception() {
        Tariff tariff = getTariff(0);
        doThrow(Exception.class).when(tariffDao).create(tariff);
        tariffService.createTariff(tariff);
    }

    @Test
    public void updateTariff_Correct() {
        Tariff tariff = getTariff(0);
        doNothing().when(tariffDao).update(tariff);
        when(tariffDao.read(0)).thenReturn(tariff);
        tariffService.updateTariff(tariff);
        verify(tariffDao, times(1)).update(tariff);
    }

    @Test(expected = TariffUpdatingException.class)
    public void updateTariff_Must_Throw_Exception_If_Tariff_Not_Found() {
        Tariff tariff = getTariff(0);
        when(tariffDao.read(0)).thenReturn(null);
        tariffService.updateTariff(tariff);
    }

    @Test(expected = TariffUpdatingException.class)
    public void updateTariff_Must_Throw_Exception() {
        Tariff tariff = getTariff(0);
        doThrow(Exception.class).when(tariffDao).update(tariff);
        tariffService.updateTariff(tariff);
    }

    @Test
    public void deleteTariffById_Correct() {
        String tariffIdString = "0";
        List<Contract> contractList = new ArrayList<Contract>();

        when(contractService.getContractListByTariffId(tariffIdString)).thenReturn(contractList);
        doNothing().when(tariffDao).deleteById(0);
        tariffService.deleteTariffById(tariffIdString);
        verify(contractService, times(1)).getContractListByTariffId(tariffIdString);
        verify(tariffDao, times(1)).deleteById(0);
    }

    @Test(expected = TariffDeletingException.class)
    public void deleteTariffById_Must_Throw_Exception_If_Id_Is_Incorrect() {
        String tariffIdString = "asas";
        List<Contract> contractList = new ArrayList<Contract>();
        when(contractService.getContractListByTariffId(tariffIdString)).thenReturn(contractList);
        tariffService.deleteTariffById(tariffIdString);
    }

    @Test(expected = TariffDeletingException.class)
    public void deleteTariffById_Must_Throw_Exception_If_Tariff_Is_Used_By_Contracts() {
        String tariffIdString = "0";
        Contract contract1 = getContract(1);
        Contract contract2 = getContract(2);
        List<Contract> contractList = new ArrayList<Contract>(Arrays.asList(contract1, contract2));

        when(contractService.getContractListByTariffId(tariffIdString)).thenReturn(contractList);
        tariffService.deleteTariffById(tariffIdString);
    }

    @Test(expected = TariffDeletingException.class)
    public void deleteTariffById_Must_Throw_Exception() {
        String tariffIdString = "1";
        List<Contract> contractList = new ArrayList<Contract>();
        when(contractService.getContractListByTariffId(tariffIdString)).thenReturn(contractList);
        doThrow(Exception.class).when(tariffDao).deleteById(1);
        tariffService.deleteTariffById(tariffIdString);
    }

    @Test
    public void getWSTariffList_Correct() {
        Tariff tariff1 = getTariff(1);
        tariff1.setName("Tariff1");
        Tariff tariff2 = getTariff(2);
        tariff2.setName("Tariff2");
        List<Tariff> tariffList = new ArrayList<Tariff>(Arrays.asList(tariff1, tariff2));
        when(tariffDao.getAllEntities()).thenReturn(tariffList);

        WSTariff wsTariff1 = new WSTariff(1, "Tariff1");
        WSTariff wsTariff2 = new WSTariff(2, "Tariff2");
        List<WSTariff> expectedWsTariffList = new ArrayList<WSTariff>(Arrays.asList(wsTariff1, wsTariff2));
        List<WSTariff> resultWsTariffList = tariffService.getWSTariffList();
        assertEquals(expectedWsTariffList, resultWsTariffList);
    }

    @Test(expected = TariffReadingException.class)
    @SuppressWarnings("unchecked")
    public void getWSTariffList_Must_Throw_Exception() {
        when(tariffDao.getAllEntities()).thenThrow(Exception.class);
        tariffService.getWSTariffList();
    }
}