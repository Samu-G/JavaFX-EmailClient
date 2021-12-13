package unito.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import unito.EmailManager;
import unito.model.Email;
import unito.view.ViewManager;

/**
 * Classe del controller utilizzato per la finestra per visualizzare un'email
 */
public class MessageWindowController extends BaseController implements Initializable {

    @FXML
    private TextArea messageTextArea;

    @FXML
    private TextField recipientsTextField;

    @FXML
    private TextField senderTextField;

    @FXML
    private TextField subjectTextField;

    private final Email emailToView;

    /**
     * @param emailManager riferimento all'emailManger dell'applicazione
     * @param viewManager  riferimento al viewManager dell'applicazione
     * @param fxmlName     path del file .fxml
     * @param emailToView  oggetto Email da visualizzare nella vista
     */
    public MessageWindowController(EmailManager emailManager, ViewManager viewManager, String fxmlName, Email emailToView) {
        super(emailManager, viewManager, fxmlName);
        this.emailToView = emailToView;
    }

    /**
     * Risponde all'email selezionata
     */
    @FXML
    void rispondiButtonAction() {
        emailManager.reply(emailToView);
    }

    /**
     * Rispondi a tutti i destinari inseriti
     */
    @FXML
    void rispondiATuttiButtonAction() {
        emailManager.replyAll(emailToView);
    }

    /**
     * Inoltra l'email selezionata
     */
    @FXML
    void inoltraButtonAction() {
        emailManager.forward(emailToView);
    }

    /**
     * Cancella l'email selezionata
     */
    @FXML
    void cancellaButtonAction() {
        emailManager.delete(emailToView);
        Stage thisStage = (Stage) messageTextArea.getScene().getWindow();
        viewManager.closeStage(thisStage);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (emailManager.getSelectedMessage() != null) {
            /* Non togliere da qui questo codice!! Funziona solo nell'initializer! */
            subjectTextField.textProperty().bind(emailManager.getSelectedMessage().getSubjectProperty());
            recipientsTextField.textProperty().bind(emailManager.getSelectedMessage().getRecipientsProperty());
            senderTextField.textProperty().bind(emailManager.getSelectedMessage().getSenderProperty());
            messageTextArea.textProperty().bind(emailManager.getSelectedMessage().getTextMessageProperty());
        }
    }
}
