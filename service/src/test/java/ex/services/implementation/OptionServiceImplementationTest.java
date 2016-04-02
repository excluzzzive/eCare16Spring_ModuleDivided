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
import ru.simflex.ex.exceptions.OptionCreatingException;
import ru.simflex.ex.exceptions.OptionDeletingException;
import ru.simflex.ex.exceptions.OptionReadingException;
import ru.simflex.ex.exceptions.OptionUpdatingException;
import ru.simflex.ex.services.implementation.OptionServiceImplementation;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Test class for OptionService.
 */
public class OptionServiceImplementationTest {

    private OptionServiceImplementation optionService;

    @Mock
    private OptionDao optionDao;

    @Mock
    private TariffDao tariffDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        optionService = new OptionServiceImplementation();
        optionService.setOptionDao(optionDao);
        optionService.setTariffDao(tariffDao);
    }

    @After
    public void destroy() {
        optionService = null;
    }

    @Test
    public void getOptionList_Must_Return_List_Of_Options() {

        Option option1 = new Option();
        Option option2 = new Option();
        option1.setId(1);
        option2.setId(2);
        List<Option> expectedOptionList = new ArrayList<Option>(Arrays.asList(option1, option2));

        when(optionDao.getAllEntities()).thenReturn(expectedOptionList);

        List<Option> optionList = optionService.getOptionList();

        assertTrue(expectedOptionList.equals(optionList));
        verify(optionDao, times(1)).getAllEntities();
    }

    @Test(expected = OptionReadingException.class)
    @SuppressWarnings("unchecked")
    public void getOptionList_Must_Throw_Exception() {
        when(optionDao.getAllEntities()).thenThrow(Exception.class);
        optionService.getOptionList();
    }

    @Test
    public void createOption_Correct() {
        Option option1 = new Option();
        Option option2 = new Option();
        Option option3 = new Option();
        option1.setId(1);
        option2.setId(2);
        option3.setId(3);
        option1.getIncompatibleOptions().add(option2);
        option1.getIncompatibleOptions().add(option3);

        doNothing().when(optionDao).create(option1);
        doNothing().when(optionDao).update(option2);
        doNothing().when(optionDao).update(option3);

        optionService.createOption(option1);
        verify(optionDao, times(1)).update(option2);
        verify(optionDao, times(1)).update(option3);
    }

    @Test(expected = OptionCreatingException.class)
    public void createOption_Must_Throw_Exception() {
        Option option1 = new Option();
        doThrow(Exception.class).when(optionDao).create(option1);
        optionService.createOption(option1);
    }

    @Test
    public void updateOption_Correct() {
        Option option1 = new Option();
        Option option2 = new Option();
        option1.setId(1);
        option2.setId(2);

        doNothing().when(optionDao).update(option1);
        when(optionDao.read(option1.getId())).thenReturn(option2);

        optionService.updateOption(option1);
        verify(optionDao, times(1)).update(option1);
    }

    @Test(expected = OptionUpdatingException.class)
    public void updateOption_Must_Throw_Exception_When_Is_Used_By_Contract_And_Change_Some_List() {
        Option option1 = new Option();
        Option option2 = new Option();
        Option option3 = new Option();
        option1.setId(1);
        option2.setId(2);
        option3.setId(3);
        option1.getIncompatibleOptions().add(option2);

        List<Contract> contractList = new ArrayList<Contract>(Arrays.asList(new Contract()));
        when(optionDao.getOptionUsedByContractList(option1.getId())).thenReturn(contractList);
        when(optionDao.read(option1.getId())).thenReturn(option3);

        optionService.updateOption(option1);
    }

    @Test(expected = OptionDeletingException.class)
    public void deleteOption_Must_Throw_Exception_When_Is_Used_By_Contract() {
        Option option1 = new Option();
        option1.setId(1);
        List<Contract> contractList = new ArrayList<Contract>(Arrays.asList(new Contract()));
        when(optionDao.getOptionUsedByContractList(option1.getId())).thenReturn(contractList);
        optionService.deleteOption(option1);
    }

    @Test(expected = OptionDeletingException.class)
    public void deleteOption_Must_Throw_Exception_When_Is_Used_As_Joint() {
        Option option1 = new Option();
        option1.setId(1);
        List<Option> optionList = new ArrayList<Option>(Arrays.asList(new Option()));
        when(optionDao.getOptionUsedAsJointOptionList(option1.getId())).thenReturn(optionList);
        optionService.deleteOption(option1);
    }

    @Test
    public void optionIdsArrayToListOfOption_Must_Return_List_Of_Options() {
        Option option1 = new Option();
        Option option2 = new Option();
        option1.setId(1);
        option2.setId(2);

        String[] ids = {"1", "2"};

        List<Option> expectedList = new ArrayList<Option>(Arrays.asList(option1, option2));

        when(optionDao.read(1)).thenReturn(option1);
        when(optionDao.read(2)).thenReturn(option2);
        List<Option> optionList = optionService.optionIdsArrayToListOfOption(ids);

        assertEquals(expectedList, optionList);
        verify(optionDao, times(1)).read(1);
        verify(optionDao, times(1)).read(2);

    }

    @Test(expected = OptionReadingException.class)
    @SuppressWarnings("unchecked")
    public void optionIdsArrayToListOfOption_Must_Throw_Exception() {
        String[] ids = {"1", "2"};
        when(optionDao.read(1)).thenThrow(Exception.class);
        optionService.optionIdsArrayToListOfOption(ids);
    }

    @Test
    public void getOptionById_Correct() {
        Option option1 = new Option();
        option1.setId(1);
        when(optionDao.read(1)).thenReturn(option1);
        Option result = optionService.getOptionById(String.valueOf(1));
        assertEquals(option1, result);
        verify(optionDao, times(1)).read(1);
    }

    @Test(expected = OptionReadingException.class)
    public void getOptionById_Must_Throw_Exception() {
        when(optionDao.read(1)).thenReturn(null);
        optionService.getOptionById(String.valueOf(1));
    }

    @Test
    public void getOptionByName_Correct() {
        String name = "option1";
        Option option1 = new Option();
        option1.setName(name);
        when(optionDao.getOptionByName(name)).thenReturn(option1);
        Option result = optionService.getOptionByName(name);
        assertEquals(option1, result);
        verify(optionDao, times(1)).getOptionByName(name);
    }

    @Test(expected = OptionReadingException.class)
    @SuppressWarnings("unchecked")
    public void getOptionByName_Must_Throw_Exception() {
        String name = "option1";
        when(optionDao.getOptionByName(name)).thenThrow(Exception.class);
        optionService.getOptionByName(name);
    }

    @Test
    public void getOptionUsedByTariffList_Correct() {
        Option option1 = new Option();
        option1.setId(1);

        Tariff tariff1 = new Tariff();
        tariff1.getPossibleOptions().add(option1);
        List<Tariff> expectedTariffList = new ArrayList<Tariff>(Arrays.asList(tariff1));

        when(optionDao.getOptionUsedByTariffList(1)).thenReturn(expectedTariffList);
        List<Tariff> resultList = optionService.getOptionUsedByTariffList(String.valueOf(1));
        assertEquals(expectedTariffList, resultList);
        verify(optionDao, times(1)).getOptionUsedByTariffList(1);
    }

    @Test(expected = OptionReadingException.class)
    @SuppressWarnings("unchecked")
    public void getOptionUsedByTariffList_Must_Throw_Exception() {
        when(optionDao.getOptionUsedByTariffList(1)).thenThrow(Exception.class);
        optionService.getOptionUsedByTariffList(String.valueOf(1));
    }

    @Test
    public void getOptionUsedByContractList_Correct() {
        Option option1 = new Option();
        option1.setId(1);

        Contract contract = new Contract();
        contract.getChosenOptions().add(option1);
        List<Contract> expectedList = new ArrayList<Contract>(Arrays.asList(contract));

        when(optionDao.getOptionUsedByContractList(1)).thenReturn(expectedList);
        List<Contract> resultList = optionService.getOptionUsedByContractList(String.valueOf(1));
        assertEquals(expectedList, resultList);
        verify(optionDao, times(1)).getOptionUsedByContractList(1);
    }

    @Test(expected = OptionReadingException.class)
    @SuppressWarnings("unchecked")
    public void getOptionUsedByContractList_Must_Throw_Exception() {
        when(optionDao.getOptionUsedByContractList(1)).thenThrow(Exception.class);
        optionService.getOptionUsedByContractList(String.valueOf(1));
    }

    @Test
    public void getOptionUsedAsJointOptionList_Correct() {
        Option option1 = new Option();
        Option option2 = new Option();
        option1.setId(1);
        option1.setId(2);
        option2.getJointOptions().add(option1);

        List<Option> expectedList = new ArrayList<Option>(Arrays.asList(option2));

        when(optionDao.getOptionUsedAsJointOptionList(1)).thenReturn(expectedList);
        List<Option> resultList = optionService.getOptionUsedAsJointOptionList(String.valueOf(1));
        assertEquals(expectedList, resultList);
        verify(optionDao, times(1)).getOptionUsedAsJointOptionList(1);
    }

    @Test(expected = OptionReadingException.class)
    @SuppressWarnings("unchecked")
    public void getOptionUsedAsJointOptionList_Must_Throw_Exception() {
        when(optionDao.getOptionUsedAsJointOptionList(1)).thenThrow(Exception.class);
        optionService.getOptionUsedAsJointOptionList(String.valueOf(1));
    }

    @Test
    public void checkIncompatibleOptions_Must_Return_Set_Of_Incompatible_Options() {

        Option option1 = new Option();
        Option option2 = new Option();
        Option option3 = new Option();
        option1.setId(1);
        option2.setId(2);
        option3.setId(3);

        option1.getIncompatibleOptions().add(option2);
        option2.getIncompatibleOptions().add(option1);

        List<Option> optionList = new ArrayList<Option>();
        optionList.add(option1);
        optionList.add(option2);
        optionList.add(option3);

        Set<Option> incompatibleSet = optionService.checkIncompatibleOptions(optionList);

        Set<Option> expectedResultSet = new HashSet<Option>();
        expectedResultSet.add(option1);
        expectedResultSet.add(option2);

        assertEquals(expectedResultSet, incompatibleSet);
    }

    @Test
    public void checkJointOptions_Must_Return_Set_Of_Joint_Options() {

        Option option1 = new Option();
        Option option2 = new Option();
        Option option3 = new Option();
        Option option4 = new Option();
        Option option5 = new Option();

        option1.setId(1);
        option2.setId(2);
        option3.setId(3);
        option4.setId(4);
        option5.setId(5);

        option1.getJointOptions().add(option2);
        option1.getJointOptions().add(option3);
        option4.getJointOptions().add(option5);

        List<Option> optionList = new ArrayList<Option>();
        optionList.add(option1);
        optionList.add(option4);

        Set<Option> jointSet = optionService.checkJointOptions(optionList);

        Set<Option> expectedResultSet = new HashSet<Option>();
        expectedResultSet.add(option1);
        expectedResultSet.add(option4);

        assertEquals(expectedResultSet, jointSet);
    }
}