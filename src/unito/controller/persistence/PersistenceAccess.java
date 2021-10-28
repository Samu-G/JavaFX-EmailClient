package unito.controller.persistence;

import unito.model.ValidAccount;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe non generica usata per leggere e scrivere sul file di persistenza gli account salvati nel client al momento dell'appertura / chiusura della applicazione
 */
public class PersistenceAccess {

    private static String VALID_ACCOUNTS_LOCATION = ".validAccounts.ser";
    private static Encoder encoder = new Encoder();

    /**
     * Legge il file di persistenza salvato nella locazione indicata da VALID_ACCOUNT_LOCATION e restituisce la lista degli account letti dal file
     */
    public static List<ValidAccount> loadFromPersistenceValidAccount() {

        List<ValidAccount> resultList = new ArrayList<>();

        /* * Account salvati nel file di persistenza
        /*
        resultList.add(new ValidAccount("user1@email.com", "user1"));
        resultList.add(new ValidAccount("user2@email.com", "user2"));
        resultList.add(new ValidAccount("user3@email.com", "user3"));
        */

        /* Carico dal File di persistenza gli account del client */
        try {
            FileInputStream fileInputStream = new FileInputStream(VALID_ACCOUNTS_LOCATION);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            resultList = (List<ValidAccount>) objectInputStream.readObject();

            /* Decripto le password di ogni account della lista */
            decodePasswords(resultList);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }

    /**
     * Prende una lista di "ValidAccount" e ne decripta le password
     *
     * @param persistedList la lista con le password da decriptare
     */
    private static void decodePasswords(List<ValidAccount> persistedList) {
        for (ValidAccount validAccount : persistedList) {
            String originalPassword = validAccount.getPassword();
            validAccount.setPassword(encoder.decode(originalPassword));
        }
    }

    /**
     * Prende una lista di "ValidAccount" e ne cripta le password
     *
     * @param persistedList la lista con le password da criptare
     */
    private static void encodePasswords(List<ValidAccount> persistedList) {
        for (ValidAccount validAccount : persistedList) {
            String originalPassword = validAccount.getPassword();
            validAccount.setPassword(encoder.encode(originalPassword));
        }
    }


    /**
     * Salva nel file di persistenza gli account salvati nel client al momento della chiusura
     *
     * @param validAccounts la lista di account da salvare nel file di pesistenza
     */
    public static void saveToPersistence(List<ValidAccount> validAccounts) {
        try {
            File file = new File(VALID_ACCOUNTS_LOCATION);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            encodePasswords(validAccounts);
            objectOutputStream.writeObject(validAccounts);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
