package unito.controller.service;

import unito.EmailManager;
import unito.model.EmailAccount;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;


public class LoginService extends Service<ClientRequestResult> {

    EmailAccount emailAccount;
    EmailManager emailManager;

    public LoginService(EmailAccount emailAccount, EmailManager emailManager) {
        this.emailAccount = emailAccount;
        this.emailManager = emailManager;
    }

    private ClientRequestResult login() {
        try {

            emailManager.setLogString("connessione in corso...");
            String nomeHost = InetAddress.getLocalHost().getHostName();
            System.out.println(nomeHost);
            Socket s = new Socket(nomeHost, 8189);
            System.out.println("Ho aperto il socket verso il server.\n");


            try {
                emailManager.setLogString("connessione RIUSCITA!");

                InputStream inStream = s.getInputStream();
                Scanner in = new Scanner(inStream);

                ObjectOutputStream outStream = new ObjectOutputStream(s.getOutputStream());

                System.out.println("Sto per ricevere dati dal socket server!");

                String line = in.nextLine();
                System.out.println(line);

                boolean done = false;
                outStream.writeObject(new Date());

                line = in.nextLine();
                System.out.println(line);
            } finally {
                s.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ClientRequestResult.SUCCESS;
    }

/*
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAccount.getAddress(), emailAccount.getPassword());
            }
        };

        try {
            Session session = Session.getInstance(emailAccount.getProperties(), authenticator);
            emailAccount.setSession(session);
            Store store = session.getStore("imaps");
            store.connect(emailAccount.getProperties().getProperty("incomingHost"),
                    emailAccount.getAddress(),
                    emailAccount.getPassword());
            emailAccount.setStore(store);
            emailManager.addEmailAccount(emailAccount);
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            return EmailLoginResult.FAILED_BY_NETWORK;
        } catch (AuthenticationFailedException e) {
            e.printStackTrace();
            return  EmailLoginResult.FAILED_BY_CREDENTIALS;
        } catch (MessagingException e) {
            e.printStackTrace();
            return EmailLoginResult.FAILED_BY_UNEXPECTED_ERROR;
        } catch (Exception e) {
            e.printStackTrace();
            return  EmailLoginResult.FAILED_BY_UNEXPECTED_ERROR;
        }
        return EmailLoginResult.SUCCESS;



 */



    @Override
    protected Task<ClientRequestResult> createTask() {
        return new Task<ClientRequestResult>() {
            @Override
            protected ClientRequestResult call() throws Exception {
                return login();
            }
        };
    }

}
