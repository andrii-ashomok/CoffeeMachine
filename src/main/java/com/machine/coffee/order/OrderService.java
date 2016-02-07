package com.machine.coffee.order;

import com.machine.coffee.client.Client;

/**
 * Created by rado on 06.02.2016.
 */
public interface OrderService {

    void takeOrder(Client client);

    void findACup();

    void putCup();

    void pickType();

    void fillDrink(long time);

    void leave();
}
