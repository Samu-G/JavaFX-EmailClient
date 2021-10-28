package unito;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import unito.model.ValidAccount;
import unito.model.ValidEmail;
import unito.model.Email;
import unito.model.EmailAccount;

import java.util.List;

/**
 * Questa classe contiene:
 * - le due observableList di emailAccount e di Email
 * - funzioni per trasformare validAccount e vaildEmail in Account ed Email (serializzabili -> non serializzabili)
 */
public class EmailManager {

    public ObservableList<EmailAccount> emailAccounts;
    private SimpleObjectProperty<EmailAccount> currentAccount;
    private ObservableList<Email> emailList;
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
                "\nADRESS: " + emailAccount.getAddress() +
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
     * Trasforma gli oggeti validEmail (serializzabili) in oggetti Email (non serializzabili)
     * e li salva all'interno di una ObservableList.
     *
     * @param validEmailList QUESTA E' UNA LISTA DI VALIDEMAIL che restituisce loadPersistence
     */
    public void loadEmail(List<ValidEmail> validEmailList) {
        if(validEmailList != null) {
            for (int i = 0; i < validEmailList.size(); i++) {
                ValidEmail emailToLoad = validEmailList.get(i);
                emailList.addAll(new Email(emailToLoad.getSender(), emailToLoad.getRecipients(), emailToLoad.getSubject(), emailToLoad.getTextMessage()));
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
            emailAccounts.addAll(new EmailAccount(accountToLoad.getAddress(), accountToLoad.getPassword()));
        }
    }


    public void refreshEmailList() {

    }

    public void deleteSelectedMessage() {
        //TODO: da implementare
    }
}
