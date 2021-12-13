package unito.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import unito.EmailManager;
import unito.model.Email;
import unito.view.ViewManager;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Classe del controller utilizzato per la finestra principale dell'account (Vedere le email, ...)
 */
public class MainWindowController extends BaseController implements Initializable {

    @FXML
    private TableView<Email> emailsTableView;

    @FXML
    private TableColumn<Email, String> dateCol;

    @FXML
    private TableColumn<Email, String> subjectCol;

    @FXML
    private TableColumn<Email, String> senderCol;

    @FXML
    private TableColumn<Email, String> recipientCol;

    @FXML
    private TableColumn<Email, String> sizeCol;

    @FXML
    private Label logLabel;

    @FXML
    private MenuItem refreshRadioButton2;

    @FXML
    private MenuItem refreshRadioButton5;

    private final MenuItem Rispondi;
    private final MenuItem Rispondi_a_tutti;
    private final MenuItem Inoltra;
    private final MenuItem Cancella;

    /**
     * @param emailManager riferimento all'emailManger dell'applicazione
     * @param viewManager riferimento al viewManager dell'applicazione
     * @param fxmlName path del file .fxml
     */
    public MainWindowController(EmailManager emailManager, ViewManager viewManager, String fxmlName) {
        super(emailManager, viewManager, fxmlName);
        this.Rispondi = new MenuItem("Rispondi");
        this.Rispondi_a_tutti = new MenuItem("Rispondi a tutti");
        this.Inoltra = new MenuItem("Inoltra");
        this.Cancella = new MenuItem("Cancella");
    }

    /* Setup */

    private void setUpContextMenus() {
        Rispondi.setOnAction(e -> {
            System.out.println("Rispondi contextualMenuItem pressed.");
            reply();
        });

        Rispondi_a_tutti.setOnAction(e -> {
            System.out.println("Rispondi_a_tutti contextualMenuItem pressed.");
            replyAll();
        });

        Inoltra.setOnAction(e -> {
            System.out.println("Inoltra contextualMenuItem pressed.");
            forward();
        });

        Cancella.setOnAction(e -> {
            System.out.println("Cancella contextualMenuItem pressed.");
            delete();
        });

    }

    private void setUpMessageSelection() {
        emailsTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 1) {

                        Email message = emailsTableView.getSelectionModel().getSelectedItem();

                        if (message != null) {
                            emailManager.setSelectedMessage(message);
                        } else {
                            System.out.println("setUpMessageSelection(): message is null");
                        }

                    } else if (mouseEvent.getClickCount() == 2) {
                        Email message = emailsTableView.getSelectionModel().getSelectedItem();

                        if (message != null) {
                            emailManager.setSelectedMessage(message);
                            String windowTitle = message.getSubject();
                            viewManager.showMessageWindow(windowTitle, message);
                            emailsTableView.getSelectionModel().clearSelection();
                        } else {
                            System.out.println("setUpMessageSelection(): message is null");
                        }

                    }
                }
            }
        });
    }

    private void setUpEmailsList() {
        //inizializzazione delle colonne
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        senderCol.setCellValueFactory(new PropertyValueFactory<>("sender"));

        recipientCol.setCellValueFactory(new PropertyValueFactory<>("textMessage"));

        subjectCol.setCellValueFactory(new PropertyValueFactory<>("subject"));

        sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));

        //inizializzazione del context menu
        emailsTableView.setContextMenu(new ContextMenu(Rispondi, Rispondi_a_tutti, Inoltra, Cancella));

        //set del contenuto della tabella
        emailsTableView.setItems(emailManager.getEmailList());
    }

    /* Context table menu function */

    private void reply() { emailManager.reply(emailManager.getSelectedMessage()); }

    private void replyAll() { emailManager.replyAll(emailManager.getSelectedMessage()); }

    private void forward() { emailManager.forward(emailManager.getSelectedMessage()); }

    private void delete() { emailManager.delete(emailManager.getSelectedMessage()); }

    /* Menu button action */

    /**
     * Mostra la finestra per comporre una nuova email
     */
    @FXML
    void newMessageAction() {
        viewManager.showComposeWindow();
    }

    /**
     * Chiude l'applicazione
     */
    @FXML
    void quitAction() {
        Stage stage = (Stage) emailsTableView.getScene().getWindow();
        viewManager.closeStage(stage);
    }

    /**
     * Aggiorna la lista di email manualmente
     */
    @FXML
    void manualUpdateMessageAction() {
        emailManager.manualRefresh();
    }

    /**
     * Attiva il refresh automatico (5000 ms)
     */
    @FXML
    void automaticRefreshAction() {
            emailManager.turnOnAutoRefresh(5000);
    }

    /**
     * Disattiva il refresh automatico
     */
    @FXML
    void disableAutomaticRefreshAction() {
        emailManager.turnOffAutoRefresh();
    }

    /**
     * Fissa la velocità del refresh
     *
     * @param event
     */
    @FXML
    void setRefreshSpeed(ActionEvent event) {
        MenuItem itemPressed = (MenuItem) event.getSource();
        System.out.println(itemPressed);
        switch (itemPressed.getId()) {
            case "refreshRadioButtonForce" -> emailManager.setRefreshSpeed(50);
            case "refreshRadioButton2" -> emailManager.setRefreshSpeed(2000);
            case "refreshRadioButton5" -> emailManager.setRefreshSpeed(5000);
        }
    }

    /**
     * Setter stringa di log della finestra principale
     *
     * @param s la stringa per la label
     */
    public void setLabel(String s) {
        this.logLabel.setText(s);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpEmailsList();
        setUpMessageSelection();
        setUpContextMenus();
    }

}
