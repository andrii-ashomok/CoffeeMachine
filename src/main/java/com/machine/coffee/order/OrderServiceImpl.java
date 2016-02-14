package com.machine.coffee.order;

import com.machine.coffee.client.Client;
import com.machine.coffee.product.Coffee;
import com.machine.coffee.watcher.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.Timer;
import java.util.concurrent.*;
import java.util.stream.Stream;

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

    private static final int SLEEP_TIME = 500; // ms, wait for order coffee process will be done
    private ExecutorService orderExecutor;

    public void setOrderExecutor(ExecutorService orderExecutor) {
        this.orderExecutor = orderExecutor;
    }


    @Override
    public Client takeOrder(final int index, final Coffee coffee) {
        Watcher watcher = new Watcher();
        watcher.start();

        Future<Client> clientFuture = orderExecutor.submit(new Order(index, coffee));

        Client modifyClient = null;
        try {
            while (!clientFuture.isDone()) {
                log.info("Wait {} ms until Client-{} will get coffee", SLEEP_TIME, index);
                TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);
            }

            modifyClient = clientFuture.get();
            log.info("Client got coffee, {}", modifyClient);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Process of getting coffee interrupted for Client-{}, {}",
                    index, e.getMessage(), e);
        }

        watcher.stop();
        log.info("Order process {}, duration {} ms", modifyClient, watcher.getInterval());

        return modifyClient;
    }

    @Override
    public long findACup(int index) {
        log.info("Client-{} find a cup, duration {} ms", index, timeFindCup);
        return timeFindCup;
    }

    @Override
    public long putCup(int index) {
        log.info("Client-{} put a cup, duration {} ms", index, timePutCup);
        return timePutCup;
    }

    @Override
    public long pickType(int index) {
        log.info("Client-{} pick a type, duration {} ms", index, timePickType);
        return timePickType;
    }

    @Override
    public long fillDrink(int index, Coffee coffee) {
        log.info("Client-{} fill {}, duration {} ms",
                index, coffee.getCoffeeType().name(), coffee.getCockTime());
        return coffee.getCockTime();
    }

    public long leave(int index) {
        log.info("Client-{} left coffee machine, duration {} ms", index, timeLeave);
        return timeLeave;
    }

    private final class Order implements Callable<Client> {

        private int index;
        private Coffee coffee;

        private Order(int index, Coffee coffee) {
            this.index = index;
            this.coffee = coffee;
        }

        @Override
        public Client call() throws Exception {
            log.info("Order process is started for Client-{}", index);

            Watcher watcher = new Watcher();
            watcher.start();

            Client client = new Client(index);

            long durationTime = findACup(index);

            durationTime += putCup(index);
            durationTime += pickType(index);
            durationTime += fillDrink(index, coffee);
            durationTime += leave(index);

            watcher.stop();

            long interval = watcher.getInterval();

            if (interval < durationTime) {
                client.setSpendTimeToTakeOrder(durationTime);
            } else {
                client.setSpendTimeToTakeOrder(interval);
            }

            client.setCoffeeMachineIndex(Thread.currentThread().getId());
            log.info("Client-{} send {} ms near coffee machine {}",
                    index, client.getSpendTimeToTakeOrder(), Thread.currentThread().getId());

            return client;
        }
    }

    @Override
    public void shutdown() {
        orderExecutor.shutdown();
    }
}
