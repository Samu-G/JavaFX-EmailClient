package unito.controller.service;

import unito.EmailManager;
import unito.controller.persistence.ValidAccount;
import unito.controller.persistence.ValidEmail;
import unito.model.Email;
import unito.model.EmailAccount;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.Callable;

//TODO: PERCHE' CALLABLE?
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
                            switch(clientRequestType) {
                                case HANDSHAKING: handshaking();
                                case INVIOMESSAGGIO: invioMessaggi(emailToSend);
                                case RICEVIMESSAGGIO: riceviMessaggi();
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
        } catch (Exception e1) {
            e1.printStackTrace();
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
            emailManager.loadValidEmailFromPersistence(myEmail);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    private void invioMessaggi(List<Email> email) {
        //TODO: USARE UN VALID ACCOUNT DA MANDARE IN STREAM

        //Creare un Array di ArrayList<ValidAccount>
        for {
            ArrayList<ValidAccount>
        }

        inStream.writeObject(validAccountToSend);


    }

    private void riceviMessaggi() {


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