package it.ispw.daniele.backpacker.bean;

import java.io.Serial;
import java.io.Serializable;

public class SearchBean implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String country;
    private String city;
    private String address;
    private String range;
    private String restaurant;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String isRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

}
