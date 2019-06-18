package models;

/**
 * Represents an address for a {@link Player}
 */
public class Address {

    private int id;

    private String city;
    private String street;
    private int houseNr;
    private String zipCode;

    public Address(int id, String city, String street, int houseNr, String zipCode) {
        this.id = id;
        this.city = city;
        this.street = street;
        this.houseNr = houseNr;
        this.zipCode = zipCode;
    }

    public Address(String city, String street, int houseNr, String zipCode) {
        this(-1, city, street, houseNr, zipCode);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getHouseNr() {
        return houseNr;
    }

    public void setHouseNr(int houseNr) {
        this.houseNr = houseNr;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
