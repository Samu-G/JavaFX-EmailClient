package unito.controller.persistence;

import javafx.collections.ObservableList;
import unito.model.EmailAccount;
import unito.model.ValidAccount;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe non generica usata per leggere e scrivere sul file di persistenza gli account salvati nel client al momento dell'apertura / chiusura della applicazione
 */
public class PersistenceAccess {

    private static final String VALID_ACCOUNTS_LOCATION = "src/unito/controller/persistence/validAccounts.ser";
    private static final Encoder encoder = new Encoder();

    /**
     * Legge il file di persistenza salvato nella locazione indicata da VALID_ACCOUNT_LOCATION e restituisce la lista degli account letti dal file
     *
     * @return la lista di ValidAccount salvati sul file di persitenza
     */
    public static List<ValidAccount> loadFromPersistenceValidAccount() {

        List<ValidAccount> resultList = new ArrayList<>();

        try {
            FileInputStream fileInputStream = new FileInputStream(VALID_ACCOUNTS_LOCATION);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            Object o = objectInputStream.readObject();

            if(o instanceof List) {
                if(!((List<?>) o).isEmpty()) {
                    if(((List<?>) o).get(0) instanceof ValidAccount) {
                        resultList = (List<ValidAccount>)o;
                    }
                }
            }

            decodePasswords(resultList);

        } catch (FileNotFoundException e) {
            /* Account salvati nel file di persistenza */
            System.out.println("File NOT FOUND! Loading demo...");
            resultList.add(new ValidAccount("user1@email.com", "user1"));
            resultList.add(new ValidAccount("user2@email.com", "user2"));
            resultList.add(new ValidAccount("user3@email.com", "user3"));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    private static void decodePasswords(List<ValidAccount> persistedList) {
        for (ValidAccount validAccount : persistedList) {
            String originalPassword = validAccount.getPassword();
            validAccount.setPassword(encoder.decode(originalPassword));
        }
    }

    private static void encodePasswords(List<ValidAccount> persistedList) {
        for (ValidAccount validAccount : persistedList) {
            String originalPassword = validAccount.getPassword();
            validAccount.setPassword(encoder.encode(originalPassword));
        }
    }

    /**
     * Salva nel file di persistenza gli account salvati nel client al momento della chiusura
     *
     * @param validAccounts la lista osservabile di account da salvare nel file di persistenza
     */
    public static void saveToPersistence(ObservableList<EmailAccount> validAccounts) {
        List<ValidAccount> validAccountList = new ArrayList<ValidAccount>();
        for (EmailAccount emailAccount : validAccounts) {
            validAccountList.add(new ValidAccount(emailAccount.getAddress(), emailAccount.getPassword()
            ));
        }
        try {
            File file = new File(VALID_ACCOUNTS_LOCATION);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            encodePasswords(validAccountList);
            objectOutputStream.writeObject(validAccountList);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
