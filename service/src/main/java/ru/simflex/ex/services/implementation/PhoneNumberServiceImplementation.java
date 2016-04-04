package ru.simflex.ex.services.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.simflex.ex.annotations.Loggable;
import ru.simflex.ex.constants.Messages;
import ru.simflex.ex.dao.interfaces.PhoneNumberDao;
import ru.simflex.ex.entities.PhoneNumber;
import ru.simflex.ex.exceptions.PhoneNumberCreatingException;
import ru.simflex.ex.exceptions.PhoneNumberDeletingException;
import ru.simflex.ex.exceptions.PhoneNumberReadingException;
import ru.simflex.ex.services.interfaces.PhoneNumberService;
import ru.simflex.ex.util.Utility;
import ru.simflex.ex.constants.Attributes;

import java.util.LinkedList;
import java.util.List;

/**
 * Phone number service implementation class.
 */
@Service
@Scope(value = "singleton")
public class PhoneNumberServiceImplementation implements PhoneNumberService {

    /**
     * PhoneNumberDao instance.
     */
    @Autowired
    private PhoneNumberDao phoneNumberDao;

    /**
     * Phone number must start with "7".
     */
    private static final String PHONE_NUMBER_STARTS_WITH = "7";

    /**
     * Phone number must be 11 numbers.
     */
    private static final int PHONE_NUMBER_LENGTH = 11;

    /**
     * Setter for PhoneNumberDAO.
     *
     * @param phoneNumberDao PhoneNumberDao object
     */
    public void setPhoneNumberDao(PhoneNumberDao phoneNumberDao) {
        this.phoneNumberDao = phoneNumberDao;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public List<PhoneNumber> getPhoneNumberList() {
        try {
            return phoneNumberDao.getAllEntitiesSorted();
        } catch (Exception e) {
            throw new PhoneNumberReadingException(Messages.EXCEPTION_MESSAGE_SOMETHING_GONE_WRONG, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public List<PhoneNumber> getAvailablePhoneNumberList() {
        try {
            return phoneNumberDao.getAvailablePhoneNumberList();
        } catch (Exception e) {
            throw new PhoneNumberReadingException(Messages.EXCEPTION_MESSAGE_SOMETHING_GONE_WRONG, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Loggable
    public void addPhoneNumber(String phoneNumberString) {
        phoneNumberString = phoneNumberString.replaceAll("[^0-9]", "");
        if (phoneNumberString.startsWith(PHONE_NUMBER_STARTS_WITH)
                && phoneNumberString.length() == PHONE_NUMBER_LENGTH) {
            PhoneNumber phoneNumber = new PhoneNumber();
            phoneNumber.setPhoneNumberString(phoneNumberString);
            phoneNumber.setAvailable(true);

            try {
                phoneNumberDao.create(phoneNumber);
            } catch (Exception e) {
                throw new PhoneNumberCreatingException(Messages.EXCEPTION_MESSAGE_SOMETHING_GONE_WRONG, e);
            }

        } else {
            throw new PhoneNumberCreatingException("Incorrect number");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Loggable
    public void deletePhoneNumberById(Integer id) {
        PhoneNumber deletedPhoneNumber;
        try {
            deletedPhoneNumber = phoneNumberDao.read(id);
        } catch (Exception e) {
            throw new PhoneNumberReadingException(Messages.EXCEPTION_MESSAGE_SOMETHING_GONE_WRONG, e);
        }

        if (deletedPhoneNumber != null && deletedPhoneNumber.isAvailable()) {

            try {
                phoneNumberDao.delete(deletedPhoneNumber);
            } catch (Exception e) {
                throw new PhoneNumberDeletingException(Messages.EXCEPTION_MESSAGE_SOMETHING_GONE_WRONG, e);
            }

        } else {
            throw new PhoneNumberDeletingException("Trying to remove used or nonexistent phone number!");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public PhoneNumber getPhoneNumberById(String idString) {
        try {
            int id = Integer.parseInt(idString);
            return phoneNumberDao.read(id);
        } catch (Exception e) {
            throw new PhoneNumberReadingException(Messages.EXCEPTION_MESSAGE_SOMETHING_GONE_WRONG, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public long getPhoneNumberCount() {
        try {
            return phoneNumberDao.getEntityCount();
        } catch (Exception e) {
            throw new PhoneNumberReadingException(Messages.EXCEPTION_MESSAGE_SOMETHING_GONE_WRONG, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public PhoneNumber getPhoneNumberByString(String phoneNumberString) {
        try {
            return phoneNumberDao.getPhoneNumberByString(phoneNumberString);
        } catch (Exception e) {
            throw new PhoneNumberReadingException(Messages.EXCEPTION_MESSAGE_SOMETHING_GONE_WRONG, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<PhoneNumber> getPhoneNumberListByPage(int page) {
        try {

            if (page < 0) {
                throw new PhoneNumberReadingException("Incorrect page number!");
            }

            return phoneNumberDao.getPhoneNumberListByPage(page);
        } catch (Exception e) {
            throw new PhoneNumberReadingException(Messages.EXCEPTION_MESSAGE_SOMETHING_GONE_WRONG, e);
        }
    }

    public int getCurrentPageByPhoneNumberString(String phoneNumberString) {
        List<PhoneNumber> phoneNumberList = phoneNumberDao.getAllEntitiesSorted();

        int phoneNumberIndex = 0;
        for (int i = 0; i < phoneNumberList.size(); i++) {
            if (phoneNumberList.get(i).getPhoneNumberString().equals(phoneNumberString)) {
                phoneNumberIndex = i + 1;
                break;
            }
        }

        if (phoneNumberIndex % Attributes.LIMIT_PHONE_NUMBERS_PER_PAGE == 0) {
            return phoneNumberIndex / Attributes.LIMIT_PHONE_NUMBERS_PER_PAGE;
        } else {
            return phoneNumberIndex / Attributes.LIMIT_PHONE_NUMBERS_PER_PAGE + 1;
        }
    }
}
