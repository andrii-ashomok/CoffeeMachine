package com.machine.coffee.order;

import com.machine.coffee.client.Client;
import com.machine.coffee.watcher.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.*;

/**
 * Created by rado on 06.02.2016.
 */
public class OrderServiceImpl implements OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Value("${time.find_cup}")
    private long timeFindCup;

    @Value("${time.put_cup}")
    private long timePutCup;

    @Value("${time.pick_type}")
    private long timePickType;

    @Value("${time.leave}")
    private long timeLeave;

    private ExecutorService orderExecutor;

    public void setOrderExecutor(ExecutorService orderExecutor) {
        this.orderExecutor = orderExecutor;
    }


    @Override
    public synchronized Client takeOrder(final int index) {
        Watcher watcher = new Watcher();
        watcher.start();

        Future<Client> clientFuture = orderExecutor.submit(new Order(index));

        Client modifyClient = null;
        try {

            modifyClient = clientFuture.get();

        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
        }

        watcher.stop();
        log.info("Order process duration {} ms", watcher.getInterval());

        return modifyClient;
    }

    public void findACup() {
        try {
            TimeUnit.MILLISECONDS.sleep(timeFindCup);
        } catch (InterruptedException e) {
            log.error("Interrupted during operation 'Find a cup'. {}", e.getMessage(), e);
        }
    }


    public void putCup() {
        try {
            TimeUnit.MILLISECONDS.sleep(timePutCup);
        } catch (InterruptedException e) {
            log.error("Interrupted during operation 'Put a cup'. {}", e.getMessage(), e);
        }
    }


    public void pickType() {
        try {
            TimeUnit.MILLISECONDS.sleep(timePickType);
        } catch (InterruptedException e) {
            log.error("Interrupted during operation 'Pick a type'. {}", e.getMessage(), e);
        }
    }

    @Override
    public void fillDrink(long time) {
        try {
            TimeUnit.MILLISECONDS.sleep(time);
        } catch (InterruptedException e) {
            log.error("Interrupted during operation 'Wait to fill drink'. {}", e.getMessage(), e);
        }
    }

    public void leave() {
        try {
            TimeUnit.MILLISECONDS.sleep(timeLeave);
        } catch (InterruptedException e) {
            log.error("Interrupted during operation 'Leave'. {}", e.getMessage(), e);
        }
    }

    private final class Order implements Callable<Client> {

        private Client client;

        private Order(int index) {
            this.client = new Client(index);
        }

        @Override
        public Client call() throws Exception {
            log.info("Order process is started for Client-{}", client.getIndex());

            Watcher watcher = new Watcher();
            watcher.start();

            long fillTime = client.getCoffee().getCockTime();

            findACup();
            putCup();
            pickType();
            fillDrink(fillTime);
            leave();

            watcher.stop();

            long interval = watcher.getInterval();
            log.debug("Client-{}. Operation 'Order' duration {} ms",
                    client.getIndex(), interval);


            client.setSpendTimeToTakeOrder(interval);

            log.info("Order process completed. {}", client);

            return client;
        }
    }

}
