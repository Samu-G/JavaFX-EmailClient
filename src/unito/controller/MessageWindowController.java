package unito.controller;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (emailManager.getSelectedMessage() != null) {
            /* Non togliere da qui questo codice!! Funziona solo nell'initializer! */
            subjectTextField.setText(emailManager.getSelectedMessage().getSubject());
            recipientsTextField.setText(Arrays.toString(emailManager.getSelectedMessage().getRecipientsArray()));
            senderTextField.setText(emailManager.getSelectedMessage().getSender());
            messageTextArea.setText(emailManager.getSelectedMessage().getTextMessage());
        }

    }
}
