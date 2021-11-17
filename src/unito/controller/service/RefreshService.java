package unito.controller.service;

import javafx.stage.Stage;
import unito.EmailManager;
import unito.view.ViewFactory;

import java.util.concurrent.FutureTask;

public class RefreshService implements Runnable {

    public long refreshRateInMs;
    private final EmailManager emailManager;
    private boolean loop;

    public RefreshService(EmailManager emailManager, long refreshRate, boolean loop) {
        super();
        this.emailManager = emailManager;
        this.refreshRateInMs = refreshRate;
        this.loop = loop;
    }

    @Override
    public void run() {
        do {
            try {
                Thread.sleep(refreshRateInMs);

                ClientService clientService = new ClientService(emailManager, ClientRequestType.RICEVIMESSAGGIO, null);

                FutureTask<ClientRequestResult> refreshService = new FutureTask<>(clientService);

                Thread thread = new Thread(refreshService);

                System.out.println("Loop: " + loop);

                thread.start();

                try {
                    ClientRequestResult r = refreshService.get();
                    switch (r) {
                        case SUCCESS -> System.out.println("Refresh Done!");

                        case FAILED_BY_CREDENTIALS -> ViewFactory.writeOnLogLabel("ERRORE: Refresh automatico fallito a causa di credenziali errate. ");

                        case FAILED_BY_SERVER_DOWN -> ViewFactory.writeOnLogLabel("ERRORE: Refresh automatico fallito a causa del server che è spento. ");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (InterruptedException e) {

            }
            System.out.println("sono qui");
        } while (loop);
    }

    public void setRefreshRate(long rate) {
        this.refreshRateInMs = rate;
    }

    public void setLoop(boolean b) {
        this.loop = b;
    }
}
