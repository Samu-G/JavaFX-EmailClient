package unito.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import unito.EmailManager;
import unito.model.Email;
import unito.view.ViewFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class MessageWindowController extends BaseController implements Initializable {

    @FXML // fx:id="subjectTextField"
    private TextField subjectTextField; // Value injected by FXMLLoader

    @FXML // fx:id="recipientsTextField"
    private TextField recipientsTextField; // Value injected by FXMLLoader

    @FXML // fx:id="recipientTextArea"
    private TextArea recipientTextArea; // Value injected by FXMLLoader

    private static Email emailSelected;

    public MessageWindowController(EmailManager emailManager, ViewFactory viewFactory, String s) {
        super(emailManager, viewFactory, s);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        subjectTextField.setText(emailManager.getSelectedMessage().getSubject());
        recipientsTextField.setText(emailManager.getSelectedMessage().getRecipients());
        recipientTextArea.setText(emailManager.getSelectedMessage().getTextMessage());
    }
}
