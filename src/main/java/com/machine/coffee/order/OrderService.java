package com.machine.coffee.order;

import com.machine.coffee.client.Client;

/**
 * Created by rado on 06.02.2016.
 */
public interface OrderService {

    Client takeOrder(int index);

    void findACup();

    void putCup();

    void pickType();

    void fillDrink(long time);

    void leave();
}
