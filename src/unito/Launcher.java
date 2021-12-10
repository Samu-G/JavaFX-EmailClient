package unito;

import javafx.application.Application;
import javafx.stage.Stage;
import unito.controller.persistence.PersistenceAccess;
import unito.view.ViewFactory;

/**
 * Classe usata per avviare l'applicazione
 */
public class Launcher extends Application {

    //init application manager
    private final EmailManager emailManager = new EmailManager(PersistenceAccess.loadFromPersistenceValidAccount());

    //init view manager
    private final ViewFactory viewFactory = new ViewFactory(emailManager);

    public static void main(String[] args) { launch(args); }

    /**
     * Avvia l'applicazione
     *
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        emailManager.setViewFactory(viewFactory);
        if (emailManager.emailAccounts.size() == 0) {
            ViewFactory.viewAlert("Attenzione", "Nessun account salvato nel client.");
        }
        viewFactory.showAccountSelectionWindow();
    }

    /**
     * Chiude l'applicazione
     *
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        PersistenceAccess.saveToPersistence(emailManager.getEmailAccounts());
    }

}


