package unito.model;

import javafx.beans.property.SimpleStringProperty;


public class EmailAccount {

    private final SimpleStringProperty address;
    private final SimpleStringProperty password;

    /*Constructor*/

    public EmailAccount(String address, String password) {
        this.address = new SimpleStringProperty(address);
        this.password = new SimpleStringProperty(password);
    }

    public EmailAccount(ValidAccount validAccount) {
        this.address = new SimpleStringProperty(validAccount.getAddress());
        this.password = new SimpleStringProperty(validAccount.getPassword());
    }

    /*Getter*/

    public String getAddress() {
        return address.get();
    }

    public String getPassword() {
        return password.get();
    }

    /*Aux*/

    @Override
    public String toString() {
        System.out.println("Creato un nuovo EmailAccount\nADRESS: " + address.get() + "\nPASSWORD: " + password.get());
        return address.get();
    }

}
