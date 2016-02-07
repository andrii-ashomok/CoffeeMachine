package com.machine.coffee.client;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.machine.coffee.watcher.TimeConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * Created by rado on 06.02.2016.
 */
public class ClientPoolImpl extends AbstractClientPool {
    private static final Logger log = LoggerFactory.getLogger(ClientPoolImpl.class);

    private Client[] clients;
    private long timeFullOrder;
    private int maxBuyPerson;
    private boolean isStarted;

    final BlockingQueue<Runnable> paymentQueue;
    private ExecutorService paymentExecutor;

    final BlockingQueue<Runnable> resultQueue;
    private ExecutorService resultExecutor;

    public ClientPoolImpl(List<String> drinkList, TimeConstant timeConstant,
                          int maxBuyPerson, int maxPayPerson, int maxGetPerson) {
        super();

        timeFullOrder = timeConstant.getTimeFullOrder();
        this.maxBuyPerson = maxBuyPerson;

        clients = new Client[maxBuyPerson];

        paymentQueue = new ArrayBlockingQueue(maxBuyPerson);
        ThreadFactory paymentThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("payment-").build();
        paymentExecutor = new ThreadPoolExecutor(maxPayPerson, maxPayPerson,
                timeFullOrder, TimeUnit.MILLISECONDS, paymentQueue,
                paymentThreadFactory);


        resultQueue = new ArrayBlockingQueue(maxPayPerson);
        ThreadFactory resultThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("result-").build();
        resultExecutor = new ThreadPoolExecutor(maxGetPerson, maxGetPerson,
                timeFullOrder, TimeUnit.MILLISECONDS, resultQueue,
                resultThreadFactory);

        for (int i = 0; i < maxBuyPerson; i++) {
            /*clients[i] = new Client(i, drinkList, timeConstant.getTimePickFavorite(),
                    timeConstant.getTimePayCredit(), timeConstant.getTimePayCash(),
                    timeConstant.getTimeFindCup(), timeConstant.getTimePutCup(),
                    timeConstant.getTimePickType(), timeConstant.getTimeLeave(),
                    paymentExecutor);*/

            releaseClient(i);
        }



    }

    public boolean startClientPool() {
        for (Client client : clients) {
//            client.startOrder();
        }

        isStarted = isActiveClient(timeFullOrder);

        return isStarted;
    }

    public void stopClientPool() {
        for (int i = 0; i < maxBuyPerson; i++) {
            removeClient(i);
        }
    }

    public Client acquireClient() {
        Client client = null;

        if (isActiveClient(timeFullOrder)) {
            Integer index = removeFreeClient(timeFullOrder);
            client = Objects.isNull(index) ? null : clients[index];
        }

        log.debug("Get next");

        return client;
    }

}
