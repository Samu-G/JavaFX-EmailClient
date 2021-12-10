package unito.controller.persistence;

import java.util.Base64;

/**
 * Classe generica usata come cifrario per codificare e decodificare
 */
public class Encoder {

    private static Base64.Encoder enc = Base64.getEncoder();
    private static Base64.Decoder dec = Base64.getDecoder();

    /**
     * Prende la password e la cripta
     *
     * @param text password da criptare
     * @return la password criptata
     */
    public String encode(String text) {
        try {
            return enc.encodeToString(text.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Prende la password e la decripta
     *
     * @param text password da decriptare
     * @return la password decriptata
     */
    public String decode(String text) {
        try {
            return new String(dec.decode(text.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
