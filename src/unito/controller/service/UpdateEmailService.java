package unito.controller.service;

import unito.EmailManager;
import unito.controller.persistence.ValidAccount;
import unito.controller.persistence.ValidEmail;
import unito.model.EmailAccount;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.Callable;

public class UpdateEmailService implements Callable<RequestResult> {

    private EmailManager emailManager;
    private ValidAccount myCredentials;

    public UpdateEmailService(EmailManager emailManager) {
        this.emailManager = emailManager;
        /* Preparo l'oggetto ValidAccount (serializzabile) per accedere al server */
        EmailAccount currentAccount = emailManager.getCurrentAccount();
        myCredentials = new ValidAccount(currentAccount.getAddress(), currentAccount.getPassword());
    }

    @Override
    public RequestResult call() throws Exception {
        try {
            String nomeHost = InetAddress.getLocalHost().getHostName();

            //qui dobbiamo ancora connetterci!
            System.out.println("In attesa di risposta dal server su " + nomeHost);

            //richiedo la connessione
            Socket s = new Socket(nomeHost, 8189);

            System.out.println("Ho aperto il socket verso il server. \n");

            try {

                //dichiarati speculari
                ObjectOutputStream outStream = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream inStream = new ObjectInputStream(s.getInputStream());

                //TODO: test da elliminare
                //apro lo stream di output
                //qui sto sfruttando il tunnel di ObjectInputStream e non di OutputStream
                //PrintWriter out = new PrintWriter(outStream, true /* autoFlush */);
                /*
                // invio la prima stringa al server
                out.println( "Hello! I'm a client, i'm idenified as " + emailManager.getCurrentAccount().toString() );
                */

                // preparo le mie credenziali
                outStream.writeObject(myCredentials);

                // ricevo le email
                try {
                    List<ValidEmail> myEmail = (List<ValidEmail>) inStream.readObject();

                    //3.5)se l'oggetto è null, significa che le credenziali sono errate
                    if (myEmail == null) {
                        System.out.println("Credenziali non accettate.");
                        return RequestResult.FAILED_BY_CREDENTIALS;
                    }

                    /* Mi trasferisco in emailManager le email appena scaricate dal server */
                    emailManager.loadValidEmailFromPersistence(myEmail);

                } catch (ClassNotFoundException | EOFException e) {
                    e.printStackTrace();
                }
            } finally {
                s.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return RequestResult.FAILED_BY_SERVER_DOWN;
        }
        return RequestResult.SUCCESS;
    }
}
