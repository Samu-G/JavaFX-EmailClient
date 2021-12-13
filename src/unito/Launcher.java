package unito;

import javafx.application.Application;
import javafx.stage.Stage;
import unito.controller.persistence.PersistenceAccess;
import unito.view.ViewManager;

/**
 * Classe usata per avviare l'applicazione
 */
public class Launcher extends Application {

    //init application manager
    private final EmailManager emailManager = new EmailManager(PersistenceAccess.loadFromPersistenceValidAccount());

    //init view manager
    private final ViewManager viewManager = new ViewManager(emailManager);

    public static void main(String[] args) { launch(args); }

    /**
     * Avvia l'applicazione
     *
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        emailManager.setViewFactory(viewManager);
        if (emailManager.getEmailAccounts().size() == 0) {
            ViewManager.viewAlert("Attenzione", "Nessun account salvato nel client.");
        }
        viewManager.showAccountSelectionWindow();
    }

    /**
     * Chiude l'applicazione
     *
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        System.out.println("stop()");
        PersistenceAccess.saveToPersistence(emailManager.getEmailAccounts());
    }

}


