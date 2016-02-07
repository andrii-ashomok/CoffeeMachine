package com.machine.coffee.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

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

        for (int i = 1; i <= size; i++ ) {
            orderService.takeOrder(i);
        }
    }

}
