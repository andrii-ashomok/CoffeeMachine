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

    private static final int SLEEP_TIME = 500; // ms, wait for pay coffee process will be done

    private ExecutorService paymentExecutor;

    public void setPaymentExecutor(ExecutorService paymentExecutor) {
        this.paymentExecutor = paymentExecutor;
    }

    @Override
    public Client pay(final int index) {

        Future<Client> clientFuture = paymentExecutor.submit(new Payment(index));
        Client modifyClient = null;
        try {
            while (!clientFuture.isDone()) {
                log.info("Wait {} ms until Client-{} will pay for coffee", SLEEP_TIME, index);
                TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);
            }

            modifyClient = clientFuture.get();
            log.info("Client-{} payed for coffee, {}", index, modifyClient);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Payment process interrupted for Client-{}, {}",
                    index, e.getMessage(), e);
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
                client.setSpendTimeToPay(limitTime);
            } else
                client.setSpendTimeToPay(interval);

            log.info("Process of pay for drink completed. {} ", client);

            return client;
        }
    }

    @Override
    public void shutdown() {
        paymentExecutor.shutdown();
    }
}
