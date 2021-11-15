package unito.controller;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import unito.EmailManager;
import unito.view.ViewFactory;

public class MessageWindowController extends BaseController implements Initializable {


    @FXML
    private TextArea messageTextArea;

    @FXML
    private TextField recipientsTextField;

    @FXML
    private TextField senderTextField;

    @FXML
    private TextField subjectTextField;


    public MessageWindowController(EmailManager emailManager, ViewFactory viewFactory, String s) {
        super(emailManager, viewFactory, s);
    }

    @FXML
    void rispondiButtonAction(ActionEvent event) {
        emailManager.reply();
    }

    @FXML
    void rispondiATuttiButtonAction(ActionEvent event) {
        emailManager.replyAll();
    }

    @FXML
    void inoltraButtonAction(ActionEvent event) {
        emailManager.forward();
    }

    @FXML
    void cancellaButtonAction(ActionEvent event) {
        emailManager.delete();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (emailManager.getSelectedMessage() != null) {
            /* Non togliere da qui questo codice!! Funziona solo nell'initializer! */
            subjectTextField.textProperty().bind(emailManager.getSelectedMessage().getSubjectProperty());
            recipientsTextField.textProperty().bind(emailManager.getSelectedMessage().getrecipientsProperty());
            senderTextField.textProperty().bind(emailManager.getSelectedMessage().getSenderProperty());
            messageTextArea.textProperty().bind(emailManager.getSelectedMessage().getTextMessageProperty());
        }

    }
}
