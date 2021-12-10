package unito.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import unito.EmailManager;
import unito.model.Email;
import unito.view.ViewFactory;

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
     * @param emailManager
     * @param viewFactory   abstract view controller
     * @param fxmlName      fxml file path of this controller
     * @param emailToView   selected email
     */
    public MessageWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName, Email emailToView) {
        super(emailManager, viewFactory, fxmlName);
        this.emailToView = emailToView;
    }

    /**
     * Risponde all'email selezionata
     *
     * @param event
     */
    @FXML
    void rispondiButtonAction(ActionEvent event) {
        emailManager.reply(emailToView);
    }

    /**
     * Rispondi a tutti i destinari inseriti
     *
     * @param event
     */
    @FXML
    void rispondiATuttiButtonAction(ActionEvent event) { emailManager.replyAll(emailToView); }

    /**
     * Inoltra l'email selezionata
     *
     * @param event
     */
    @FXML
    void inoltraButtonAction(ActionEvent event) {
        emailManager.forward(emailToView);
    }

    /**
     * Cancella l'email selezionata
     *
     * @param event
     */
    @FXML
    void cancellaButtonAction(ActionEvent event) {
        emailManager.delete(emailToView);
    }

    /**
     * Inizializza il controller
     *
     * @param url
     * @param resourceBundle
     */
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
