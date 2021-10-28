package unito;

import javafx.application.Application;
import javafx.stage.Stage;
import unito.controller.persistence.PersistenceAccess;
import unito.model.ValidAccount;
import unito.model.EmailAccount;
import unito.view.ViewFactory;

import java.util.ArrayList;
import java.util.List;

public class Launcher extends Application {

    //init model
    /* Questo oggetto emailManager gestirà le liste di account, email relative a quell'account, account corrente */
    private EmailManager emailManager = new EmailManager(PersistenceAccess.loadFromPersistenceValidAccount());

    //init view
    /* Questo oggetto gestisce la view, necessita del puntatore a emailManager  */
    private ViewFactory viewFactory = new ViewFactory(emailManager);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        /* Controllo se ci sono account salvati nel file di persistenza appena letto */
        if (emailManager.emailAccounts.size() > 0) {
            viewFactory.showAccountSelectionWindow();
        }
        /* Do messaggio di errore se non ne trovo neanche uno */
        else {
            ViewFactory.viewAlert("attenzione", "nessun account salvato nel client.");
            stop();
        }
    }

    @Override
    public void stop() throws Exception {
        /* Salvataggio sul file di persistenza */
        List<ValidAccount> validAccountList = new ArrayList<ValidAccount>();
        for (EmailAccount emailAccount : emailManager.getEmailAccounts()) {
            validAccountList.add(new ValidAccount(emailAccount.getAddress(), emailAccount.getPassword()
            ));
        }
        PersistenceAccess.saveToPersistence(validAccountList);
        /**/
    }
}


