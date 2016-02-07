package com.machine.coffee.payment;

import com.machine.coffee.client.Client;
import com.machine.coffee.watcher.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.*;

/**
 * Created by rado on 07.02.2016.
 */
public class PaymentServiceImpl implements PaymentService {
    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Value("${time.pay.credit}")
    private long timePayCredit;

    @Value("${time.pay.cash}")
    private long timePayCash;

    private ExecutorService paymentExecutor;

    public void setPaymentExecutor(ExecutorService paymentExecutor) {
        this.paymentExecutor = paymentExecutor;
    }


    public Client pay(final int index) {

        Future<Client> clientFuture = paymentExecutor.submit(new Payment(index));
        Client modifyClient = null;
        try {
            /*while (!clientFuture.isDone())
                Thread.currentThread().join();*/

            modifyClient = clientFuture.get();

        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
        }

        return modifyClient;
    }

    private final class Payment implements Callable<Client> {

        private Client client;

        private Payment(int index) {
            this.client = new Client(index);
        }

        @Override
        public Client call() throws Exception {
            Watcher watcher = new Watcher();
            watcher.start();

            boolean isCash = ThreadLocalRandom.current().nextBoolean();
            log.info("Client-{} start to pay using {}",
                    client.getIndex(), isCash ? "cash" : "credit");

            watcher.stop();
            long interval = watcher.getInterval();

            long limitTime = isCash ? timePayCash : timePayCredit;
            if (interval < limitTime) {

                try {
                    log.debug("Client index {}. Operation 'Pay' go to sleep: {} ms",
                            client.getIndex(), limitTime - interval);
                    Thread.currentThread().join(limitTime - interval);
                } catch (InterruptedException e) {
                    log.error("Interrupted during operation 'payment'. {}", e.getMessage(), e);
                }

                client.setSpendTimeToPay(limitTime);
            } else
                client.setSpendTimeToPay(interval);

            log.info("Process of pay for drink completed. {} ", client);

            return client;
        }
    }

}
