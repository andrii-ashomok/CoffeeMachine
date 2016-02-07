package com.machine.coffee.client;

/**
 * Created by rado on 06.02.2016.
 */
public interface ClientPool {

    boolean isActiveClient(long timeFullOrder);

    void releaseClient(int index);

    void removeClient(int index);

    Integer removeFreeClient(long timeOut);

    boolean startClientPool();

    void stopClientPool();

    Client acquireClient();
}
