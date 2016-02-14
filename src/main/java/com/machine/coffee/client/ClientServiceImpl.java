package com.machine.coffee.client;

import com.machine.coffee.order.OrderService;
import com.machine.coffee.payment.PaymentService;
import com.machine.coffee.product.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by rado on 06.02.2016.
 */
public class ClientServiceImpl implements ClientService {
    private static final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderService orderService;

    private ExecutorService processExecutor;

    public void setProcessExecutor(ExecutorService processExecutor) {
        this.processExecutor = processExecutor;
    }

    @Override
    public List<Client> processClient(int count) {


        List<Client> clientList = new ArrayList<>();
        List<Future> futureClientList = new ArrayList<>();

        for (int i =0; i < count; i++) {

            Future<Client> clientFuture = processExecutor.submit(new Process(i));
            futureClientList.add(clientFuture);

        }

        log.info("{} processes are running.", futureClientList.size());

        while (!futureClientList.isEmpty()) {

            List<Future> doneFutureClientList = futureClientList
                    .stream()
                    .filter(Future::isDone)
                    .collect(Collectors.toList());

            if (doneFutureClientList.isEmpty()) {
                try {
                    log.info("Coffee select process not finished, sleep ...");
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    log.error("Interrupt while Coffee Selector is processing. {}", e.getMessage());
                }

                continue;
            }

            for (Future future : doneFutureClientList) {

                try {
                    Client client = (Client) future.get();

                    clientList.add(client);
                    futureClientList.remove(future);

                    log.info("Coffee Selector finished for {}", client);
                } catch (InterruptedException | ExecutionException e) {
                    log.error("Could not get information about some client, cause {}", e.getMessage(), e);
                }

            }

        }

        log.info("Full result {} size, {}", clientList.size(), clientList);

        return clientList;
    }

    @Override
    public void shutdown() {
        orderService.shutdown();
        paymentService.shutdown();
        productService.shutdown();
        processExecutor.shutdown();
    }


    private final class Process implements Callable<Client> {

        private int index;

        private Process(int index) {
            this.index = index;
        }

        @Override
        public Client call() throws Exception {

            Client modifyClient = productService.chooseCoffee(index);
            Client client = new Client(index);

            if (Objects.nonNull(modifyClient)) {
                client.setCoffee(modifyClient.getCoffee());
                client.setSpendTimeToChoose(modifyClient.getSpendTimeToChoose());
            }

            modifyClient = paymentService.pay(index);

            if (Objects.nonNull(modifyClient)) {
                client.setSpendTimeToPay(modifyClient.getSpendTimeToPay());
            }

            modifyClient = orderService.takeOrder(client.getIndex(), client.getCoffee());

            if (Objects.nonNull(modifyClient)) {
                client.setSpendTimeToTakeOrder(modifyClient.getSpendTimeToTakeOrder());
                client.setCoffeeMachineIndex(modifyClient.getCoffeeMachineIndex());
            }

            long totalTime = client.getSpendTimeToChoose();
            totalTime += client.getSpendTimeToPay();
            totalTime += client.getSpendTimeToTakeOrder();
            client.setSummaryTime(totalTime);

            return client;
        }
    }

}
