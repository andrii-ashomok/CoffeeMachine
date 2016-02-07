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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

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

    public void processClient(int count) {

        Future<Client> clientFuture;
        List<Client> clientList = new ArrayList<>();

        for (int i =0; i < count; i++) {

            clientFuture = processExecutor.submit(new Process(i));

            try {
                Client client = clientFuture.get();
                clientList.add(client);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }

        log.info("Result: {}", clientList);

    }


    private final class Process implements Callable<Client> {

        private Client client;

        private Process(int index) {
            this.client = new Client(index);
        }

        @Override
        public Client call() throws Exception {

            Client modifyClient = productService.chooseCoffee(client.getIndex());

            if (Objects.nonNull(modifyClient)) {
                client.setCoffee(modifyClient.getCoffee());
                client.setSpendTimeToChoose(modifyClient.getSpendTimeToChoose());
            }

            modifyClient = paymentService.pay(client.getIndex());

            if (Objects.nonNull(modifyClient)) {
                client.setSpendTimeToPay(modifyClient.getSpendTimeToPay());
            }

            modifyClient = orderService.takeOrder(client.getIndex());

            if (Objects.nonNull(modifyClient)) {
                client.setSpendTimeToTakeOrder(modifyClient.getSpendTimeToTakeOrder());
            }

            return client;
        }
    }

}
