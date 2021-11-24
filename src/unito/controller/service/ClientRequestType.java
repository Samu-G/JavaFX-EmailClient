package unito.controller.service;

/**
 * Enum utilizzata dal Client per far capire al Server quale operazione vuole fare
 */
public enum ClientRequestType {
    HANDSHAKING,
    INVIOMESSAGGIO,
    RICEVIMESSAGGIO,
    CANCELLAMESSAGGIO;

}
