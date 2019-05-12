package michal.edu.survey.Models;

import java.io.Serializable;

public class Address implements Serializable {

    private String city;
    private String street;
    private int num;

    public Address(String city, String street, int num) {
        this.city = city;
        this.street = street;
        this.num = num;
    }

    public Address() {
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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "Address{" +
                "city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", num=" + num +
                '}';
    }
}
