package unito.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

/**
 * Classe ValidEmail (model serializzabile)
 */
public class ValidEmail implements Serializable {

    private final long identifier;

    private final String sender;

    private final String [] recipients;

    private final String subject;

    private final int size;

    private final Date date;

    private final String textMessage;

    /*Constructor*/

    public ValidEmail(Email email) {
        this.identifier = email.getIdentifier();
        this.sender = email.getSender();
        this.recipients = email.getRecipientsArray();
        this.subject = email.getSubject();
        this.size = email.getSize();
        this.date = email.getDate();
        this.textMessage = email.getTextMessage();
    }

    /*Getter*/

    public long getIdentifier() {
        return identifier;
    }

    public String getSender() {
        return sender;
    }

    public String [] getRecipients() {
        return recipients;
    }

    public String getSubject() {
        return subject;
    }

    public int getSize() {
        return size;
    }

    public Date getDate() {
        return date;
    }

    public String getTextMessage() {
        return textMessage;
    }


    /*Aux*/

    /**
     * Controlla se l'oggetto corrisponde alla ValidEmail corrente
     *
     * @param o l'oggetto da confrontare
     * @return true se sono uguali, altrimenti false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidEmail that = (ValidEmail) o;
        return  Objects.equals(sender, that.getSender()) &&
                Objects.equals(subject, that.getSubject()) &&
                Arrays.equals(recipients, that.getRecipients()) &&
                Objects.equals(date, that.getDate());
    }

    /**
     * Crea una stringa adatta per la visualizzazione su standard output
     *
     * @return l'oggetto nella relativa rappresentazione di stringa
     */
    @Override
    public String toString() {
        return "Identifier is: " + getIdentifier() + "\n" + "Sender: " + getSender() +"\n" + "Receiver: " + Arrays.toString(getRecipients()) + "\n" +
                "Subject: " + getSubject() + "\n" + "Size: " + getSize() + "\n" + "Date: " + getDate() +"\n" + "Text: " + getTextMessage() +"\n";
    }
}
