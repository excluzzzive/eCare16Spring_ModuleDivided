package ru.simflex.ex.webservices.entities;

/**
 * Special Tariff entity for sending through web services.
 */
public class WSTariff {

    /**
     * Id of tariff.
     */
    private int id;

    /**
     * Name of tariff.
     */
    private String name;

    /**
     * Default constructor.
     */
    public WSTariff() {}

    /**
     * Constructor with parameters.
     * @param id Id of tariff
     * @param name Name of tariff
     */
    public WSTariff(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Standard POJO getter.
     */
    public int getId() {
        return id;
    }

    /**
     * Standard POJO setter.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Standard POJO getter.
     */
    public String getName() {
        return name;
    }

    /**
     * Standard POJO setter.
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "WSTariff{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WSTariff wsTariff = (WSTariff) o;

        if (id != wsTariff.id) return false;
        return name != null ? name.equals(wsTariff.name) : wsTariff.name == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
