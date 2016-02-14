package com.machine.coffee.statistics;

import com.machine.coffee.ClientGenerate;
import com.machine.coffee.client.Client;
import com.machine.coffee.client.ClientService;
import com.machine.coffee.product.CoffeeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by rado on 2/14/16.
 */
@ContextConfiguration("/spring-context.xml")
@Test
public class StatisticTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private Statistic statistic;

    @Autowired
    private ClientService clientService;

    @Test
    public void testGetTotalAmountCoffeeSold() {

        long totalAmountCoffeeSold  = statistic.getTotalAmountCoffeeSold(Collections.EMPTY_LIST);

        assert totalAmountCoffeeSold == 0;

        int size = 10;

        Stream<Client> clientStream = ClientGenerate.generateForOrder(size);

        List<Client> clientListWithNull = clientStream.collect(Collectors.toList());
        clientListWithNull.add(null);
        clientListWithNull.add(null);

        totalAmountCoffeeSold  = statistic.getTotalAmountCoffeeSold(clientListWithNull);

        assert totalAmountCoffeeSold == size;

        size = 100;

        List<Client> clientList = clientService.processClient(size);

        assert !clientList.isEmpty();

        totalAmountCoffeeSold = statistic.getTotalAmountCoffeeSold(clientList);
        assert totalAmountCoffeeSold == size;
    }

    @Test
    public void testGetTotalAmountCoffeeSoldByType() {
        Map<CoffeeType, Integer> statMap = statistic.getTotalAmountCoffeeSoldByType(Collections.EMPTY_LIST);
        assert !statMap.isEmpty()
                && statMap.containsKey(CoffeeType.CAPPUCCINO)
                && statMap.get(CoffeeType.CAPPUCCINO) == 0
                && statMap.containsKey(CoffeeType.LATTE)
                && statMap.get(CoffeeType.LATTE) == 0
                && statMap.containsKey(CoffeeType.ESPRESSO)
                && statMap.get(CoffeeType.ESPRESSO) == 0;


        int size = 100;
        List<Client> clientList = clientService.processClient(size);

        assert !clientList.isEmpty();

        statMap = statistic.getTotalAmountCoffeeSoldByType(clientList);

        assert !statMap.isEmpty()
                && statMap.containsKey(CoffeeType.CAPPUCCINO)
                && statMap.get(CoffeeType.CAPPUCCINO) > 0
                && statMap.containsKey(CoffeeType.LATTE)
                && statMap.get(CoffeeType.LATTE) > 0
                && statMap.containsKey(CoffeeType.ESPRESSO)
                && statMap.get(CoffeeType.ESPRESSO) > 0;
    }

    @Test
    public void testDispensedByEachCoffeeMachine() {
        int size = 100;
        List<Client> clientList = clientService.processClient(size);

        assert !clientList.isEmpty();


        Map<String, Integer> resultMap = statistic.dispensedByEachCoffeeMachine(clientList);

        assert !resultMap.isEmpty();
    }

    @Test
    public void testComparator() {
        int size = 100;
        List<Client> clientList = clientService.processClient(size);

        assert !clientList.isEmpty();

        long fastest = statistic.getFastestTime(clientList);
        long slowest = statistic.getSlowestTime(clientList);

        assert fastest > 0 && slowest > 0;
        assert fastest >= slowest;
    }

}
