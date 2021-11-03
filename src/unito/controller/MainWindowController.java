package unito.controller;

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

public class MainWindowController extends BaseController implements Initializable {

    private final MenuItem Rispondi = new MenuItem("Rispondi");
    private final MenuItem Rispondi_a_tutti = new MenuItem("Rispondi a tutti");
    private final MenuItem Inoltra = new MenuItem("Inoltra");
    private final MenuItem Cancella = new MenuItem("Cancella");

    @FXML // fx:id="emailsTableView"
    private TableView<Email> emailsTableView; // Value injected by FXMLLoader

    @FXML // fx:id="dateCol"
    private TableColumn<Email, String> dateCol; // Value injected by FXMLLoader

    @FXML // fx:id="subjectCol"
    private TableColumn<Email, String> subjectCol; // Value injected by FXMLLoader

    @FXML // fx:id="senderCol"
    private TableColumn<Email, String> senderCol; // Value injected by FXMLLoader

    @FXML // fx:id="recipientCol"
    private TableColumn<Email, String> recipientCol; // Value injected by FXMLLoader

    @FXML // fx:id="sizeCol"
    private TableColumn<Email, String> sizeCol; // Value injected by FXMLLoader

    @FXML // fx:id="logLabel"
    private Label logLabel; // Value injected by FXMLLoader


    /**
     * @param emailManager the client manager
     * @param viewFactory  abstract view controller
     * @param fxmlName     fxml file path of this controller
     */
    public MainWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @FXML
    void newMessageAction() {
        viewFactory.showComposeWindow();
    }

    @FXML
    void quitAction() {
        Stage stage = (Stage) emailsTableView.getScene().getWindow();
        viewFactory.closeStage(stage);
        System.exit(0);
    }

    private void setUpContextMenus() {

        //TODO: da implementare
        Rispondi.setOnAction(e -> {
            System.out.println("Rispondi contextualMenuItem pressed.");
            replySelectedMessage();
        });

        Rispondi_a_tutti.setOnAction(e -> {
            System.out.println("Rispondi_a_tutti contextualMenuItem pressed.");
            replySelectedMessage();
        });

        Inoltra.setOnAction(e -> {
            System.out.println("Inoltra contextualMenuItem pressed.");
            forwardSelectedMessage();
        });

        Cancella.setOnAction(e -> {
            System.out.println("Cancella contextualMenuItem pressed.");
            deleteSelectedMessage();
        });

    }

    private void setUpMessageSelection() {
        emailsTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {

                        System.out.println("Double clicked");

                        Email message = emailsTableView.getSelectionModel().getSelectedItem();

                        if (message != null) {
                            emailManager.setSelectedMessage(message);
                            String windowTitle = message.getSubject();
                            viewFactory.showMessageWindow(windowTitle);
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

    public void replySelectedMessage() {
        if (emailManager.getSelectedMessage() != null) {
            String recipients = emailManager.getSelectedMessage().getRecipients();
            if (recipients != null) {
                viewFactory.showComposeWindow();
                viewFactory.composeWindowController.setRecipiantTextArea(recipients);
            }
        }
    }

    public void forwardSelectedMessage() {
        if (emailManager.getSelectedMessage() != null) {
            viewFactory.showComposeWindow();

            if (viewFactory.composeWindowController != null) {
                viewFactory.composeWindowController.setSubjectTextField(emailManager.getSelectedMessage().getSubject());
                viewFactory.composeWindowController.setRecipiantTextArea(emailManager.getSelectedMessage().getTextMessage());
            }
        }
    }

    public void deleteSelectedMessage() {
        if(emailManager.getSelectedMessage() != null) {
            emailManager.emailList.remove(emailManager.getSelectedMessage());
            //TODO:refresh
            //emailsTableView.refresh();

            // chiamata per il refresh
            refreshEmailList();
        }
    }

    //prova
    public void refreshEmailList() {

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpEmailsList();
        setUpMessageSelection();
        setUpContextMenus();
    }

}
