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

public class AccountSelectionWindowController extends BaseController implements Initializable {

    @FXML
    private Label errorLabel;
    @FXML
    private ChoiceBox<EmailAccount> accountPicker;

    public AccountSelectionWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    /**
     * Azione legata al bottone LOGIN della finestra di selezione dell'account
     */
    @FXML
    void loginButtonAction() {
        System.out.println("loginButtonAction() called.");

        /* Qui viene fatto il set del account scelto dall'utente al momento in cui clicca login */
        try {
            emailManager.setCurrentAccount(accountPicker.getValue());
        } catch (RuntimeException e) {
            errorLabel.setText("Devi prima selezionare un'account dalla lista");
        }

        /* Qui viene avviato il task volto a collegarsi e scaricare "l'EmailBean" dal server */
        ClientService clientService = new ClientService(emailManager, ClientRequestType.HANDSHAKING, null);
        /* Viene avviato un thread apposito per gestire questo lavoro in concorrenza */
        FutureTask<ClientRequestResult> loginService = new FutureTask<ClientRequestResult>(clientService);

        Thread thread = new Thread(loginService);
        thread.start();

        try {
            //Viene restituito un risultato dal thread (PER QUESTO E'UN FUTURETASK)
            ClientRequestResult r = loginService.get();

            switch (r) {
                case SUCCESS:
                    ViewFactory.viewAlert("Evviva!", "Login avvenuto con successo");
                    Stage thisStage = (Stage) errorLabel.getScene().getWindow();
                    viewFactory.closeStage(thisStage);
                    viewFactory.showMainWindow();
                    return;

                case FAILED_BY_CREDENTIALS:
                    errorLabel.setText("Credenziali errate!");
                    return;

                case FAILED_BY_SERVER_DOWN:
                    errorLabel.setText("Il server è spento!");
                    return;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Riempie l'oggetto ChoiceBox con gli account salvati
     */
    private void setUpMenuButton() {
        accountPicker.setItems(emailManager.getEmailAccounts());
        accountPicker.setValue(emailManager.getCurrentAccount());
    }


    /**
     * Attenzione a questo initlialize
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpMenuButton();
    }
}
