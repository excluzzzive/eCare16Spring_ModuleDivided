package ru.simflex.ex.services.interfaces;

import ru.simflex.ex.entities.PhoneNumber;

import java.util.List;

/**
 * Phone number service interface.
 */
public interface PhoneNumberService {

    /**
     * Gets PhoneNumber list.
     *
     * @return PhoneNumber list
     */
    List<PhoneNumber> getPhoneNumberList();

    /**
     * Gets PhoneNumber list where phone numbers are available.
     *
     * @return PhoneNumber list
     */
    List<PhoneNumber> getAvailablePhoneNumberList();

    /**
     * Add phone number to database.
     *
     * @param phoneNumberString Text form of phone number
     */
    void addPhoneNumber(String phoneNumberString);


    /**
     * Returns an PhoneNumber object by id.
     *
     * @param idString Id of phoneNumber
     * @return PhoneNumber object
     */
    PhoneNumber getPhoneNumberById(String idString);

    /**
     * Delete phone number object by Id.
     *
     * @param id Id of phone number
     */
    void deletePhoneNumberById(Integer id);

    /**
     * Returns a quantity of phone number entities in DB.
     *
     * @return Quantity of phone numbers.
     */
    long getPhoneNumberCount();

    /**
     * Returns PhoneNumber entity if it exists in DB.
     *
     * @param phoneNumberString String form of phone number
     * @return PhoneNumber object
     */
    PhoneNumber getPhoneNumberByString(String phoneNumberString);

}
