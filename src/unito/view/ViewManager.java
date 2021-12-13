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
public class ViewManager {

    private final EmailManager emailManager;
    private final ArrayList<Stage> activeStages;
    private AccountSelectionWindowController accountSelectionWindowController;
    private MainWindowController mainWindowController;
    private ComposeWindowController composeWindowController;
    private MessageWindowController messageWindowController;

    public MainWindowController getMainWindowController() {
        return mainWindowController;
    }

    public ComposeWindowController getComposeWindowController() {
        return composeWindowController;
    }

    /**
     * @param emailManager
     */
    public ViewManager(EmailManager emailManager) {
        this.emailManager = emailManager;
        activeStages = new ArrayList<Stage>();
    }

    /**
     * Crea un controller per la finestra di Login, dopo inizializza la finestra di selezione dell'account
     */
    public void showAccountSelectionWindow() {
        System.out.println("showAccountSelectionWindow() called.");
        accountSelectionWindowController = new AccountSelectionWindowController(emailManager, this, "AccountSelectionWindow.fxml");
        initializeView(accountSelectionWindowController, "Seleziona l'account per la prima volta");
    }

    /**
     * Crea il controller per la finestra principale, dopo inizializza la finestra principale
     */
    public void showMainWindow() {
        System.out.println("showMainWindow() called.");
        mainWindowController = new MainWindowController(emailManager, this, "MainWindow.fxml");
        initializeView(mainWindowController, "Client");
    }

    /**
     * Crea il controller per la finestra di composizione, dopo inizializza la finestra di composizione
     */
    public void showComposeWindow(boolean clearTextArea) {
        System.out.println("showComposeWindow() called.");
        composeWindowController = new ComposeWindowController(emailManager, this, "ComposeWindow.fxml", clearTextArea);
        initializeView(composeWindowController, "Componi un messaggio");
    }

    /**
     * Crea il controller per la finestra di visualizzazione dell'email, dopo inizializza la finestra di visualizzazione
     *
     * @param windowTitle titolo della finestra
     * @param emailToView l'email selezionata
     */
    public void showMessageWindow(String windowTitle, Email emailToView) {
        System.out.println("showMessageWindow() called.");
        messageWindowController = new MessageWindowController(emailManager, this, "MessageWindow.fxml", emailToView);
        initializeView(messageWindowController, windowTitle);
    }

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

    /**
     * Chiude una finestra rimuovendola dalla lista activeStages
     *
     * @param stageToClose finestra da chiudere
     */
    public void closeStage(Stage stageToClose) {
        activeStages.remove(stageToClose);
        stageToClose.close();
    }

    /**
     * Crea un alert per informare il Client
     *
     * @param title titolo dell'alert
     * @param contentText contenuto dell'alert
     */
    public static void viewAlert(String title, String contentText) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("viewAlert() called.");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(contentText);
                alert.showAndWait();
            }
        });
    }

    /**
     * Setter stringa di log della finestra principale
     *
     * @param s la stringa per la label
     */
    public void writeOnLogLabel(String s) {
        ViewManager vf = this;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vf.mainWindowController.setLabel(s);
            }
        });
    }

}
