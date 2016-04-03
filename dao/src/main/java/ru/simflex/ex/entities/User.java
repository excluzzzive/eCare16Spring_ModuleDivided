package ru.simflex.ex.entities;

import org.hibernate.validator.constraints.Email;

import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * User class represents user entity.
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * Id of user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    /**
     * First name of user.
     */
    @NotNull
    @Pattern(regexp = "^[A-z]{2,20}$", message = "")
    @Size(min = 2, max = 20)
    @Column(name = "user_first_name", nullable = false)
    private String firstName;

    /**
     * Last name of user.
     */
    @NotNull
    @Pattern(regexp = "^[A-z]{2,20}$", message = "")
    @Size(min = 2, max = 20)
    @Column(name = "user_last_name", nullable = false)
    private String lastName;

    /**
     * Birth date of user.
     */
    @NotNull
    @Past
    @Column(name = "user_birth_date", nullable = false)
    private Date birthDate;

    /**
     * User's passport data.
     */
    @NotNull
    @Pattern(regexp = "^[A-z0-9\\s]{8,40}$", message = "")
    @Size(min = 8, max = 40)
    @Column(name = "user_passport_data", nullable = false, unique = true)
    private String passportData;

    /**
     * User's email (login).
     */
    @NotNull
    @Email
    @Column(name = "user_email", nullable = false, unique = true)
    private String email;

    /**
     * User's password.
     */
    @Column(name = "user_password", nullable = false)
    private String password;

    /**
     * Is user an employee boolean flag.
     */
    @Column(name = "user_is_employee")
    private boolean employee;

    /**
     * Standard POJO getter.
     *
     * @return Id of user
     */
    public int getId() {
        return id;
    }

    /**
     * Standard POJO setter.
     *
     * @param id Id of user
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Standard POJO getter.
     *
     * @return User's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Standard POJO setter.
     *
     * @param firstName User's first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Standard POJO getter.
     *
     * @return User's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Standard POJO setter.
     *
     * @param lastName User's last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Standard POJO getter.
     *
     * @return User's birth date
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * Standard POJO setter.
     *
     * @param birthDate User's birth date
     */
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Standard POJO getter.
     *
     * @return User's passport data
     */
    public String getPassportData() {
        return passportData;
    }

    /**
     * Standard POJO setter.
     *
     * @param passportData User's passport data
     */
    public void setPassportData(String passportData) {
        this.passportData = passportData;
    }

    /**
     * Standard POJO getter.
     *
     * @return User's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Standard POJO setter.
     *
     * @param email User's email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Standard POJO getter.
     *
     * @return User's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Standard POJO setter.
     *
     * @param password User's password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Standard POJO getter.
     *
     * @return User's employee flag
     */
    public boolean isEmployee() {
        return employee;
    }

    /**
     * Standard POJO setter.
     *
     * @param employee User's employee flag
     */
    public void setEmployee(boolean employee) {
        this.employee = employee;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        User option = (User) other;
        return this.getId() == option.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return id * 31;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                ", passportData='" + passportData + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", employee=" + employee +
                '}';
    }
}
