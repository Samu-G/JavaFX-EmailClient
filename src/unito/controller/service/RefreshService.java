package unito.controller.service;

import javafx.stage.Stage;
import unito.EmailManager;
import unito.view.ViewFactory;

import java.util.concurrent.FutureTask;

public class RefreshService implements Runnable {

    public long refreshRateInMs = 2000;
    private final EmailManager emailManager;
    private static boolean loop = true;

    public RefreshService(EmailManager emailManager, long refreshRate, boolean loop) {
        super();
        this.emailManager = emailManager;
        this.refreshRateInMs = refreshRate;
        this.loop = loop;
    }

    @Override
    public void run() {
        for (; ; ) {
            try {
                Thread.sleep(refreshRateInMs);

                ClientService clientService = new ClientService(emailManager, ClientRequestType.RICEVIMESSAGGIO, null);

                FutureTask<ClientRequestResult> refreshService = new FutureTask<>(clientService);

                Thread thread = new Thread(refreshService);

                thread.start();

                try {
                    ClientRequestResult r = refreshService.get();

                    System.out.println("Prova");
                    switch (r) {
                        case SUCCESS -> System.out.println("Refresh Done!");

                        case FAILED_BY_CREDENTIALS -> ViewFactory.writeOnLogLabel("ERRORE: Refresh automatico fallito a causa di credenziali errate. ");

                        case FAILED_BY_SERVER_DOWN -> ViewFactory.writeOnLogLabel("ERRORE: Refresh automatico fallito a causa del server che è spento. ");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(!loop) break;
        }
    }

    public void setRefreshRate(long rate) {
        this.refreshRateInMs = rate;
    }
}
