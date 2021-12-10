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
import unito.view.ViewFactory;
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
     * @param emailManager
     * @param viewFactory   abstract view controller
     * @param fxmlName      fxml file path of this controller
     */
    public MainWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
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

                        System.out.println("ONE clicked");

                        Email message = emailsTableView.getSelectionModel().getSelectedItem();

                        if (message != null) {
                            emailManager.setSelectedMessage(message);
                        } else {
                            System.out.println("message is null");
                        }

                    } else if (mouseEvent.getClickCount() == 2) {
                        Email message = emailsTableView.getSelectionModel().getSelectedItem();

                        if (message != null) {
                            emailManager.setSelectedMessage(message);
                            String windowTitle = message.getSubject();
                            viewFactory.showMessageWindow(windowTitle, message);
                            emailsTableView.getSelectionModel().clearSelection();
                        } else {
                            System.out.println("message is null");
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

    //TODO: Solo replyAll deve essere impostato su privato?

    /**
     * Risponde all'email selezionata
     */
    public void reply() { emailManager.reply(emailManager.getSelectedMessage()); }

    private void replyAll() { emailManager.replyAll(emailManager.getSelectedMessage()); }

    /**
     * Inoltra l'email selezionata
     */
    public void forward() { emailManager.forward(emailManager.getSelectedMessage()); }

    /**
     * Cancella l'email selezionata
     */
    public void delete() { emailManager.delete(emailManager.getSelectedMessage()); }

    /* Menu button action */

    /**
     * Mostra la finestra per comporre una nuova email
     */
    @FXML
    void newMessageAction() {
        viewFactory.showComposeWindow();
    }

    /**
     * Chiude l'applicazione
     */
    @FXML
    void quitAction() {
        Stage stage = (Stage) emailsTableView.getScene().getWindow();
        viewFactory.closeStage(stage);
        System.exit(0);
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
     *
     * @param event
     */
    @FXML
    void automaticRefreshAction(ActionEvent event) {
            emailManager.turnOnAutoRefresh(5000);
    }

    /**
     * Disattiva il refresh automatico
     *
     * @param event
     */
    @FXML
    void disableAutomaticRefreshAction(ActionEvent event) {
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

    public void setLabel(String s) {
        this.logLabel.setText(s);
    }

    /**
     * Inizializza il controller
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpEmailsList();
        setUpMessageSelection();
        setUpContextMenus();
    }

}
