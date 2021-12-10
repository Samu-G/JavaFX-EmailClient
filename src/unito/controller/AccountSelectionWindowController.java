package unito.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import unito.EmailManager;
import unito.controller.service.ClientRequestResult;
import unito.controller.service.ClientRequestType;
import unito.controller.service.ClientService;
import unito.model.EmailAccount;
import unito.view.ViewFactory;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.FutureTask;

/**
 * Classe del controller utilizzato per la finestra di selezione di un account registrato presso il Server
 */
public class AccountSelectionWindowController extends BaseController implements Initializable {

    @FXML
    private Label errorLabel;

    @FXML
    private ChoiceBox<EmailAccount> accountPicker;

    /**
     * @param emailManager
     * @param viewFactory   abstract view controller
     * @param fxmlName      fxml file path of this controller
     */
    public AccountSelectionWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    /**
     * Azione legata al bottone "login" della finestra di selezione dell'account
     */
    @FXML
    void loginButtonAction() {
        System.out.println("loginButtonAction() called.");

        /* Qui viene fatto il set del account scelto dall'utente al momento in cui clicca login */
        try {
            emailManager.setCurrentAccount(accountPicker.getValue());
        } catch (RuntimeException e) {
            errorLabel.setText("Devi prima selezionare un'account dalla lista");
            return;
        }

        ClientService clientService = new ClientService(emailManager, ClientRequestType.HANDSHAKING, null);

        FutureTask<ClientRequestResult> loginService = new FutureTask<>(clientService);

        Thread thread = new Thread(loginService);

        thread.start();

        try {
            ClientRequestResult r = loginService.get();

            switch (r) {
                case SUCCESS:
                    ViewFactory.viewAlert("Evviva!", "Login avvenuto con successo");
                    Stage thisStage = (Stage) errorLabel.getScene().getWindow();
                    viewFactory.closeStage(thisStage);
                    viewFactory.showMainWindow();
                    viewFactory.getMainWindowController().setLabel("Connessione stabilita con il server: autenticato come " + emailManager.getCurrentAccount().getAddress());
                    break;

                case FAILED_BY_CREDENTIALS:
                    errorLabel.setText("Credenziali errate!");
                    break;

                case FAILED_BY_SERVER_DOWN:
                    errorLabel.setText("Il server è spento!");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setUpMenuButton() {
        accountPicker.setItems(emailManager.getEmailAccounts());
        accountPicker.setValue(emailManager.getCurrentAccount());
    }

    /**
     * Inizializza il controller
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpMenuButton();
    }
}
