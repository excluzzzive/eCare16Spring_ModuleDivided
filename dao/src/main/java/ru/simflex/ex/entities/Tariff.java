package ru.simflex.ex.entities;

import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Tariff class represents tariff entity.
 */
@Entity
@Table(name = "tariffs")
public class Tariff {

    /**
     * Id of tariff.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tariff_id")
    private int id;

    /**
     * Name of tariff.
     */
    @Pattern(regexp = "^[A-z0-9\\s]{2,30}$", message = "")
    @Size(min = 2, max = 30)
    @Column(name = "tariff_name", nullable = false, unique = true)
    private String name;

    /**
     * Monthly payment for tariff.
     */
    @Min(value = 0)
    @Max(value = 999)
    @Column(name = "tariff_monthly_payment", nullable = false)
    private double monthlyPayment;

    /**
     * List of possible options.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tariff_possible_options",
            joinColumns = @JoinColumn(name = "tariff_id"),
            inverseJoinColumns = @JoinColumn(name = "option_id")
    )
    private List<Option> possibleOptions = new ArrayList<Option>();

    /**
     * Standard POJO getter.
     * @return Id of tariff
     */
    public int getId() {
        return id;
    }

    /**
     * Standard POJO setter.
     * @param id Id of tariff
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Standard POJO getter.
     * @return Name of tariff
     */
    public String getName() {
        return name;
    }

    /**
     * Standard POJO setter.
     * @param name Name of tariff
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Standard POJO getter.
     * @return Monthly payment for tariff
     */
    public double getMonthlyPayment() {
        return monthlyPayment;
    }

    /**
     * Standard POJO setter.
     * @param monthlyPayment Monthly payment for tariff
     */
    public void setMonthlyPayment(double monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    /**
     * Standard POJO getter.
     * @return List of possible options.
     */
    public List<Option> getPossibleOptions() {
        return possibleOptions;
    }

    /**
     * Standard POJO setter.
     * @param possibleOptions List of possible options.
     */
    public void setPossibleOptions(List<Option> possibleOptions) {
        this.possibleOptions = possibleOptions;
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

        Tariff option = (Tariff) other;
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
        return "Tariff{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", monthlyPayment=" + monthlyPayment +
                ", possibleOptions=" + possibleOptions +
                '}';
    }
}
