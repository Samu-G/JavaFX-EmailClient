package unito;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import unito.controller.persistence.ValidAccount;
import unito.controller.persistence.ValidEmail;
import unito.model.Email;
import unito.model.EmailAccount;

import java.util.List;

/**
 * Questa classe contiene:
 * - le due observableList di emailAccount e di Email
 * - funzioni per trasformare validAccount e vaildEmail in Account ed Email (serializzabili -> non serializzabili)
 */
public class EmailManager {

    /* Lista osservabile volta ad ospitare gli account letti dalla lista di persistenza */
    private ObservableList<EmailAccount> emailAccounts = FXCollections.observableArrayList();

    /* Account scelto dall'utente dalla lista di emailAccount */
    private SimpleObjectProperty<EmailAccount> currentAccount = new SimpleObjectProperty<>();

    /* Lista di email della casella scelta dall'utente */
    private ObservableList<Email> emailList = FXCollections.observableArrayList();

    /* ? */
    private Email selectedMessage;

    private String logString;

    public EmailManager(List<ValidAccount> validAccountList) {
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

    public SimpleObjectProperty<EmailAccount> currentAccountProperty() {
        return currentAccount;
    }

    public void deleteSelectedMessage() {
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
    public void loadValidEmailFromPersistence(List<ValidEmail> validEmailList) {
        for (int i = 0; i < validEmailList.size(); i++) {
            ValidEmail emailToLoad = validEmailList.get(i);
            emailList.addAll(new Email(emailToLoad.getSender(), emailToLoad.getRecipients(), emailToLoad.getSubject(), emailToLoad.getTextMessage()));
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


}
