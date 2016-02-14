package com.machine.coffee.statistics;

import com.machine.coffee.client.Client;
import com.machine.coffee.product.CoffeeType;

import java.util.List;
import java.util.Map;

/**
 * Created by rado on 2/14/16.
 */
public interface Statistic {

    void getStatistics(List<Client> clients);

    long getTotalAmountCoffeeSold(List<Client> clients);

    Map<CoffeeType, Integer> getTotalAmountCoffeeSoldByType(List<Client> clients);

    Map<String, Integer> dispensedByEachCoffeeMachine(List<Client> clients);

    long getAverageTime(List<Client> clients);

    long getFastestTime(List<Client> clients);

    long getSlowestTime(List<Client> clients);

}
