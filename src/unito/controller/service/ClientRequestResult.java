package unito.controller.service;

/**
 * Enum utilizzata come risultato di una richiesta mandata dal Client al Server
 */
public enum ClientRequestResult {
    FAILED_BY_CREDENTIALS,
    FAILED_BY_SERVER_DOWN,
    SUCCESS,
    ERROR;
}
