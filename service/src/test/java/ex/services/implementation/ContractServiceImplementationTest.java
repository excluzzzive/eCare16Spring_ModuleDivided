package ex.services.implementation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.simflex.ex.dao.interfaces.*;
import ru.simflex.ex.entities.*;
import ru.simflex.ex.exceptions.ContractCreatingException;
import ru.simflex.ex.exceptions.ContractReadingException;
import ru.simflex.ex.exceptions.ContractUpdatingException;
import ru.simflex.ex.services.implementation.ContractServiceImplementation;
import ru.simflex.ex.services.interfaces.OptionService;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Test class for ContractService
 */
public class ContractServiceImplementationTest {

    private ContractServiceImplementation contractService;

    @Mock
    private OptionService optionService;

    @Mock
    private ContractDao contractDao;

    @Mock
    private OptionDao optionDao;

    @Mock
    private PhoneNumberDao phoneNumberDao;

    @Mock
    private TariffDao tariffDao;

    @Mock
    private UserDao userDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        contractService = new ContractServiceImplementation();
        contractService.setOptionService(optionService);
        contractService.setContractDao(contractDao);
        contractService.setOptionDao(optionDao);
        contractService.setPhoneNumberDao(phoneNumberDao);
        contractService.setTariffDao(tariffDao);
        contractService.setUserDao(userDao);
    }

    @After
    public void destroy() {
        contractService = null;
    }

    /**
     * Special utility method for creating contracts.
     */
    private Contract getContractBlocked(int id, boolean blocked) {
        Contract contract = new Contract();
        contract.setId(id);
        contract.setBlocked(blocked);
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
     * Special utility method for creating phone numbers.
     */
    private PhoneNumber getPhoneNumber(int id) {
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setId(id);
        return phoneNumber;
    }

    /**
     * Special utility method for creating tariffs.
     */
    private Tariff getTariff(int id) {
        Tariff tariff = new Tariff();
        tariff.setId(id);
        return tariff;
    }

    /**
     * Special utility method for creating users.
     */
    private User getUser(int id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    @Test
    public void getContractList_Correct() {
        Contract contract1 = getContractBlocked(1, false);
        Contract contract2 = getContractBlocked(2, false);
        List<Contract> expectedList = new ArrayList<Contract>(Arrays.asList(contract1, contract2));

        when(contractDao.getAllEntitiesSorted()).thenReturn(expectedList);
        List<Contract> resultList = contractService.getContractList();

        assertEquals(expectedList, resultList);
        verify(contractDao, times(1)).getAllEntitiesSorted();
    }

    @Test(expected = ContractReadingException.class)
    @SuppressWarnings("unchecked")
    public void getContractList_Must_Throw_Exception() {
        when(contractDao.getAllEntitiesSorted()).thenThrow(Exception.class);
        contractService.getContractList();
    }

    @Test
    public void getContractListByUser_Correct() {

        User user = getUser(1);
        Contract contract1 = getContractBlocked(1, false);
        Contract contract2 = getContractBlocked(2, false);

        List<Contract> expectedList = new ArrayList<Contract>(Arrays.asList(contract1, contract2));
        when(contractDao.getAllEntitiesByUser(user)).thenReturn(expectedList);
        List<Contract> resultList = contractService.getContractListByUser(user);

        assertEquals(expectedList, resultList);
        verify(contractDao, times(1)).getAllEntitiesByUser(user);

    }

    @Test(expected = ContractReadingException.class)
    @SuppressWarnings("unchecked")
    public void getContractListByUser_Must_Throw_Exception() {
        User user = getUser(1);
        when(contractDao.getAllEntitiesByUser(user)).thenThrow(Exception.class);
        contractService.getContractListByUser(user);
    }

    @Test
    public void getContractListByTariffId_Correct() {
        Contract contract1 = getContractBlocked(1, false);
        Contract contract2 = getContractBlocked(2, false);
        List<Contract> expectedList = new ArrayList<Contract>(Arrays.asList(contract1, contract2));
        when(contractDao.getContractListByTariffId(1)).thenReturn(expectedList);
        List<Contract> resultList = contractService.getContractListByTariffId("1");
        assertEquals(expectedList, resultList);
        verify(contractDao, times(1)).getContractListByTariffId(1);
    }

    @Test(expected = ContractReadingException.class)
    public void getContractListByTariffId_Must_Throw_Exception_When_Id_Incorrect() {
        contractService.getContractListByTariffId("as");
    }

    @Test(expected = ContractReadingException.class)
    @SuppressWarnings("unchecked")
    public void getContractListByTariffId_Must_Throw_Exception() {
        when(contractDao.getContractListByTariffId(1)).thenThrow(Exception.class);
        contractService.getContractListByTariffId("1");
    }

    @Test
    public void getContractById_Correct() {
        Contract expectedContract = getContractBlocked(1, false);

        when(contractDao.read(1)).thenReturn(expectedContract);
        Contract result = contractService.getContractById("1");

        assertEquals(expectedContract, result);
        verify(contractDao, times(1)).read(1);
    }

    @Test(expected = ContractReadingException.class)
    public void getContractById_Must_Throw_Exception_When_Id_Incorrect() {
        contractService.getContractById("invalidId");
    }

    @Test(expected = ContractReadingException.class)
    @SuppressWarnings("unchecked")
    public void getContractById_Must_Throw_Exception() {
        when(contractDao.read(1)).thenThrow(Exception.class);
        contractService.getContractById("1");
    }

    @Test
    public void getContractByUserAndId_Correct() {
        String idString = "1";
        int idInt = 1;
        User user = getUser(idInt);
        Contract expectedContract = new Contract();
        expectedContract.setId(idInt);

        when(contractDao.getContractByUserAndId(user, idInt)).thenReturn(expectedContract);
        Contract result = contractService.getContractByUserAndId(user, idString);

        assertEquals(expectedContract, result);
        verify(contractDao, times(1)).getContractByUserAndId(user, idInt);
    }

    @Test(expected = ContractReadingException.class)
    @SuppressWarnings("unchecked")
    public void getContractByUserAndId_Must_Throw_Exception_When_Id_Incorrect() {
        contractService.getContractByUserAndId(new User(), "invalidId");
    }

    @Test(expected = ContractReadingException.class)
    @SuppressWarnings("unchecked")
    public void getContractByUserAndId_Must_Throw_Exception() {
        when(contractDao.read(1)).thenThrow(Exception.class);
        contractService.getContractByUserAndId(new User(), "1");
    }

    @Test
    public void changeBlockState_Correct() {
        int id = 1;
        Contract contract = getContractBlocked(id, false);
        User user = getUser(id);

        when(contractDao.read(id)).thenReturn(contract);
        doNothing().when(contractDao).update(contract);

        contractService.changeBlockState(user, String.valueOf(id));
        verify(contractDao, times(1)).read(id);
        verify(contractDao, times(1)).update(contract);
    }

    @Test(expected = ContractReadingException.class)
    public void changeBlockState_Must_Throw_Exception_When_Id_Incorrect() {
        contractService.changeBlockState(new User(), "invalidId");
    }

    @Test(expected = ContractReadingException.class)
    public void changeBlockState_Must_Throw_Exception_If_Contract_Is_Null() {
        when(contractDao.read(1)).thenReturn(null);
        contractService.changeBlockState(new User(), "1");
    }

    @Test(expected = ContractUpdatingException.class)
    public void changeBlockState_Must_Throw_Exception_When_User_Try_To_Unblock_BlockedByEmployeeContract() {
        Contract contract = getContractBlocked(1, true);
        contract.setBlockedByEmployeeId(1);

        when(contractDao.read(1)).thenReturn(contract);
        contractService.changeBlockState(null, "1");
    }

    @Test(expected = ContractReadingException.class)
    @SuppressWarnings("unchecked")
    public void changeBlockState_Must_Throw_Exception() {
        when(contractDao.read(1)).thenThrow(Exception.class);
        contractService.getContractByUserAndId(new User(), "1");
    }

    @Test
    public void updateContract_Correct() {
        Contract contract = getContractBlocked(1, false);

        doNothing().when(contractDao).update(contract);
        when(contractDao.read(1)).thenReturn(contract);

        contractService.updateContract(contract);
        verify(contractDao, times(1)).read(1);
        verify(contractDao, times(1)).update(contract);

    }

    @Test(expected = ContractReadingException.class)
    public void updateContract_Must_Throw_Exception_If_Contract_Is_Blocked() {
        Contract contract = getContractBlocked(1, true);

        doNothing().when(contractDao).update(contract);
        when(contractDao.read(1)).thenReturn(contract);

        contractService.updateContract(contract);
    }

    @Test(expected = ContractReadingException.class)
    public void updateContract_Must_Throw_Exception() {
        Contract contract = getContractBlocked(1, false);

        when(contractDao.read(1)).thenReturn(contract);
        doThrow(Exception.class).when(contractDao).update(contract);
        contractService.getContractByUserAndId(new User(), "1");
    }

    @Test
    public void deleteContractById_Correct() {
        String idString = "1";
        int idInt = 1;

        Contract contract = getContractBlocked(1, false);
        contract.setPhoneNumber(new PhoneNumber());

        doNothing().when(contractDao).delete(contract);
        when(contractDao.read(1)).thenReturn(contract);
        contractService.deleteContractById(idString);
        verify(contractDao, times(1)).read(idInt);
        verify(contractDao, times(1)).delete(contract);
    }

    @Test(expected = ContractReadingException.class)
    public void deleteContractById_Must_Throw_Exception_If_Contract_Is_Already_Deleted() {
        when(contractDao.read(1)).thenReturn(null);
        contractService.deleteContractById("1");
    }

    @Test(expected = ContractReadingException.class)
    public void deleteContractById_Must_Throw_Exception_If_Contract_Is_Blocked() {
        Contract contract = getContractBlocked(1, true);

        when(contractDao.read(1)).thenReturn(contract);
        doNothing().when(contractDao).delete(contract);

        contractService.deleteContractById("1");
    }

    @Test(expected = ContractReadingException.class)
    @SuppressWarnings("unchecked")
    public void deleteContractById_Must_Throw_Exception() {
        when(contractDao.read(1)).thenThrow(Exception.class);
        contractService.deleteContractById("1");
    }

    @Test
    public void createContract_Correct() {
        int id = 1;
        User user = getUser(id);
        PhoneNumber phoneNumber = getPhoneNumber(id);
        phoneNumber.setAvailable(true);
        Tariff tariff = getTariff(id);
        Contract contract = getContractBlocked(1, false);
        contract.setUser(user);
        contract.setPhoneNumber(phoneNumber);
        contract.setTariff(tariff);

        when(userDao.read(id)).thenReturn(user);
        when(phoneNumberDao.read(id)).thenReturn(phoneNumber);
        when(tariffDao.read(id)).thenReturn(tariff);
        doNothing().when(contractDao).create(contract);

        contractService.createContract(contract);
        verify(userDao, times(1)).read(id);
        verify(phoneNumberDao, times(1)).read(id);
        verify(tariffDao, times(1)).read(id);
        verify(contractDao, times(1)).create(contract);
    }

    @Test(expected = ContractCreatingException.class)
    public void createContract_Must_Throw_Exception_If_User_Is_Deleted() {
        int id = 1;
        User user = getUser(id);
        PhoneNumber phoneNumber = getPhoneNumber(id);
        phoneNumber.setAvailable(true);
        Tariff tariff = getTariff(id);
        Contract contract = getContractBlocked(1, false);
        contract.setUser(user);
        contract.setPhoneNumber(phoneNumber);
        contract.setTariff(tariff);

        when(userDao.read(id)).thenReturn(null);
        when(phoneNumberDao.read(id)).thenReturn(phoneNumber);
        when(tariffDao.read(id)).thenReturn(tariff);
        doNothing().when(contractDao).create(contract);

        contractService.createContract(contract);
    }

    @Test(expected = ContractCreatingException.class)
    public void createContract_Must_Throw_Exception_If_PhoneNumber_Is_Deleted() {
        int id = 1;
        User user = getUser(id);
        PhoneNumber phoneNumber = getPhoneNumber(id);
        phoneNumber.setAvailable(true);
        Tariff tariff = getTariff(id);
        Contract contract = getContractBlocked(1, false);
        contract.setUser(user);
        contract.setPhoneNumber(phoneNumber);
        contract.setTariff(tariff);

        when(userDao.read(id)).thenReturn(user);
        when(phoneNumberDao.read(id)).thenReturn(null);
        when(tariffDao.read(id)).thenReturn(tariff);
        doNothing().when(contractDao).create(contract);

        contractService.createContract(contract);
    }

    @Test(expected = ContractCreatingException.class)
    public void createContract_Must_Throw_Exception_If_PhoneNumber_Is_Not_Available() {
        int id = 1;
        User user = getUser(id);
        PhoneNumber phoneNumber = getPhoneNumber(id);
        phoneNumber.setAvailable(false);
        Tariff tariff = getTariff(id);
        Contract contract = getContractBlocked(1, false);
        contract.setUser(user);
        contract.setPhoneNumber(phoneNumber);
        contract.setTariff(tariff);

        when(userDao.read(id)).thenReturn(user);
        when(phoneNumberDao.read(id)).thenReturn(phoneNumber);
        when(tariffDao.read(id)).thenReturn(tariff);
        doNothing().when(contractDao).create(contract);

        contractService.createContract(contract);
    }

    @Test(expected = ContractCreatingException.class)
    public void createContract_Must_Throw_Exception_If_Tariff_Is_Deleted() {
        int id = 1;
        User user = getUser(id);
        PhoneNumber phoneNumber = getPhoneNumber(id);
        phoneNumber.setAvailable(true);
        Tariff tariff = getTariff(id);
        Contract contract = getContractBlocked(1, false);
        contract.setUser(user);
        contract.setPhoneNumber(phoneNumber);
        contract.setTariff(tariff);

        when(userDao.read(id)).thenReturn(user);
        when(phoneNumberDao.read(id)).thenReturn(phoneNumber);
        when(tariffDao.read(id)).thenReturn(null);
        doNothing().when(contractDao).create(contract);

        contractService.createContract(contract);
    }

    @Test(expected = ContractCreatingException.class)
    public void createContract_Must_Throw_Exception_If_Have_Incompatible_Options() {
        int id = 1;
        User user = getUser(id);
        PhoneNumber phoneNumber = getPhoneNumber(id);
        phoneNumber.setAvailable(true);
        Tariff tariff = getTariff(id);

        Option option1 = getOption(1);
        Option option2 = getOption(2);
        option1.getIncompatibleOptions().add(option2);
        option2.getIncompatibleOptions().add(option1);
        List<Option> chosenOptionList = new ArrayList<Option>(Arrays.asList(option1, option2));
        Set<Option> incompatibleOptionSet = new HashSet<Option>(Arrays.asList(option1, option2));

        Contract contract = getContractBlocked(1, false);
        contract.setUser(user);
        contract.setPhoneNumber(phoneNumber);
        contract.setTariff(tariff);
        contract.setChosenOptions(chosenOptionList);

        when(userDao.read(id)).thenReturn(user);
        when(phoneNumberDao.read(id)).thenReturn(phoneNumber);
        when(tariffDao.read(id)).thenReturn(tariff);
        when(optionDao.read(1)).thenReturn(option1);
        when(optionDao.read(2)).thenReturn(option2);
        when(optionService.checkIncompatibleOptions(chosenOptionList)).thenReturn(incompatibleOptionSet);
        doNothing().when(contractDao).create(contract);

        contractService.createContract(contract);
    }

    @Test(expected = ContractCreatingException.class)
    public void createContract_Must_Throw_Exception_If_Need_Joint_Options() {
        int id = 1;
        User user = getUser(id);
        PhoneNumber phoneNumber = getPhoneNumber(id);
        phoneNumber.setAvailable(true);
        Tariff tariff = getTariff(id);

        Option option1 = getOption(1);
        Option option2 = getOption(2);
        option1.getJointOptions().add(option2);
        List<Option> chosenOptionList = new ArrayList<Option>(Arrays.asList(option1));
        Set<Option> jointOptionSet = new HashSet<Option>(Arrays.asList(option1));

        Contract contract = getContractBlocked(1, false);
        contract.setUser(user);
        contract.setPhoneNumber(phoneNumber);
        contract.setTariff(tariff);
        contract.setChosenOptions(chosenOptionList);

        when(userDao.read(id)).thenReturn(user);
        when(phoneNumberDao.read(id)).thenReturn(phoneNumber);
        when(tariffDao.read(id)).thenReturn(tariff);
        when(optionDao.read(1)).thenReturn(option1);
        when(optionDao.read(2)).thenReturn(option2);
        when(optionService.checkJointOptions(chosenOptionList)).thenReturn(jointOptionSet);
        doNothing().when(contractDao).create(contract);

        contractService.createContract(contract);
    }
}