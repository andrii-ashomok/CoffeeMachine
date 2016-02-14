package com.machine.coffee.statistics;

import com.machine.coffee.client.Client;
import com.machine.coffee.product.Coffee;
import com.machine.coffee.product.CoffeeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.pkcs11.wrapper.Functions;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by rado on 2/14/16.
 */
public class StatisticImpl implements Statistic {
    private static final Logger log = LoggerFactory.getLogger(StatisticImpl.class);

    @Override
    public void getStatistics(List<Client> clients) {
        if (Objects.isNull(clients) && clients.isEmpty())
            return;

        log.info("Total amount of sold coffee {}", getTotalAmountCoffeeSold(clients));
        log.info("Total amount of sold coffee by type: {}", getTotalAmountCoffeeSoldByType(clients));
        log.info("Total amount each CoffeeMachine: {}", dispensedByEachCoffeeMachine(clients));

    }

    @Override
    public long getTotalAmountCoffeeSold(List<Client> clients) {
        return clients
                .stream()
                .filter(Objects::nonNull)
                .count();
    }

    @Override
    public Map<CoffeeType, Integer> getTotalAmountCoffeeSoldByType(List<Client> clients) {
        int countCap = 0;
        int countEsp = 0;
        int countLatte = 0;

        Map<CoffeeType, Integer> statMap = new HashMap<>();

        for (Client client : clients) {

            switch (client.getCoffee().getCoffeeType()) {
                case CAPPUCCINO:
                    countCap++;
                    break;

                case ESPRESSO:
                    countEsp++;
                    break;

                case LATTE:
                    countLatte++;
                    break;
            }

        }

        statMap.put(CoffeeType.CAPPUCCINO, countCap);
        statMap.put(CoffeeType.ESPRESSO, countEsp);
        statMap.put(CoffeeType.LATTE, countLatte);

        return statMap;
    }

    @Override
    public Map<String, Integer> dispensedByEachCoffeeMachine(List<Client> clients) {
        long numberMachine1 = clients.get(0).getCoffeeMachineIndex();

        Map<Boolean, List<Client>> clientMap = clients.stream()
                .collect(Collectors.partitioningBy(client -> client.getCoffeeMachineIndex() == numberMachine1));

        Map<String, Integer> resultMap = new HashMap<>();

        resultMap.put("CoffeeMachine_1", clientMap.get(true).size());
        resultMap.put("CoffeeMachine_2", clientMap.get(false).size());

        return resultMap;
    }

    @Override
    public long getAverageTime(List<Client> clients) {
        return 0;
    }

    @Override
    public long getFastestTime(List<Client> clients) {
        final Comparator<Client> comp = (c1, c2) -> Long.compare( c1.getSummaryTime(), c2.getSummaryTime());
        return clients.stream()
                .max(comp)
                .get()
                .getSummaryTime();
    }

    @Override
    public long getSlowestTime(List<Client> clients) {
        return clients.stream()
                .max((c1, c2) -> Long.compare( c1.getSummaryTime(), c2.getSummaryTime()))
                .get()
                .getSummaryTime();
    }
}
