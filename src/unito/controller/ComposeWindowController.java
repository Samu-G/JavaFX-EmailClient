package unito.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import unito.EmailManager;
import unito.controller.service.ClientRequestResult;
import unito.controller.service.ClientRequestType;
import unito.controller.service.ClientService;
import unito.model.Email;
import unito.view.ViewManager;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe del controller utilizzato per la finestra di composizione
 */
public class ComposeWindowController extends BaseController {

    @FXML
    private TextField subjectTextField;

    @FXML
    private TextField recipientsTextField;

    @FXML
    private TextArea messageTextArea;

    /* Array di destinatari */
    private String[] recipientsBuffer;

    private boolean dirtyTextArea = true;

    /**
     * @param emailManager riferimento all'emailManger dell'applicazione
     * @param viewManager riferimento al viewManager dell'applicazione
     * @param fxmlName path del file .fxml
     */
    public ComposeWindowController(EmailManager emailManager, ViewManager viewManager, String fxmlName) {
        super(emailManager, viewManager, fxmlName);
    }

    /* Setter */

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
        recipientsBuffer = text.split(", ");
        Matcher matcher;

        for (String recipient : recipientsBuffer) {
            System.out.println(recipient);
            matcher = p.matcher(recipient);
            if (!matcher.matches()) {
                System.out.println("email parser error!");
                ViewManager.viewAlert("Attenzione!", "Controllare il campo destinatari");
                return false;
            }
        }
        return true;
    }

    /**
     * Esegue un controllo sintattico del campo destinatari, esegue un FutureTask (invia la Email al server)
     * e restituisce la risposta del server (con Alert)
     */
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

            viewManager.closeStage(stage);

            ClientService clientService = new ClientService(emailManager, ClientRequestType.INVIOMESSAGGIO, toSend);

            FutureTask<ClientRequestResult> sendService = new FutureTask<>(clientService);

            Thread thread = new Thread(sendService);

            thread.start();

            try {

                ClientRequestResult r = sendService.get();

                if (!emailManager.getAddressesNotFoundedBuffer().isEmpty()) {
                    ViewManager.viewAlert("ATTENZIONE", "I destinatari " + emailManager.getAddressesNotFoundedBuffer() + " sono inesistenti");
                }

                switch (r) {
                    case SUCCESS:
                        ViewManager.viewAlert("Successo", "Mail inviata con successo");
                        break;

                    case ERROR:
                        ViewManager.viewAlert("Errore", "C'è stato qualche errore nell'invio del messaggio");
                        break;

                    case FAILED_BY_CREDENTIALS:
                        ViewManager.viewAlert("Errore", "Errore nelle credenziali");
                        break;

                    case FAILED_BY_SERVER_DOWN:
                        ViewManager.viewAlert("Errore", "Il server è spento");
                        break;
                }

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Al click sul campo dei destinatari (solo per la prima volta) viene svuotato il campo
     */
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
