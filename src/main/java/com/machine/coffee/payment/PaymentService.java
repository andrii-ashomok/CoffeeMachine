package com.machine.coffee.payment;

import com.machine.coffee.client.Client;

/**
 * Created by rado on 07.02.2016.
 */
public interface PaymentService {

    Client pay(int index);

}
