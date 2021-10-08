package unito.controller.persistence;

import unito.model.EmailAccount;

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
    public static List<ValidAccount> loadFromPersistence() {

        List<ValidAccount> resultList = new ArrayList<>();

        //TODO: Esempi di account, da elliminare
        for (int i = 0; i < 10; i++) {
            resultList.add(
                    new ValidAccount("prova@gmail.com" + i, "pinco" + i)
            );
        }

        /* Carico da File di persistenza gli account del client */
        try {
            /* Apro un FileInputStream e leggo il file .validAccounts.ser */
            FileInputStream fileInputStream = new FileInputStream(VALID_ACCOUNTS_LOCATION);
            /* Il file è composto da stringhe di testo che descrivono gli oggetti "ValidAccount" */
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            /* Leggo gli oggetti dallo stream con readObject() */
            List<ValidAccount> persistedList = (List<ValidAccount>) objectInputStream.readObject();
            /* Decripto le password di ogni account della lista */
            decodePasswords(persistedList);
            /* Aggiungo alla lista */
            resultList.addAll(persistedList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* Ritorno la lista di account salvati nel file di oggetti .valdAccounts.ser */
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
