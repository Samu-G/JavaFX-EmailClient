package unito.controller.service;

import unito.EmailManager;
import unito.model.ValidAccount;
import unito.model.ValidEmail;
import unito.model.Email;

import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
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
        ValidAccount myCredentialsTemp;
        this.emailManager = emailManager;
        try {
            myCredentialsTemp = new ValidAccount(emailManager.getCurrentAccount().getAddress(), emailManager.getCurrentAccount().getPassword());
        } catch (RuntimeException e) {
            myCredentialsTemp = new ValidAccount(null, null);
        }
        this.myCredentials = myCredentialsTemp;
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

                boolean operationResult;

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
            } finally {
                closeStream();
            }
        } catch (ConnectException e) {
            return ClientRequestResult.FAILED_BY_SERVER_DOWN;
        }

        return null;
    }

    private boolean handshaking() {
        try {
            outStream.writeObject(clientRequestType);

            List<ValidEmail> myEmail = null;

            Object o = inStream.readObject();

            if (o instanceof List) {
                if (!((List<?>) o).isEmpty()) {
                    if (((List<?>) o).get(0) instanceof ValidEmail) {
                        myEmail = (List<ValidEmail>) o;
                    }
                }
            }

            if (myEmail != null) {
                emailManager.loadEmail(myEmail);
                System.out.println("Handshaking completed.");
                return true;
            } else {
                System.out.println("Handshaking FAILED. List<ValidEmail> is null or not valid.");
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

                    LinkedList<String> addressesNotFounded = new LinkedList<>();

                    Object o = inStream.readObject();

                    if (o instanceof List) {
                        if (!((List<?>) o).isEmpty()) {
                            if (((List<?>) o).get(0) instanceof String) {
                                addressesNotFounded = (LinkedList<String>) o;
                            }
                        }
                    }

                    if (!addressesNotFounded.isEmpty()) {
                        emailManager.setAddressesNotFoundedBuffer(addressesNotFounded);
                        return false;
                    } else {
                        emailManager.setAddressesNotFoundedBuffer(new LinkedList<>());
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
        try {
            outStream.writeObject(clientRequestType);

            List<ValidEmail> validEmailToReceive;

            Object o = inStream.readObject();

            if (o instanceof List) {
                if (!((List<?>) o).isEmpty()) {
                    if (((List<?>) o).get(0) instanceof ValidEmail) {
                        validEmailToReceive = (List<ValidEmail>) o;
                        emailManager.loadEmail(validEmailToReceive);
                        System.out.println("RiceviMessaggi completed.");
                        return true;
                    }
                }
            } else {
                System.out.println("RiceviMessaggi FAILED.");
                return false;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean cancellaMessaggio(Email toDelete) {
        boolean opResult;
        try {
            outStream.writeObject(clientRequestType);

            ValidEmail validEmailToSend;

            if (toDelete != null) {

                validEmailToSend = new ValidEmail(toDelete);

                try {

                    outStream.writeObject(validEmailToSend);

                    Object o = inStream.readObject();

                    if (o instanceof Boolean) {
                        opResult = (Boolean) o;
                        return opResult;
                    } else {
                        System.out.println("CancellaMessaggio FAILED.");
                        return false;
                    }

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