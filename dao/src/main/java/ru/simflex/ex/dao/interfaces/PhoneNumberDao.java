package ru.simflex.ex.dao.interfaces;

import ru.simflex.ex.entities.PhoneNumber;

import java.util.List;

/**
 * PhoneNumber DAO interface.
 */
public interface PhoneNumberDao extends GenericDao<PhoneNumber> {

    /**
     * Returns sorted list of PhoneNumber entities.
     * @return List of PhoneNumber objects
     */
    List<PhoneNumber> getAllEntitiesSorted();

    /**
     * Gets PhoneNumber list where phone numbers are available.
     *
     * @return PhoneNumber list
     */
    List<PhoneNumber> getAvailablePhoneNumberList();

    /**
     * Returns PhoneNumber entity if it exists in DB.
     *
     * @param phoneNumberString String form of phone number
     * @return PhoneNumber object
     */
    PhoneNumber getPhoneNumberByString(String phoneNumberString);

    /**
     * Returns list of phone numbers depending on the page.
     * @param page Page number
     * @return List of PhoneNumber objects
     */
    List<PhoneNumber> getPhoneNumberListByPage(int page);

}
