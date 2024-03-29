package it.ispw.daniele.backpacker.bean;

import java.io.Serial;
import java.io.Serializable;

public class UserBean extends GeneralUserBean implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String name;
    private String surname;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSurname() {
        return this.surname;
    }
}
