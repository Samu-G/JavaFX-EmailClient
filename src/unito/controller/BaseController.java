package unito.controller;

import unito.EmailManager;
import unito.view.ViewFactory;

/**
 * Questa è una classe astratta che esplicita la base dei controller.
 */
public abstract class BaseController {

    protected EmailManager emailManager;
    protected ViewFactory viewFactory;
    private final String fxmlName;

    /**
     * @param emailManager
     * @param viewFactory abstract view controller
     * @param fxmlName fxml file path of this controller
     */
    public BaseController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        this.emailManager = emailManager;
        this.viewFactory = viewFactory;
        this.fxmlName = fxmlName;
    }

    /**
     * @return il nome del file .fxml associato al controller
     */
    public String getFxmlName() {
        return fxmlName;
    }

}