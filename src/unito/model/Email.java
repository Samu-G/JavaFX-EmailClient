package unito.model;

import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
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
public class Email {

    private static long identifier;

    private SimpleStringProperty sender; //mittente

    private SimpleStringProperty recipients; //destinatari

    private String[] recipients_array;

    private SimpleStringProperty subject; //argomento

    private SimpleStringProperty size;

    private SimpleStringProperty date; //data di spedizione

    private Date effectiveDate;

    public String getTextMessage() {
        return textMessage.get();
    }

    private SimpleStringProperty textMessage;

    public Email(String sender, String[] recipient, String subject, String textMessage) {
        setIdentifier();
        this.sender = new SimpleStringProperty(sender);

        String s = new String();
        for (String rec : recipient) {
            s.concat(rec + " ");
        }
        this.recipients = new SimpleStringProperty(s);
        this.recipients_array = recipient;
        this.subject = new SimpleStringProperty(subject);
        this.size = new SimpleStringProperty(Integer.toString(textMessage.length()));
        this.effectiveDate = new Date();
        this.date = new SimpleStringProperty(new Date().toString());
        this.textMessage = new SimpleStringProperty(textMessage);
    }

    public Email(ValidEmail validEmail) {
        this.identifier = validEmail.getIdentifier();
        this.sender = new SimpleStringProperty(validEmail.getSender());

        String s = new String();
        for (String rec : validEmail.getRecipients()) {
            s.concat(rec + " ");
        }
        this.recipients = new SimpleStringProperty(s);
        this.recipients_array = validEmail.getRecipients();
        this.subject = new SimpleStringProperty(validEmail.getSubject());
        this.size = new SimpleStringProperty(Integer.toString(validEmail.getSize()));
        this.date = new SimpleStringProperty(validEmail.getDate().toString());
        this.textMessage = new SimpleStringProperty(validEmail.getTextMessage());
    }

    private void setIdentifier() {
        this.identifier = System.currentTimeMillis();
    }

    public String getSender() {
        return sender.get();
    }

    public String[] getRecipientsArray() {
        return this.recipients_array;
    }

    public String getRecipients() {
        return this.recipients.get();
    }

    public String getSubject() {
        return subject.get();
    }

    public String getSize() {
        return size.get();
    }

    public Date getDate() {
        return effectiveDate;
    }

    public static long getIdentifier() {
        return identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email that = (Email) o;
        return  Objects.equals(sender, that.sender) &&
                Objects.equals(subject, that.subject) &&
                Objects.equals(recipients, that.recipients) &&
                Objects.equals(date, that.date);
    }

    @Override
    public String toString() {
        return "Sender: " + getSender() + "\n" +
                "Receiver: " + getRecipientsArray() + "\n" +
                "Date: " + getDate() + "\n" +
                "Text: " + textMessage.get() + "\n" +
                "Identifier is: " + getIdentifier() + "\n";
    }
}
