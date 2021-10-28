package unito.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import unito.EmailManager;
import unito.controller.*;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Abstract controller class, used to manipulate View (initialize the Window, connect the model to the View, ... )
 */
public class ViewFactory {

    private EmailManager emailManager;
    private ArrayList<Stage> activeStages;

    public ViewFactory(EmailManager emailManager) {
        this.emailManager = emailManager;
        activeStages = new ArrayList<Stage>();

    }


    /**
     * Create controller for LoginWindow. Then, initialize the account selection window.
     */
    public void showAccountSelectionWindow(){
        System.out.println("showAccountSelectionWindow() called.");
        System.out.println("initializing controller for the login view...");
        BaseController controller = new AccountSelectionWindowController(emailManager, this, "AccountSelectionWindow.fxml");
        initializeView(controller, "Seleziona l'account per la prima volta");
    }

    /**
     * Create controller for MainWindow. Then, initialize the Main window.
     */
    public void showMainWindow(){
        System.out.println("showMainWindow() called.");
        System.out.println("initializing controller for the main window...");
        BaseController controller = new MainWindowController(emailManager, this, "MainWindow.fxml");
        initializeView(controller, "Client");
    }


    /**
     * Create controller for ComposeWindow. Then, initialize the Composing window.
     */
    public void showComposeWindow(){
        System.out.println("showComposeWindow() called.");
        System.out.println("initializing controller for the composing window...");
        BaseController controller = new ComposeWindowController(emailManager, this, "ComposeWindow.fxml");
        initializeView(controller, "Componi un messaggio");
    }

    /**
     * Load fxml file, set the controller, create and show the Scene/Stage.
     * @param baseController Controller of the Stage
     */
    private void initializeView(BaseController baseController, String windowTitle){
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

    public void showMessageWindow(String windowTitle) {
        System.out.println("showMessageWindow() called.");
        System.out.println("initializing controller for the message window...");
        BaseController controller = new MessageWindowController(emailManager, this, "MessageWindow.fxml");
        initializeView(controller, windowTitle);
    }

    public static void viewAlert(String title, String contentText) {
        System.out.println("viewAllert() called.");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

}
