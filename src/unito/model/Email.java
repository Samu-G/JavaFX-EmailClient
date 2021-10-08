package unito.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import jdk.jfr.Timestamp;

import javax.sound.midi.SysexMessage;
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
public class Email implements Serializable {

    private static long identifier;
    private SimpleStringProperty sender; //mittente

    private SimpleStringProperty recipients; //destinatari

    private SimpleStringProperty subject; //argomento

    private SimpleStringProperty size;

    private SimpleStringProperty date; //data di spedizione

    public String getTextMessage() {
        return textMessage.get();
    }

    public SimpleStringProperty textMessageProperty() {
        return textMessage;
    }

    private SimpleStringProperty textMessage;

    private boolean isRead;

    public Email(String sender, String recipient, String subject, String textMessage) {
        Date timeStamp = new Date(System.currentTimeMillis());
        this.date = new SimpleStringProperty( timeStamp.toString() );
        this.sender = new SimpleStringProperty(sender);
        this.recipients = new SimpleStringProperty(recipient);
        this.subject = new SimpleStringProperty(subject);
        this.textMessage = new SimpleStringProperty(textMessage);
        this.size = new SimpleStringProperty("0");
        this.isRead = false;
        System.out.println(this.toString());
    }

    public String getSender() {
        return sender.get();
    }
    public String getRecipients() {
        return recipients.get();
    }
    public String getSubject() {
        return subject.get();
    }
    public String getSize() { return size.get(); }
    public String getDate() {
        return date.get();
    }
    public static long getIdentifier() {
        return identifier;
    }
    public boolean isRead() {
        return isRead;
    }
    public void setRead(boolean read) {
        isRead = read;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email that = (Email) o;
        return isRead == that.isRead &&
                Objects.equals(sender, that.sender) &&
                Objects.equals(subject, that.subject) &&
                Objects.equals(recipients, that.recipients) &&
                Objects.equals(date, that.date);
    }
    @Override
    public String toString() {
         return "Sender: " + getSender() +"\n" +
                 "Reciver: " + getRecipients() + "\n" +
                 "Date: " + getDate() +"\n" +
                 "Text: " + textMessage.get() +"\n" +
                 "Identifier is: " + getIdentifier() + "\n";
    }
}
