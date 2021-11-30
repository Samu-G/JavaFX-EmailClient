package unito.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Classe Email (model)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email that = (Email) o;
        return  Objects.equals(sender, that.getSender()) &&
                Objects.equals(subject, that.getSubject()) &&
                Objects.equals(recipients, that.getRecipientsArray()) &&
                Objects.equals(date, that.getDate());
    }

    @Override
    public String toString() {
        return "Identifier is: " + getIdentifier() + "\n" + "Sender: " + getSender() +"\n" + "Reciver: " + getRecipients() + "\n" +
                "Subject: " + getSubject() + "\n" + "Size: " + getSize() + "\n" + "Date: " + getDate() +"\n" + "Text: " + getTextMessage() +"\n";
    }
}
