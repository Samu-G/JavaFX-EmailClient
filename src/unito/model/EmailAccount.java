package unito.model;

import javafx.beans.property.SimpleStringProperty;

import java.util.Properties;

public class EmailAccount {

    private SimpleStringProperty address;
    private SimpleStringProperty password;

    public String getAddress() {
        return address.get();
    }

    public String getPassword() {
        return password.get();
    }

    @Override
    public String toString() {
        return "Address: " + address.get() + "\nPassowrd: " + password.get();
    }

    public EmailAccount(String address, String password) {
        this.address = new SimpleStringProperty(address);
        this.password = new SimpleStringProperty(password);
    }
}
