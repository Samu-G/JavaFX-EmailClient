package unito.controller;

import unito.EmailManager;
import unito.view.ViewManager;

/**
 * Questa è una classe astratta che esplicita la base dei controller
 */
public abstract class BaseController {

    protected final EmailManager emailManager;
    protected ViewManager viewManager;
    private final String fxmlName;

    /**
     * @param emailManager riferimento all'emailManger dell'applicazione
     * @param viewManager riferimento al viewManager dell'applicazione
     * @param fxmlName path del file .fxml
     */
    public BaseController(EmailManager emailManager, ViewManager viewManager, String fxmlName) {
        this.emailManager = emailManager;
        this.viewManager = viewManager;
        this.fxmlName = fxmlName;
    }

    /**
     * @return il nome del file .fxml associato al controller
     */
    public String getFxmlName() {
        return fxmlName;
    }

}