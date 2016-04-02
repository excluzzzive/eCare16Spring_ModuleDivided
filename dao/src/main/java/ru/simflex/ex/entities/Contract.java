package ru.simflex.ex.entities;


import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;
import javax.persistence.FetchType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * The Contract class represents contract entity.
 * Contract is the central point of company-client relation.
 *
 */
@Entity
@Table(name = "contracts")
public class Contract {

    /**
     * Autogenerated ID of Contract entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_id")
    private int id;

    /**
     * ID of a PhoneNumber which belongs to a Contract.
     */
    @OneToOne
    @JoinColumn(name = "phonenumber_id", nullable = false)
    private PhoneNumber phoneNumber;

    /**
     * ID of a Tariff which belongs to a Contract.
     */
    @ManyToOne
    @JoinColumn(name = "tariff_id", nullable = false)
    private Tariff tariff;

    /**
     * List of chosen Option entities which belongs to a Contract.
     * Isolated to a separate table in database.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "contract_chosen_options",
            joinColumns = @JoinColumn(name = "contract_id"),
            inverseJoinColumns = @JoinColumn(name = "option_id")
    )
    private List<Option> chosenOptions = new ArrayList<Option>();

    /**
     * ID of a User - owner of a Contract.
     * One User can have many Contracts.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Boolean flag representing if a Contract is blocked or not.
     */
    @Column(name = "contract_is_blocked")
    private boolean blocked;

    /**
     * If a Contract is blocked by an Employee (User with employee functions),
     * ID of a User inserts here.
     */
    @Column(name = "contract_blocked_by_employee_id")
    private int blockedByEmployeeId;

    /**
     * Standart POJO getter.
     *
     * @return ID of entity
     */
    public int getId() {
        return id;
    }

    /**
     * Standart POJO setter.
     *
     * @param id ID of entity
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Standart POJO getter.
     *
     * @return ID of a PhoneNumber entity, which belongs to a Contract
     */
    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Standart POJO setter.
     *
     * @param phoneNumber ID of a PhoneNumber entity, which you need to
     *                    consolidate for a Contract
     */
    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Standart POJO getter.
     *
     * @return ID of a Tariff, which belongs to a Contract
     */
    public Tariff getTariff() {
        return tariff;
    }

    /**
     * Standart POJO setter.
     *
     * @param tariff ID of a Tariff entity, which you need to
     *               consolidate for a Contract
     */
    public void setTariff(Tariff tariff) {
        this.tariff = tariff;
    }

    /**
     * Standart POJO getter.
     *
     * @return List of chosen Option entities which belongs to a Contract.
     */
    public List<Option> getChosenOptions() {
        HashSet<Option> optionSet = new HashSet<Option>();
        optionSet.addAll(chosenOptions);
        chosenOptions.clear();
        chosenOptions.addAll(optionSet);
        return chosenOptions;
    }

    /**
     * Standart POJO setter.
     *
     * @param chosenOptions List of chosen Option entities which belongs
     *                      to a Contract.
     */
    public void setChosenOptions(List<Option> chosenOptions) {
        this.chosenOptions = chosenOptions;
    }

    /**
     * Standart POJO getter.
     *
     * @return ID of a User - owner of a Contract
     */
    public User getUser() {
        return user;
    }

    /**
     * Standart POJO setter.
     *
     * @param user ID of a User - owner of a Contract
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Standart POJO getter.
     *
     * @return Boolean flag representing if a Contract is blocked or not.
     */
    public boolean isBlocked() {
        return blocked;
    }

    /**
     * Standart POJO setter.
     *
     * @param blocked Boolean flag representing if a Contract is blocked or not.
     */
    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    /**
     * Standart POJO getter.
     *
     * @return ID of a User employee, who blocked a Contract
     */
    public int getBlockedByEmployeeId() {
        return blockedByEmployeeId;
    }

    /**
     * Standart POJO setter.
     *
     * @param blockedByEmployeeId ID of a User employee, who blocked a Contract
     */
    public void setBlockedByEmployeeId(int blockedByEmployeeId) {
        this.blockedByEmployeeId = blockedByEmployeeId;
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

        Contract option = (Contract) other;
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
        return "Contract{" +
                "id=" + id +
                ", phoneNumber=" + phoneNumber +
                ", tariff=" + tariff +
                ", chosenOptions=" + chosenOptions +
                ", user=" + user +
                ", blocked=" + blocked +
                ", blockedByEmployeeId=" + blockedByEmployeeId +
                '}';
    }
}
