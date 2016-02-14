package com.machine.coffee.client;

import java.util.List;

/**
 * Created by rado on 06.02.2016.
 */
public interface ClientService {

    List<Client> processClient(int count);

    void shutdown();
}
