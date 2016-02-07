package com.machine.coffee.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

/**
 * Created by rado on 06.02.2016.
 */
@ContextConfiguration("/spring-context.xml")
@Test(suiteName = "client")
public class ClientServiceTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private ClientService clientService;

    @Test
    public void testProcessClient() {
        clientService.processClient(20);
    }

}
