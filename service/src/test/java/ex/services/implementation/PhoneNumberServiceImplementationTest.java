package ex.services.implementation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.simflex.ex.dao.interfaces.PhoneNumberDao;
import ru.simflex.ex.entities.PhoneNumber;
import ru.simflex.ex.exceptions.PhoneNumberCreatingException;
import ru.simflex.ex.exceptions.PhoneNumberDeletingException;
import ru.simflex.ex.exceptions.PhoneNumberReadingException;
import ru.simflex.ex.services.implementation.PhoneNumberServiceImplementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Test class for PhoneNumberService
 */
public class PhoneNumberServiceImplementationTest {

    PhoneNumberServiceImplementation phoneNumberService;

    @Mock
    private PhoneNumberDao phoneNumberDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        phoneNumberService = new PhoneNumberServiceImplementation();
        phoneNumberService.setPhoneNumberDao(phoneNumberDao);
    }

    @After
    public void destroy() {
        phoneNumberService = null;
    }

    /**
     * Special utility method for creating phone numbers.
     */
    private PhoneNumber getPhoneNumber(int id) {
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setId(id);
        return phoneNumber;
    }

    @Test
    public void getPhoneNumberList_Correct() {
        PhoneNumber phoneNumber1 = getPhoneNumber(1);
        PhoneNumber phoneNumber2 = getPhoneNumber(2);
        List<PhoneNumber> expectedList = new ArrayList<PhoneNumber>(Arrays.asList(phoneNumber1, phoneNumber2));

        when(phoneNumberDao.getAllEntitiesSorted()).thenReturn(expectedList);
        List<PhoneNumber> resultList = phoneNumberService.getPhoneNumberList();

        assertEquals(expectedList, resultList);
        verify(phoneNumberDao, times(1)).getAllEntitiesSorted();
    }

    @Test(expected = PhoneNumberReadingException.class)
    @SuppressWarnings("unchecked")
    public void getPhoneNumberList_Must_Throw_Exception() {
        when(phoneNumberDao.getAllEntitiesSorted()).thenThrow(Exception.class);
        phoneNumberService.getPhoneNumberList();
    }

    @Test
    public void getAvailablePhoneNumberList_Correct() {
        PhoneNumber phoneNumber1 = getPhoneNumber(1);
        phoneNumber1.setAvailable(true);
        PhoneNumber phoneNumber2 = getPhoneNumber(2);
        phoneNumber2.setAvailable(true);
        List<PhoneNumber> expectedList = new ArrayList<PhoneNumber>(Arrays.asList(phoneNumber1, phoneNumber2));

        when(phoneNumberDao.getAvailablePhoneNumberList()).thenReturn(expectedList);
        List<PhoneNumber> resultList = phoneNumberService.getAvailablePhoneNumberList();

        assertEquals(expectedList, resultList);
        verify(phoneNumberDao, times(1)).getAvailablePhoneNumberList();
    }

    @Test(expected = PhoneNumberReadingException.class)
    @SuppressWarnings("unchecked")
    public void getAvailablePhoneNumberList_Must_Throw_Exception() {
        when(phoneNumberDao.getAvailablePhoneNumberList()).thenThrow(Exception.class);
        phoneNumberService.getAvailablePhoneNumberList();
    }

    @Test
    public void addPhoneNumber_Correct() {
        PhoneNumber expectedPhoneNumber = getPhoneNumber(0);
        expectedPhoneNumber.setPhoneNumberString("79111234567");
        expectedPhoneNumber.setAvailable(true);
        doNothing().when(phoneNumberDao).create(expectedPhoneNumber);
        phoneNumberService.addPhoneNumber("79111234567");
        verify(phoneNumberDao, times(1)).create(expectedPhoneNumber);
    }

    @Test(expected = PhoneNumberCreatingException.class)
    public void addPhoneNumber_Must_Throw_Exception_If_PhoneNumberString_Is_Incorrect() {
        PhoneNumber expectedPhoneNumber = getPhoneNumber(0);
        expectedPhoneNumber.setPhoneNumberString("s2sv");
        expectedPhoneNumber.setAvailable(true);
        doNothing().when(phoneNumberDao).create(expectedPhoneNumber);
        phoneNumberService.addPhoneNumber("s2sv");
    }

    @Test(expected = PhoneNumberCreatingException.class)
    public void addPhoneNumber_Must_Throw_Exception() {
        PhoneNumber expectedPhoneNumber = getPhoneNumber(0);
        expectedPhoneNumber.setPhoneNumberString("79111234567");
        expectedPhoneNumber.setAvailable(true);
        doThrow(Exception.class).when(phoneNumberDao).create(expectedPhoneNumber);
        phoneNumberService.addPhoneNumber("79111234567");
    }

    @Test
    public void deletePhoneNumberById_Correct() {
        PhoneNumber expectedPhoneNumber = getPhoneNumber(0);
        expectedPhoneNumber.setPhoneNumberString("79111234567");
        expectedPhoneNumber.setAvailable(true);
        when(phoneNumberDao.read(0)).thenReturn(expectedPhoneNumber);
        doNothing().when(phoneNumberDao).delete(expectedPhoneNumber);
        phoneNumberService.deletePhoneNumberById(0);
        verify(phoneNumberDao, times(1)).delete(expectedPhoneNumber);
    }

    @Test(expected = PhoneNumberDeletingException.class)
    public void deletePhoneNumberById_Must_Throw_Exception_If_PhoneNumber_Not_Found() {
        when(phoneNumberDao.read(0)).thenReturn(null);
        phoneNumberService.deletePhoneNumberById(0);
    }

    @Test(expected = PhoneNumberDeletingException.class)
    public void deletePhoneNumberById_Must_Throw_Exception_If_PhoneNumber_Is_Used() {
        PhoneNumber expectedPhoneNumber = getPhoneNumber(0);
        expectedPhoneNumber.setPhoneNumberString("79111234567");
        expectedPhoneNumber.setAvailable(false);
        when(phoneNumberDao.read(0)).thenReturn(expectedPhoneNumber);
        phoneNumberService.deletePhoneNumberById(0);
    }

    @Test(expected = PhoneNumberReadingException.class)
    @SuppressWarnings("unchecked")
    public void deletePhoneNumberById_Must_Throw_Exception_If_Reading_Is_False() {
        when(phoneNumberDao.read(0)).thenThrow(Exception.class);
        phoneNumberService.deletePhoneNumberById(0);
    }

    @Test(expected = PhoneNumberDeletingException.class)
    public void deletePhoneNumberById_Must_Throw_Exception() {
        PhoneNumber expectedPhoneNumber = getPhoneNumber(0);
        doThrow(Exception.class).when(phoneNumberDao).delete(expectedPhoneNumber);
        phoneNumberService.deletePhoneNumberById(0);
    }

    @Test
    public void getPhoneNumberById_Correct() {
        PhoneNumber expectedPhoneNumber = getPhoneNumber(0);
        when(phoneNumberDao.read(0)).thenReturn(expectedPhoneNumber);
        PhoneNumber resultPhoneNumber = phoneNumberService.getPhoneNumberById("0");
        assertEquals(expectedPhoneNumber, resultPhoneNumber);
        verify(phoneNumberDao, times(1)).read(0);
    }

    @Test(expected = PhoneNumberReadingException.class)
    public void getPhoneNumberById_Must_Throw_Exception_If_Id_Is_Incorrect() {
        phoneNumberService.getPhoneNumberById("sv2");
    }

    @Test(expected = PhoneNumberReadingException.class)
    @SuppressWarnings("unchecked")
    public void getPhoneNumberById_Must_Throw_Exception() {
        when(phoneNumberDao.read(0)).thenThrow(Exception.class);
        phoneNumberService.getPhoneNumberById("0");
    }

    @Test
    public void getPhoneNumberCount_Correct() {
        Long expectedCount = 100L;
        when(phoneNumberDao.getEntityCount()).thenReturn(expectedCount);
        Long resultCount = phoneNumberService.getPhoneNumberCount();
        assertEquals(expectedCount, resultCount);
        verify(phoneNumberDao,times(1)).getEntityCount();
    }

    @Test(expected = PhoneNumberReadingException.class)
    @SuppressWarnings("unchecked")
    public void getPhoneNumberCount_Must_Throw_Exception() {
        when(phoneNumberDao.getEntityCount()).thenThrow(Exception.class);
        phoneNumberService.getPhoneNumberCount();
    }

    @Test
    public void getPhoneNumberByString_Correct() {
        PhoneNumber expectedPhoneNumber = getPhoneNumber(0);
        expectedPhoneNumber.setPhoneNumberString("79111234567");
        when(phoneNumberDao.getPhoneNumberByString("79111234567")).thenReturn(expectedPhoneNumber);
        PhoneNumber resultPhoneNumber = phoneNumberService.getPhoneNumberByString("79111234567");
        assertEquals(expectedPhoneNumber, resultPhoneNumber);
        verify(phoneNumberDao,times(1)).getPhoneNumberByString("79111234567");
    }

    @Test(expected = PhoneNumberReadingException.class)
    @SuppressWarnings("unchecked")
    public void getPhoneNumberByString_Must_Throw_Exception() {
        when(phoneNumberDao.getPhoneNumberByString("79111234567")).thenThrow(Exception.class);
        phoneNumberService.getPhoneNumberByString("79111234567");
    }
}