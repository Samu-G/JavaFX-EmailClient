package unito.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class ValidEmail implements Serializable {

    private static long identifier;

    private final String sender;

    private final String [] recipients; //destinatari

    private final String subject; //oggetto

    private final int size;

    private final Date date; //data di creazione

    private final String textMessage; //testo del messaggio

    /*Constructor*/

    public ValidEmail(String sender, String [] recipients, String subject, String size, Date date, String textMessage) {
        setIdentifier();
        this.sender = sender;
        this.recipients = recipients;
        this.subject = subject;
        this.size = textMessage.length();
        this.date = date;
        this.textMessage = textMessage;
    }

    /*Getter*/

    public static long getIdentifier() {
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

    /*Private Setter*/

    private void setIdentifier() {
        this.identifier = System.currentTimeMillis();
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
