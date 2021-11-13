package unito.model;

import javafx.beans.property.SimpleStringProperty;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class Email {

    private static long identifier;

    private final SimpleStringProperty sender; //mittente

    private final SimpleStringProperty recipients; //destinatari

    private final String[] recipients_array;

    private final SimpleStringProperty subject; //argomento

    private final SimpleStringProperty size;

    private final SimpleStringProperty date; //data di spedizione

    private final SimpleStringProperty textMessage;

    private Date effectiveDate;

    public String getTextMessage() {
        return textMessage.get();
    }

    /*Constructor*/

    public Email(String sender, String[] recipient, String subject, String textMessage) {
        setIdentifier();
        this.sender = new SimpleStringProperty(sender);

        String s = "";
        for (String rec : recipient) {
            s.concat(rec + " ");
        }

        this.recipients = new SimpleStringProperty(s);
        this.recipients_array = recipient;
        this.subject = new SimpleStringProperty(subject);
        this.size = new SimpleStringProperty(Integer.toString(textMessage.length()));
        this.effectiveDate = new Date();
        this.date = new SimpleStringProperty(this.effectiveDate.toString());
        this.textMessage = new SimpleStringProperty(textMessage);
    }

    public Email(ValidEmail validEmail) {
        this.identifier = validEmail.getIdentifier();
        this.sender = new SimpleStringProperty(validEmail.getSender());

        String s = "";
        for (String rec : validEmail.getRecipients()) {
            s.concat(rec + ", ");
        }

        this.recipients = new SimpleStringProperty(s);
        this.recipients_array = validEmail.getRecipients();
        this.subject = new SimpleStringProperty(validEmail.getSubject());
        this.size = new SimpleStringProperty(Integer.toString(validEmail.getSize()));
        this.date = new SimpleStringProperty(validEmail.getDate().toString());
        this.textMessage = new SimpleStringProperty(validEmail.getTextMessage());
    }

    /*Getter*/

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

    /*Aux*/

    private void setIdentifier() {
        this.identifier = System.currentTimeMillis();
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
                "Reciver: " + Arrays.toString(getRecipientsArray()) + "\n" +
                "Date: " + getDate() + "\n" +
                "Text: " + textMessage.get() + "\n" +
                "Identifier is: " + getIdentifier() + "\n";
    }
}
