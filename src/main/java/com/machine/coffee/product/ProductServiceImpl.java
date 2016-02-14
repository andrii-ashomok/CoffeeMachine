package com.machine.coffee.product;

import com.machine.coffee.client.Client;
import com.machine.coffee.watcher.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by rado on 06.02.2016.
 */
public class ProductServiceImpl implements ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Value("${coffee.espresso.name}")
    private String espresso;

    @Value("${coffee.latte.name}")
    private String latte;

    @Value("${coffee.cappuccino.name}")
    private String cappuccino;

    @Value("${price.espresso}")
    private int priceEspresso;

    @Value("${price.latte}")
    private int priceLatte;

    @Value("${price.cappuccino}")
    private int priceCappuccino;

    @Value("${cock.time.espresso}")
    private long cockTimeEspresso;

    @Value("${cock.time.latte}")
    private long cockTimeLatte;

    @Value("${cock.time.cappuccino}")
    private long cockTimeCappuccino;

    @Value("${time.pick.personal.favorite}")
    private long timePickFavorite;

    private static final int SLEEP_TIME = 500; // ms, wait for choose coffee process will be done
    private Map<String, Coffee> coffeeMap;
    private List<String> drinks;
    private ExecutorService chooseCoffeeExecutor;

    public void setChooseCoffeeExecutor(ExecutorService chooseCoffeeExecutor) {
        this.chooseCoffeeExecutor = chooseCoffeeExecutor;
    }

    @PostConstruct
    public void init() {
        coffeeMap = new HashMap<>();

        drinks = Arrays.asList(espresso, latte, cappuccino);

        coffeeMap.put(espresso, new Coffee(CoffeeType.ESPRESSO,
                priceEspresso, cockTimeEspresso));

        coffeeMap.put(latte, new Coffee(CoffeeType.LATTE,
                priceLatte, cockTimeLatte));

        coffeeMap.put(cappuccino, new Coffee(CoffeeType.CAPPUCCINO,
                priceCappuccino, cockTimeCappuccino));

        log.debug("Init coffee products: {}", coffeeMap);
    }


    @Override
    public Coffee getProduct(String productName) {
       return coffeeMap.getOrDefault(productName.toLowerCase(), new Coffee());
    }


    @Override
    public Client chooseCoffee(final int index) {
        Future<Client> clientFuture = chooseCoffeeExecutor.submit(new CoffeeMenu(index));

        Client modifyClient = null;
        try {
            while (!clientFuture.isDone()) {
                log.info("Wait {} ms until Client-{} will choose coffee", SLEEP_TIME, index);
                TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);
            }

            modifyClient = clientFuture.get();
            log.info("Coffee was chosen, {}", modifyClient);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Process of Coffee choose interrupted for Client-{}, {}",
                    index, e.getMessage(), e);
        }

        return modifyClient;

    }


    private final class CoffeeMenu implements Callable<Client> {

        private Client client;

        private CoffeeMenu(int index) {
            this.client = new Client(index);
        }

        @Override
        public Client call() throws Exception {
            Watcher watcher = new Watcher();
            watcher.start();

            int randomDrinkIndex = ThreadLocalRandom.current().nextInt(drinks.size());
            String drink = drinks.get(randomDrinkIndex);
            Coffee coffee = getProduct(drink);

            client.setCoffee(coffee);

            watcher.stop();
            long interval = watcher.getInterval();
            log.debug("Client index {}. Operation 'Choose drink' duration {} ms",
                    client.getIndex(), interval);

            if (interval < timePickFavorite) {
                client.setSpendTimeToChoose(timePickFavorite);
            } else {
                client.setSpendTimeToChoose(interval);
            }

            log.info("Process of choosing drink completed. {} ", client);

            return client;
        }
    }

    @Override
    public void shutdown() {
        chooseCoffeeExecutor.shutdown();
    }
}
