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
        return "ADRESS: " + address.get() + ", PASSWORD: " + password.get();
    }

    public EmailAccount(String address, String password) {
        System.out.println("EmailAccount() called.");
        this.address = new SimpleStringProperty(address);
        this.password = new SimpleStringProperty(password);

        /*
        properties = new Properties();
        properties.put("incomingHost", "imap.gmail.com");
        properties.put("mail.store.protocol", "imaps");

        properties.put("mail.transport.protocol", "smtps");
        properties.put("mail.smtps.host", "smtp.gmail.com");
        properties.put("mail.smtps.auth", "true");
        properties.put("outgoingHost", "smtp.gmail.com");

         */
    }
}
