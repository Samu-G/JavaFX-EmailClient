package unito.model;

import javafx.beans.property.SimpleStringProperty;
import unito.model.Email;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * I messaggi di posta elettronica sono istanze di una classe Email
 * - ID univoco (hash) CE
 * - mittente CE
 * - destinatario CE
 * - argomento CE
 * - testo del messaggio
 * - data di spedizione
 * <p>
 * <p>
 * USARE LE P R O P E R T Y
 */
public class ValidEmail implements Serializable {

    private static long identifier;

    private String sender;

    private String recipients; //destinatari

    private String subject; //oggetto

    private String size;

    private String date; //data di creazione

    private String textMessage; //testo del messaggio

    public ValidEmail(String sender, String recipients, String subject, String size, String date, String textMessage) {
        this.sender = sender;
        this.recipients = recipients;
        this.subject = subject;
        this.size = size;
        this.date = date;
        this.textMessage = textMessage;
        setIdentifier();
        //System.out.println("email creata " + getSender());
    }

    public static long getIdentifier() {
        return identifier;
    }


    private void setIdentifier() {
        this.identifier = System.currentTimeMillis();
    }

    public String getSender() {
        return sender;
    }

    public String getRecipients() {
        return recipients;
    }

    public String getSubject() {
        return subject;
    }

    public String getSize() {
        return size;
    }

    public String getDate() {
        return date;
    }

    public String getTextMessage() {
        return textMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email that = (Email) o;
        return  Objects.equals(sender, that.getSender()) &&
                Objects.equals(subject, that.getSubject()) &&
                Objects.equals(recipients, that.getRecipients()) &&
                Objects.equals(date, that.getDate());
    }

    @Override
    public String toString() {
        return "Identifier is: " + getIdentifier() + "\n" + "Sender: " + getSender() +"\n" + "Reciver: " + getRecipients() + "\n" +
                "Subject: " + getSubject() + "\n" + "Size: " + getSize() + "\n" + "Date: " + getDate() +"\n" + "Text: " + getTextMessage() +"\n";
    }
}
