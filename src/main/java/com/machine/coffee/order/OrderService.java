package com.machine.coffee.order;

import com.machine.coffee.client.Client;
import com.machine.coffee.product.Coffee;

/**
 * Created by rado on 06.02.2016.
 */
public interface OrderService {

    Client takeOrder(int index, Coffee coffee);

    long findACup(int index);

    long putCup(int index);

    long pickType(int index);

    long fillDrink(int index, Coffee coffee);

    long leave(int index);

    void shutdown();
}
