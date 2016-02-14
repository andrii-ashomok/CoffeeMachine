package com.machine.coffee.product;

import com.machine.coffee.client.Client;

/**
 * Created by rado on 06.02.2016.
 */
public interface ProductService {

    Coffee getProduct(String productName);

    Client chooseCoffee(int index);

    void shutdown();
}
