package unito.model;

import javafx.beans.property.SimpleStringProperty;

/**
 * Classe EmailAccount volta alla visualizzazione su view della classe serializzabile ValidAccount
 */
public class EmailAccount {

    private final SimpleStringProperty address;
    private final SimpleStringProperty password;

    /*Constructor*/

    /**
     * @param address address of account
     * @param password password of account
     */
    public EmailAccount(String address, String password) {
        this.address = new SimpleStringProperty(address);
        this.password = new SimpleStringProperty(password);
    }

    /**
     * @param validAccount
     */
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

    /**
     * Crea una stringa adatta per la visualizzazione su standard output
     *
     * @return l'oggetto nella relativa rappresentazione di stringa
     */
    @Override
    public String toString() {
        return "address: " + address.get() + " password: " + password.get();
    }

}
