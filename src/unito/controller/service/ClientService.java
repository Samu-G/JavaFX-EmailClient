package unito.controller.service;

//import org.jetbrains.annotations.NotNull;
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
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Classe Callable<ClientRequestResult> volta a fare una richiesta al Server (che può essere di vario tipo)
 */
public class ClientService implements Callable<ClientRequestResult> {

    private final EmailManager emailManager;
    private final ValidAccount myCredentials;
    private Socket socket;
    private final ClientRequestType clientRequestType;
    private final Email emailToSend;
    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;

    public ClientService(EmailManager emailManager, ClientRequestType clientRequestType, Email toSend) {
        this.emailManager = emailManager;
        this.myCredentials = new ValidAccount(emailManager.getCurrentAccount().getAddress(), emailManager.getCurrentAccount().getPassword());
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

                ClientRequestResult authenticationResult = (ClientRequestResult) inStream.readObject();

                boolean operationResult = false;

               if (authenticationResult != null) {
                    if (authenticationResult == ClientRequestResult.SUCCESS) {
                        operationResult = switch (clientRequestType) {
                            case HANDSHAKING -> handshaking();
                            case INVIOMESSAGGIO -> invioMessaggio(emailToSend);
                            case RICEVIMESSAGGIO -> riceviMessaggi();
                            case CANCELLAMESSAGGIO -> cancellaMessaggio(emailToSend);
                        };
                        if (operationResult) {
                            return ClientRequestResult.SUCCESS;
                        } else {
                            return ClientRequestResult.ERROR;
                        }
                    } else if (authenticationResult == ClientRequestResult.FAILED_BY_CREDENTIALS) {
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

    private boolean handshaking() {
        try {
            outStream.writeObject(clientRequestType);

            List<ValidEmail> myEmail = (List<ValidEmail>) inStream.readObject();

            if (myEmail != null) {
                emailManager.loadEmail(myEmail);
                System.out.println("Handshaking completed.");
                return true;
            } else {
                System.out.println("Handshaking FAILED. List<ValidEmail> is null.");
                return false;
            }

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            System.out.println("Handshaking FAILED.");
            return false;
        }
    }

    private boolean invioMessaggio(Email email) {
        try {
            outStream.writeObject(clientRequestType);

            ValidEmail validEmailToSend;

            if (email != null) {

                validEmailToSend = new ValidEmail(email);

                try {
                    outStream.writeObject(validEmailToSend);

                    System.out.println("Ricezione lista di indirizzi non trovati...");

                    List<String> addressesNotFounded = (LinkedList<String>) inStream.readObject();

                    if (!addressesNotFounded.isEmpty()) {
                        emailManager.addressesNotFoundedBuffer = addressesNotFounded;
                        return false;
                    } else {
                        emailManager.addressesNotFoundedBuffer = new LinkedList<>();
                        return true;
                    }

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    outStream.writeObject(null);
                    System.out.println("InvioMessaggio FAILED.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean riceviMessaggi() {

        List<ValidEmail> validEmailToRecive;

        try {
            outStream.writeObject(clientRequestType);

            validEmailToRecive = (List<ValidEmail>) inStream.readObject();
            emailManager.loadEmail(validEmailToRecive);
            System.out.println("RiceviMessaggi completed.");
            return true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("RiceviMessaggi FAILED.");
        return false;

    }

    private boolean cancellaMessaggio(Email toDelete) {
        boolean opResult = false;
        try {
            outStream.writeObject(clientRequestType);

            ValidEmail validEmailToSend;

            if (toDelete != null) {

                validEmailToSend = new ValidEmail(toDelete);

                try {

                    outStream.writeObject(validEmailToSend);
                    opResult = (boolean) inStream.readObject();
                    return opResult;

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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