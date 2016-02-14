package com.machine.coffee;

import com.machine.coffee.client.Client;
import com.machine.coffee.client.ClientService;
import com.machine.coffee.statistics.Statistic;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class CoffeeMachineApplication {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");

		ClientService clientService = (ClientService) context.getBean("ClientService");
		List<Client> clientList = clientService.processClient(100);

		Statistic statistic = (Statistic) context.getBean("Statistic");
		statistic.getStatistics(clientList);

		clientService.shutdown();
	}
}
