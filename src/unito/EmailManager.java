package unito;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import unito.controller.service.ClientRequestResult;
import unito.controller.service.ClientRequestType;
import unito.controller.service.ClientService;
import unito.controller.service.RefreshService;
import unito.model.ValidAccount;
import unito.model.ValidEmail;
import unito.model.Email;
import unito.model.EmailAccount;
import unito.view.ViewFactory;
import java.util.List;
import java.util.concurrent.FutureTask;

/**
 * Classe che gestisce le funzioni principali dell'applicazione
 */
public class EmailManager {

    /* Model */
    public ObservableList<EmailAccount> emailAccounts;
    private final SimpleObjectProperty<EmailAccount> currentAccount;
    public ObservableList<Email> emailList;
    private Email selectedMessage;
    private List<String> addressesNotFoundedBuffer;
    /* View */
    private ViewFactory viewFactory;
    /* Thread */
    private Thread refreshThread;
    private RefreshService refreshService;

    public EmailManager(List<ValidAccount> validAccountList) {
        this.emailAccounts = FXCollections.observableArrayList();
        this.currentAccount = new SimpleObjectProperty<>();
        this.emailList = FXCollections.observableArrayList();
        loadValidAccountFromPersistence(validAccountList);

    }

    public void turnOnAutoRefresh(long refreshRate) {
        if (refreshThread == null) {
            refreshService = new RefreshService(this, refreshRate, true);
        } else {
            refreshService.setLoop(true);
        }
        refreshThread = new Thread(refreshService);
        refreshThread.start();
    }

    public void turnOffAutoRefresh() {
        if (refreshThread != null) {
            refreshService.setLoop(false);
            System.out.println("turnoff");
        }
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

    public void setAddressesNotFoundedBuffer(List<String> addressesNotFoundedBuffer) {
        this.addressesNotFoundedBuffer = addressesNotFoundedBuffer;
    }

    public List<String> getAddressesNotFoundedBuffer() {
        return addressesNotFoundedBuffer;
    }

    public ViewFactory getViewFactory() {
        return viewFactory;
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

    public void manualRefresh() {
        RefreshService refreshService = new RefreshService(this, 0, false);
        Thread t = new Thread(refreshService);
        t.start();
    }

    public void setRefreshSpeed(long refreshRate) {
        if (refreshService == null) {
            turnOnAutoRefresh(refreshRate);
        }
        refreshService.setRefreshRate(refreshRate);
    }

    public void deleteSelectedMessage() {
        ClientService clientService = new ClientService(this, ClientRequestType.CANCELLAMESSAGGIO, getSelectedMessage());
        FutureTask<ClientRequestResult> deleteService = new FutureTask<>(clientService);
        Thread thread = new Thread(deleteService);
        thread.start();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {

                    ClientRequestResult r = deleteService.get();

                    switch (r) {
                        case SUCCESS:
                            ViewFactory.viewAlert("Cancellazione messaggio", "Cancellazione messaggio avvenuta con successo");
                            break;

                        case ERROR, FAILED_BY_CREDENTIALS, FAILED_BY_SERVER_DOWN:
                            ViewFactory.viewAlert("Cancellazione messaggio", "Errore nella comunicazione con il server");
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void setViewFactory(ViewFactory viewFactory) {
        this.viewFactory = viewFactory;
    }


    public void reply(Email emailSelected) {
        if (emailSelected != null) {
            String recipients = emailSelected.getRecipients();
            if (recipients != null) {
                viewFactory.showComposeWindow();
                viewFactory.getComposeWindowController().setSubjectTextField(emailSelected.getSubject());
                viewFactory.getComposeWindowController().setRecipientsTextField(emailSelected.getSender());
            }
        }
    }


    public void replyAll(Email emailSelected) {
        if (emailSelected != null) {
            viewFactory.showComposeWindow();
            if (viewFactory.getComposeWindowController() != null) {
                viewFactory.getComposeWindowController().setSubjectTextField(emailSelected.getSubject());
                viewFactory.getComposeWindowController().setRecipientsTextField(String.join(",", emailSelected.getRecipientsArray()));
            }
        }
    }


    public void forward(Email emailSelected) {
        if (emailSelected != null) {
            viewFactory.showComposeWindow();
            if (viewFactory.getComposeWindowController() != null) {
                viewFactory.getComposeWindowController().setSubjectTextField(emailSelected.getSubject());
                viewFactory.getComposeWindowController().setMessageTextArea(emailSelected.getTextMessage());
            }
        }

    }

    public void delete(Email emailSelected) {
        if (emailSelected != null) {
            this.deleteSelectedMessage();
            this.emailList.remove(emailSelected);
        }
    }

}
