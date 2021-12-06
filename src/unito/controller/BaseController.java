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


    public BaseController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        this.emailManager = emailManager;
        this.viewFactory = viewFactory;
        this.fxmlName = fxmlName;
    }

    public String getFxmlName() {
        return fxmlName;
    }

}