package ru.simflex.ex.webservices.entities;

import java.util.List;

/**
 * Special User entity for sending through web services.
 */
public class WSUser {

    /**
     * Contract phone number.
     */
    private String phoneNumberString;

    /**
     * User first name.
     */
    private String firstName;

    /**
     * User last name.
     */
    private String lastName;

    /**
     * User email.
     */
    private String email;

    /**
     * Contract tariff.
     */
    private String tariff;

    /**
     * Contract chosen options.
     */
    private List<String> chosenOptions;

    /**
     * Default constructor
     */
    public WSUser() {}

    /**
     * Standard POJO getter.
     */
    public String getPhoneNumberString() {
        return phoneNumberString;
    }

    public void setPhoneNumberString(String phoneNumberString) {
        this.phoneNumberString = phoneNumberString;
    }

    /**
     * Standard POJO getter.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Standard POJO setter.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Standard POJO getter.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Standard POJO setter.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Standard POJO getter.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Standard POJO setter.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Standard POJO getter.
     */
    public String getTariff() {
        return tariff;
    }

    /**
     * Standard POJO setter.
     */
    public void setTariff(String tariff) {
        this.tariff = tariff;
    }

    /**
     * Standard POJO getter.
     */
    public List<String> getChosenOptions() {
        return chosenOptions;
    }

    /**
     * Standard POJO setter.
     */
    public void setChosenOptions(List<String> chosenOptions) {
        this.chosenOptions = chosenOptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WSUser wsUser = (WSUser) o;

        return phoneNumberString != null ? phoneNumberString.equals(wsUser.phoneNumberString) : wsUser.phoneNumberString == null;

    }

    @Override
    public int hashCode() {
        return phoneNumberString != null ? phoneNumberString.hashCode() : 0;
    }
}
