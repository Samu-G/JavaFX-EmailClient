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
import unito.controller.service.ClientRequestType;
import unito.controller.service.ClientService;
import unito.model.Email;
import unito.view.ViewFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController extends BaseController implements Initializable {

    private final MenuItem Rispondi = new MenuItem("Rispondi");
    private final MenuItem Rispondi_a_tutti = new MenuItem("Rispondi a tutti");
    private final MenuItem Inoltra = new MenuItem("Inoltra");
    private final MenuItem Cancella = new MenuItem("Cancella");


    @FXML
    private RadioMenuItem refreshRadioButton2;

    @FXML
    private RadioMenuItem refreshRadioButton5;

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

    /**
     * @param emailManager the client manager
     * @param viewFactory  abstract view controller
     * @param fxmlName     fxml file path of this controller
     */
    public MainWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    /* Setup */

    private void setUpContextMenus() {

        //TODO: da implementare
        Rispondi.setOnAction(e -> {
            System.out.println("Rispondi contextualMenuItem pressed.");
            reply();
        });

        Rispondi_a_tutti.setOnAction(e -> {
            System.out.println("Rispondi_a_tutti contextualMenuItem pressed.");
            replAll();
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

    private void setUpMenuAction() {
        ToggleGroup toggleGroup = new ToggleGroup();
        refreshRadioButton2.setToggleGroup(toggleGroup);
        refreshRadioButton2.setToggleGroup(toggleGroup);
    }

    /* Context table menu function */

    public void reply() {
        if (emailManager.getSelectedMessage() != null) {
            String recipients = emailManager.getSelectedMessage().getRecipients();
            if (recipients != null) {
                viewFactory.showComposeWindow();
                viewFactory.composeWindowController.setSubjectTextField(emailManager.getSelectedMessage().getSubject());
                viewFactory.composeWindowController.setRecipientsTextField(emailManager.getSelectedMessage().getSender());
            }
        }
    }


    private void replAll() {
        if (emailManager.getSelectedMessage() != null) {
        viewFactory.showComposeWindow();
        if(viewFactory.composeWindowController != null) {
            viewFactory.composeWindowController.setSubjectTextField(emailManager.getSelectedMessage().getSubject());
            viewFactory.composeWindowController.setRecipientsTextField(String.join(",", emailManager.getSelectedMessage().getRecipientsArray() ));
        }
        }
    }

    public void forward() {
        if (emailManager.getSelectedMessage() != null) {
            viewFactory.showComposeWindow();

            if (viewFactory.composeWindowController != null) {
                viewFactory.composeWindowController.setSubjectTextField(emailManager.getSelectedMessage().getSubject());
                viewFactory.composeWindowController.setMessageTextArea(emailManager.getSelectedMessage().getTextMessage());
            }
        }
    }

    public void delete() {
        if (emailManager.getSelectedMessage() != null) {
            emailManager.deleteSelectedMessage();
            emailManager.emailList.remove(emailManager.getSelectedMessage());
        }
    }

    /* Menu button action */

    @FXML
    void newMessageAction() {
        viewFactory.showComposeWindow();
    }

    @FXML
        //TODO(MB): Vogliamo solo chiudere l'app oppure implementare la possibilità di cambiare account?
    void quitAction() {
        Stage stage = (Stage) emailsTableView.getScene().getWindow();
        viewFactory.closeStage(stage);
        System.exit(0);
    }

    @FXML
    void manualUpdateMessageAction() {
        emailManager.manualRefresh();
    }

    @FXML
    void automaticRefreshAction(ActionEvent event) {
        if (((CheckMenuItem) event.getSource()).isSelected()) {
            refreshRadioButton2.setSelected(false);
            refreshRadioButton5.setSelected(true);
            emailManager.turnOnAutoRefresh(5000);
        } else {
            emailManager.turnOffAutoRefresh();
        }
    }

    @FXML
    void setRefreshSpeed(ActionEvent event) {
        RadioMenuItem itemPressed = (RadioMenuItem) event.getSource();
        System.out.println(itemPressed);
        switch (itemPressed.getId()) {
            case "refreshRadioButton2" -> emailManager.setRefreshSpeed(2000);
            case "refreshRadioButton5" -> emailManager.setRefreshSpeed(5000);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpEmailsList();
        setUpMessageSelection();
        setUpContextMenus();
        setUpMenuAction();
    }

}
