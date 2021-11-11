package unito;

import com.sun.tools.javac.Main;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import unito.controller.ComposeWindowController;
import unito.controller.MainWindowController;
import unito.controller.service.ClientService;
import unito.model.ValidAccount;
import unito.model.ValidEmail;
import unito.model.Email;
import unito.model.EmailAccount;
import unito.view.ViewFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Questa classe contiene:
 * - le due observableList di emailAccount e di Email
 * - funzioni per trasformare validAccount e validEmail in Account ed Email (serializzabili -> non serializzabili)
 */
public class EmailManager {

    public ObservableList<EmailAccount> emailAccounts;
    private SimpleObjectProperty<EmailAccount> currentAccount;
    public ObservableList<Email> emailList;
    private Email selectedMessage;
    private String logString;

    public EmailManager(List<ValidAccount> validAccountList) {
        this.emailAccounts = FXCollections.observableArrayList();
        this.currentAccount = new SimpleObjectProperty<>();
        this.emailList = FXCollections.observableArrayList();
        loadValidAccountFromPersistence(validAccountList);
    }

    public void setCurrentAccount(EmailAccount emailAccount) {
        currentAccount.set(emailAccount);
        System.out.println("**********" +
                "\nsetCurrentAccount() called." +
                "\nADDRESS: " + emailAccount.getAddress() +
                "\nPASSWORD: " + emailAccount.getPassword() +
                "\n**********");
    }

    public void setSelectedMessage(Email message) {
        this.selectedMessage = message;
    }

    public void setLogString(String string) {
        logString = string;
    }

    public ObservableList<Email> getEmailList() {
        return emailList;
    }

    public ObservableList<EmailAccount> getEmailAccounts() {
        return emailAccounts;
    }

    public Email getSelectedMessage() {
        return selectedMessage;
    }

    public EmailAccount getCurrentAccount() {
        return currentAccount.get();
    }

    /**
     * Trasforma gli oggetti validEmail (serializzabili) in oggetti Email (non serializzabili)
     * e li salva all'interno di una ObservableList.
     *
     * @param validEmailList QUESTA E' UNA LISTA DI VALIDEMAIL che restituisce loadPersistence
     */
    public void loadEmail(List<ValidEmail> validEmailList) {
        if (validEmailList != null) {
            for (int i = 0; i < validEmailList.size(); i++) {
                ValidEmail emailToLoad = validEmailList.get(i);
                emailList.add(new Email(emailToLoad));
            }
        }
    }

    /**
     * Trasforma gli oggetti validAccount (serializzabili) in oggetti EmailAccount (non serializzabili)
     * e li salva all'interno di una ObservableList.
     *
     * @param validAccountList QUESTA E' UNA LISTA DI VALIDACCOUNT che restituisce loadPersistence
     */
    private void loadValidAccountFromPersistence(List<ValidAccount> validAccountList) {
        for (int i = 0; i < validAccountList.size(); i++) {
            ValidAccount accountToLoad = validAccountList.get(i);
            emailAccounts.addAll(new EmailAccount(accountToLoad));
        }
    }

    // prima implementazione del refresh dovuta a una delete
    public void refreshEmailList() {
        /*System.out.println("RefreshEmailList called\n");
        List<ValidEmail> validEmailList = new ArrayList<>();

        for (Email email: emailList) {
            validEmailList.add(new ValidEmail(email.getSender(),
                    email.getRecipientsArray(),
                    email.getSubject(),
                    email.getSize(),
                    email.getDate(),
                    email.getTextMessage()));
        }

        loadEmail(validEmailList);*/
    }

}
