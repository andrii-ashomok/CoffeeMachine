package com.machine.coffee.order;

import com.machine.coffee.ClientGenerate;
import com.machine.coffee.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.stream.Stream;

/**
 * Created by rado on 07.02.2016.
 */
@ContextConfiguration("/spring-context.xml")
@Test(suiteName = "order")
public class OrderServiceTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private OrderService orderService;

    @Test
    public void testTakeOrder() {
        int size = 15;

        final Stream<Client> clientStream = ClientGenerate.generateForOrder(size);

        clientStream.forEach(o -> orderService.takeOrder(o.getIndex(), o.getCoffee()));


    }

}
