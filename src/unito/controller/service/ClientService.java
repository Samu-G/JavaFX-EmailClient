package unito.controller.service;

import unito.EmailManager;
import unito.model.ValidAccount;
import unito.model.ValidEmail;
import unito.model.Email;
import unito.model.EmailAccount;
import unito.view.ViewFactory;

import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


public class ClientService implements Callable<ClientRequestResult> {

    private EmailManager emailManager;
    private ValidAccount myCredentials;
    private EmailAccount currentAccount;
    private Socket socket;
    private ClientRequestType clientRequestType;
    private List<Email> emailToSend;
    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;

    public ClientService(EmailManager emailManager, ClientRequestType clientRequestType, List<Email> toSend) {
        this.emailManager = emailManager;
        this.currentAccount = emailManager.getCurrentAccount();
        this.myCredentials = new ValidAccount(currentAccount.getAddress(), currentAccount.getPassword());
        this.clientRequestType = clientRequestType;
        this.emailToSend = toSend;
    }

    @Override
    public ClientRequestResult call() throws Exception {
        try {
            String nomeHost = InetAddress.getLocalHost().getHostName();

            System.out.println("In attesa di risposta dal server su " + nomeHost);

            socket = new Socket(nomeHost, 8189);

            System.out.println("Ho aperto il socket verso il server. \n");
            try {

                openStream();

                outStream.writeObject(myCredentials);

                ClientRequestResult result = (ClientRequestResult) inStream.readObject();

                System.out.println("risultato ottenuto");

                if (result != null) {
                    if (result == ClientRequestResult.SUCCESS) {
                        switch (clientRequestType) {
                            case HANDSHAKING:
                                handshaking();
                            case INVIOMESSAGGIO:
                                invioMessaggi(emailToSend);
                            case RICEVIMESSAGGIO:
                                riceviMessaggi();
                        }
                        return ClientRequestResult.SUCCESS;

                    } else if (result == ClientRequestResult.FAILED_BY_CREDENTIALS) {
                        return ClientRequestResult.FAILED_BY_CREDENTIALS;

                    } else {
                        return ClientRequestResult.ERROR;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (ConnectException e) {
            return ClientRequestResult.FAILED_BY_SERVER_DOWN;
        }

        closeStream();
        return null;
    }

    private void handshaking() {
        try {
            outStream.writeObject(clientRequestType);
            System.out.println("prima");
            List<ValidEmail> myEmail = (List<ValidEmail>) inStream.readObject();
            System.out.println("dopo");
            /* Mi trasferisco in emailManager le email appena scaricate dal server */
            emailManager.loadEmail(myEmail);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    private void invioMessaggi(List<Email> email) {
        List<ValidEmail> validEmailToSend = new ArrayList<>();

        if(email != null) {
            for (Email e : email) {
                ValidEmail toAdd = new ValidEmail(e.getSender(),
                        e.getRecipients(),
                        e.getSubject(),
                        e.getSize(),
                        e.getDate(),
                        e.getTextMessage());
                validEmailToSend.add(toAdd);
            }
        }

        try {
            outStream.writeObject(validEmailToSend);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void riceviMessaggi() {

        List<ValidEmail> validEmailToRecive;

        try {
            validEmailToRecive = (List<ValidEmail>) inStream.readObject();
            emailManager.loadEmail(validEmailToRecive);
            //TODO: da implementare solo se la lista non si aggiorna dopo loadEmail
            emailManager.refreshEmailList();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void openStream() {
        try {
            outStream = new ObjectOutputStream(socket.getOutputStream());
            inStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeStream() {
        try {
            outStream.close();
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}