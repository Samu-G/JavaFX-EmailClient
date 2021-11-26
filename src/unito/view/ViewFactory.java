package unito.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import unito.EmailManager;
import unito.controller.*;
import unito.model.Email;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe usata per manipolare la view (inizializzare le Window, connettere il controller alla View, ... )
 */
public class ViewFactory {

    private final EmailManager emailManager;
    private final ArrayList<Stage> activeStages;
    public AccountSelectionWindowController accountSelectionWindowController;
    public MainWindowController mainWindowController;
    public ComposeWindowController composeWindowController;
    public MessageWindowController messageWindowController;

    public ViewFactory(EmailManager emailManager) {
        this.emailManager = emailManager;
        activeStages = new ArrayList<Stage>();
    }

    /**
     * Create controller for LoginWindow. Then, initialize the account selection window.
     */
    public void showAccountSelectionWindow() {
        System.out.println("showAccountSelectionWindow() called.");
        accountSelectionWindowController = new AccountSelectionWindowController(emailManager, this, "AccountSelectionWindow.fxml");
        initializeView(accountSelectionWindowController, "Seleziona l'account per la prima volta");
    }

    /**
     * Create controller for MainWindow. Then, initialize the Main window.
     */
    public void showMainWindow() {
        System.out.println("showMainWindow() called.");
        mainWindowController = new MainWindowController(emailManager, this, "MainWindow.fxml");
        initializeView(mainWindowController, "Client");
    }

    /**
     * Create controller for ComposeWindow. Then, initialize the Composing window.
     */
    public void showComposeWindow() {
        System.out.println("showComposeWindow() called.");
        composeWindowController = new ComposeWindowController(emailManager, this, "ComposeWindow.fxml");
        initializeView(composeWindowController, "Componi un messaggio");
    }

    public void showMessageWindow(String windowTitle, Email emailToView) {
        System.out.println("showMessageWindow() called.");
        messageWindowController = new MessageWindowController(emailManager, this, "MessageWindow.fxml", emailToView);
        initializeView(messageWindowController, windowTitle);
    }

    /**
     * Load fxml file, set the controller, create and show the Scene/Stage.
     *
     * @param baseController Controller of the Stage
     */
    private void initializeView(BaseController baseController, String windowTitle) {
        System.out.println("initializeView() called.");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(baseController.getFxmlName()));
        fxmlLoader.setController(baseController);
        Parent parent;
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(windowTitle);
        stage.show();
        activeStages.add(stage);
    }

    public void closeStage(Stage stageToClose) {
        activeStages.remove(stageToClose);
        stageToClose.close();
    }

    public static void viewAlert(String title, String contentText) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("viewAllert() called.");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(contentText);
                alert.showAndWait();
            }
        });
    }

    public void writeOnLogLabel(String s) {
        ViewFactory vf = this;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vf.mainWindowController.setLabel(s);
            }
        });
    }

}
