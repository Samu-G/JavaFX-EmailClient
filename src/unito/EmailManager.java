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
import unito.view.ViewManager;

import java.util.List;
import java.util.concurrent.FutureTask;

/**
 * Classe manager che gestisce le funzioni principali dell'applicazione
 */
public class EmailManager {

    /* Model */
    private final ObservableList<EmailAccount> emailAccounts;
    private final SimpleObjectProperty<EmailAccount> currentAccount;
    private final ObservableList<Email> emailList;
    private Email selectedMessage;
    private List<String> addressesNotFoundedBuffer;
    /* View */
    private ViewManager viewManager;
    /* Thread */
    private Thread refreshThread;
    private RefreshService refreshService;

    /**
     * @param validAccountList lista di ValidAccount
     */
    public EmailManager(List<ValidAccount> validAccountList) {
        this.emailAccounts = FXCollections.observableArrayList();
        this.currentAccount = new SimpleObjectProperty<>();
        this.emailList = FXCollections.observableArrayList();
        loadValidAccountFromPersistence(validAccountList);

    }

    /**
     * Attiva il refresh automatico
     *
     * @param refreshRate tasso di refresh
     */
    public void turnOnAutoRefresh(long refreshRate) {
        if (refreshThread == null) {
            refreshService = new RefreshService(this, refreshRate, true);
        } else {
            refreshService.setLoop(true);
        }
        refreshThread = new Thread(refreshService);
        refreshThread.start();
    }

    /**
     * Disattiva il refresh automatico
     */
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

    public ViewManager getViewFactory() {
        return viewManager;
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
     * @param validEmailList questa è una lista di ValidEmail che restituisce loadPersistence
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
     * @param validAccountList questa è una lista di ValidAccount che restituisce loadPersistence
     */
    private void loadValidAccountFromPersistence(List<ValidAccount> validAccountList) {
        for (int i = 0; i < validAccountList.size(); i++) {
            ValidAccount accountToLoad = validAccountList.get(i);
            emailAccounts.addAll(new EmailAccount(accountToLoad));
        }
    }

    /**
     * Utilizzato per fare il refresh manuale dell'email
     */
    public void manualRefresh() {
        RefreshService refreshService = new RefreshService(this, 0, false);
        Thread t = new Thread(refreshService);
        t.start();
    }

    /**
     * Imposta la velocità di refresh della casella email in millisecondi
     *
     * @param refreshRate velocità del refresh
     */
    public void setRefreshSpeed(long refreshRate) {
        if (refreshService == null) {
            turnOnAutoRefresh(refreshRate);
        }
        refreshService.setRefreshRate(refreshRate);
    }

    /**
     * Crea un ClientService per gestire la richiesta (CANCELLAMESSAGGIO), poi gestisce il risultato della richiesta
     */
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
                            ViewManager.viewAlert("Cancellazione messaggio", "Cancellazione messaggio avvenuta con successo");
                            break;

                        case ERROR, FAILED_BY_CREDENTIALS, FAILED_BY_SERVER_DOWN:
                            ViewManager.viewAlert("Cancellazione messaggio", "Errore nella comunicazione con il server");
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * @param viewManager riferimento alla ViewManager per l'EmailManager
     */
    public void setViewFactory(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

    /**
     * Risponde all'email selezionata aprendo una nuova finestra
     *
     * @param emailSelected email selezionata
     */
    public void reply(Email emailSelected) {
        if (emailSelected != null) {
            String recipients = emailSelected.getRecipients();
            if (recipients != null) {
                viewManager.showComposeWindow();
                viewManager.getComposeWindowController().setSubjectTextField(emailSelected.getSubject());
                viewManager.getComposeWindowController().setRecipientsTextField(emailSelected.getSender());
            }
        }
    }

    /**
     * Risponde a tutti i destinatari della email selezionata, aprendo una nuova finestra
     *
     * @param emailSelected email selezionata
     */
    public void replyAll(Email emailSelected) {
        if (emailSelected != null) {
            viewManager.showComposeWindow();
            if (viewManager.getComposeWindowController() != null) {
                viewManager.getComposeWindowController().setSubjectTextField(emailSelected.getSubject());
                viewManager.getComposeWindowController().setRecipientsTextField(String.join(",", emailSelected.getRecipientsArray()));
            }
        }
    }

    /**
     * Inoltra l'email selezionata, aprendo una nuova finestra
     *
     * @param emailSelected email selezionata
     */
    public void forward(Email emailSelected) {
        if (emailSelected != null) {
            viewManager.showComposeWindow();
            if (viewManager.getComposeWindowController() != null) {
                viewManager.getComposeWindowController().setSubjectTextField(emailSelected.getSubject());
                viewManager.getComposeWindowController().setMessageTextArea(emailSelected.getTextMessage());
            }
        }

    }

    /**
     * Cancella l'email selezionata (sincronizzandosi con il server)
     *
     * @param emailSelected email selezionata
     */
    public void delete(Email emailSelected) {
        if (emailSelected != null) {
            this.deleteSelectedMessage();
            this.emailList.remove(emailSelected);
        }
    }

}
