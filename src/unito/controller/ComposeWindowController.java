package unito.controller; /**
 * Sample Skeleton for 'ComposeWindow.fxml' Controller Class
 */

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import unito.EmailManager;
import unito.controller.service.ClientRequestResult;
import unito.controller.service.ClientRequestType;
import unito.controller.service.ClientService;
import unito.model.Email;
import unito.view.ViewFactory;

import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComposeWindowController extends BaseController {

    @FXML // fx:id="subjectTextField"
    private TextField subjectTextField; // Value injected by FXMLLoader

    @FXML // fx:id="recipientsTextField"
    private TextField recipientsTextField; // Value injected by FXMLLoader

    @FXML // fx:id="messageTextArea"
    private TextArea messageTextArea; // Value injected by FXMLLoader

    /*array di destinatari*/
    private String[] recipientsBuffer;

    private boolean dirtyTextArea = true;

    public ComposeWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    public void setSubjectTextField(String text) {
        subjectTextField.setText(text);
    }

    public void setRecipientsTextField(String text) {
        recipientsTextField.setText(text);
    }

    public void setMessageTextArea(String text) {
        messageTextArea.setText(text);
    }


    private boolean checkRecipientsTextField() {
        System.out.println("checkRecipientsTextField() called.");
        String pattern = "([^@,;\s]+@[^@,;\s]+)|(?:$|\s*[,;])(?:\s*)(.*?)<([^@,;\s]+@[^@,;\s]+)>";
        Pattern p = Pattern.compile(pattern);

        String text = this.recipientsTextField.getText();
        recipientsBuffer = text.split(", "); /*!!*/
        Matcher matcher;

        for (String recipient : recipientsBuffer) {
            System.out.println(recipient);
            matcher = p.matcher(recipient);
            if (!matcher.matches()) {
                System.out.println("email parser error!");
                ViewFactory.viewAlert("Attenzione!", "Controllare il campo destinatari");
                return false;
            }
        }
        return true;
    }

    @FXML
    void sendMessageAction() {
        System.out.println("sendMessageAction() called.");

        Email toSend;

        if (checkRecipientsTextField()) {

            toSend = new Email(emailManager.getCurrentAccount().getAddress(),
                    recipientsBuffer,
                    subjectTextField.getText(),
                    messageTextArea.getText()
            );

            System.out.println(toSend.getRecipientsArray().length);

            Stage stage = (Stage) recipientsTextField.getScene().getWindow();
            viewFactory.closeStage(stage);

            ClientService clientService = new ClientService(emailManager, ClientRequestType.INVIOMESSAGGIO, toSend);

            FutureTask<ClientRequestResult> sendService = new FutureTask<>(clientService);

            Thread thread = new Thread(sendService);

            thread.start();

            try {

                ClientRequestResult r = sendService.get();

                if (!emailManager.addressesNotFoundedBuffer.isEmpty()) {
                    ViewFactory.viewAlert("ATTENZIONE", "I destinatari " + emailManager.addressesNotFoundedBuffer + " sono inesistenti");
                }

                switch (r) {
                    case SUCCESS:
                        ViewFactory.viewAlert("Successo", "Mail inviata con successo");
                        return;

                    case ERROR:
                        ViewFactory.viewAlert("Errore", "C'è stato qualche errore nell'invio del messaggio");
                        return;

                    case FAILED_BY_CREDENTIALS:
                        ViewFactory.viewAlert("Errore", "Errore nelle credenziali");
                        return;

                    case FAILED_BY_SERVER_DOWN:
                        ViewFactory.viewAlert("Errore", "Il server è spento");
                        return;
                }


            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    void clearTextAreaAction() {
        if (dirtyTextArea) {
            System.out.println("clearTextAreaAction() called.");
            recipientsTextField.requestFocus();
            recipientsTextField.selectAll();
            recipientsTextField.setText("");
            dirtyTextArea = false;
        }
    }

}
