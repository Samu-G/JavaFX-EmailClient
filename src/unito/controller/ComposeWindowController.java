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
import unito.model.Email;
import unito.view.ViewFactory;

import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComposeWindowController extends BaseController{

    @FXML // fx:id="subjectTextField"
    private TextField subjectTextField; // Value injected by FXMLLoader

    @FXML // fx:id="recipientsTextField"
    private TextField recipientsTextField; // Value injected by FXMLLoader

    @FXML // fx:id="recipiantTextArea"
    private TextArea recipiantTextArea; // Value injected by FXMLLoader

    private Email newEmail;
    private boolean dirtyTextArea = true;
    private String[] recipientsBuffer;

    /**
     * @param emailManager
     * @param viewFactory  abstract view controller
     * @param fxmlName     fxml file path of this controller
     */
    public ComposeWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @FXML
    void checkRecipients(){
        if(checkRecipientsTextField()){
            clearTextAreaAction();
        }
    }

    private boolean checkRecipientsTextField() {
        System.out.println("checkRecipientsTextField() called.");
        String pattern = "([^@,;\s]+@[^@,;\s]+)|(?:$|\s*[,;])(?:\s*)(.*?)<([^@,;\s]+@[^@,;\s]+)>";
        Pattern p = Pattern.compile(pattern);

        String text = this.recipientsTextField.getText();
        recipientsBuffer = text.split(", ");
        Matcher matcher;

        for (String recipiant : recipientsBuffer) {
            System.out.println(recipiant);
            matcher = p.matcher(recipiant);
            if (!matcher.matches()) {
                System.out.println("email parser error!");
                ViewFactory.viewAlert("Attenzione!", "Controllare il campo destinatari");
                return false;
            }
        }
        System.out.println("no error in email");
        return true;
    }

    @FXML
    void sendMessageAction() {
        System.out.println("sendMessageAction() called.");

        if(checkRecipientsTextField()) {
            for(String recipient : recipientsBuffer) {
                Email email = new Email("triccheballacche@gmail.com",
                        recipient, subjectTextField.getText(),
                        recipiantTextArea.getText());
                System.out.println(email.toString());
            }



            Stage stage = (Stage) recipientsTextField.getScene().getWindow();
            viewFactory.closeStage(stage);
        }


        //spedisci il messaggio

    }

    @FXML
    void clearTextAreaAction() {
        if(dirtyTextArea) {
            System.out.println("clearTextAreaAction() called.");
            recipientsTextField.requestFocus();
            recipientsTextField.selectAll();
            recipientsTextField.setText("");
            dirtyTextArea = false;
        }
    }


}
