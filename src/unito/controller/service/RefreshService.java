package unito.controller.service;

import unito.EmailManager;
import unito.view.ViewFactory;
import java.util.concurrent.FutureTask;

/**
 * Classe Runnable volta a fare il refresh delle Email in modo automatico
 */
public class RefreshService implements Runnable {

    public long refreshRateInMs;
    private final EmailManager emailManager;
    private boolean loop;
    private boolean serverOnline;

    /**
     * @param emailManager
     * @param refreshRate refresh rate
     * @param loop boolean to cycle
     */
    public RefreshService(EmailManager emailManager, long refreshRate, boolean loop) {
        super();
        this.emailManager = emailManager;
        this.refreshRateInMs = refreshRate;
        this.loop = loop;
    }

    /**
     * Crea un ClientService per gestire la richiesta (RICEVIMESSAGGIO), usa un FutureTask per ottenere il risultato della richiesta
     */
    @Override
    public void run() {
        do {
            try {
                Thread.sleep(refreshRateInMs);

                ClientService clientService = new ClientService(emailManager, ClientRequestType.RICEVIMESSAGGIO, null);

                FutureTask<ClientRequestResult> refreshService = new FutureTask<>(clientService);

                Thread thread = new Thread(refreshService);

                thread.start();

                try {
                    ClientRequestResult r = refreshService.get();
                    switch (r) {
                        case SUCCESS -> {
                            if (!serverOnline && loop) {
                                ViewFactory.viewAlert("Avviso", "Connessione ristabilita con il server.");
                            }
                            serverOnline = true;
                            System.out.println("Refresh Done!");
                            emailManager.getViewFactory().writeOnLogLabel("Connessione con il server stabilita. Autenticato come " + emailManager.getCurrentAccount().getAddress());
                        }

                        case FAILED_BY_CREDENTIALS -> {
                            emailManager.getViewFactory().writeOnLogLabel("ERRORE: Refresh automatico fallito a causa di credenziali errate. Autenticato come " + emailManager.getCurrentAccount().getAddress());
                        }

                        case FAILED_BY_SERVER_DOWN -> {
                            emailManager.getViewFactory().writeOnLogLabel("ERRORE: Refresh automatico fallito a causa del server che è spento. Autenticato come " + emailManager.getCurrentAccount().getAddress());

                            if (serverOnline) {
                                ViewFactory.viewAlert("Errore", "Connessione interrotta con il server. Tentativo di riconnessione in corso...");
                            }

                            serverOnline = false;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (loop);
    }

    /**
     * @param rate velocità del refresh
     */
    public void setRefreshRate(long rate) {
        this.refreshRateInMs = rate;
    }

    /**
     * @param b true per continuare il refresh automatico, false per interromperlo
     */
    public void setLoop(boolean b) {
        this.loop = b;
    }
}
