package com.machine.coffee.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by rado on 06.02.2016.
 */
public abstract class AbstractClientPool implements ClientPool {
    private static final Logger log = LoggerFactory.getLogger(AbstractClientPool.class);

    private Set<Integer> idleTransport;

    public AbstractClientPool() {
        this.idleTransport = new HashSet<>();
    }

    public boolean isActiveClient(long timeFullOrder) {
        synchronized (idleTransport) {

            if (idleTransport.isEmpty()) {
                try {
                    idleTransport.wait(timeFullOrder);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        return !idleTransport.isEmpty();
    }

    public void releaseClient(int index) {
        synchronized (idleTransport) {
            log.debug("Add {} thread in pool", index);

            idleTransport.add(index);
            idleTransport.notify();
        }
    }

    @Override
    public void removeClient(int index) {
        synchronized (idleTransport) {
            log.debug("Remove {} thread in pool", index);

            idleTransport.remove(index);
            idleTransport.notify();
        }
    }

    public Integer removeFreeClient(long timeOut) {
        Integer index = null;

        synchronized (idleTransport) {

            while (idleTransport.isEmpty()) {

                try {
                    idleTransport.wait(timeOut);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            if (!idleTransport.isEmpty()) {
                index = idleTransport.iterator().next();
                idleTransport.remove(index);
                log.debug("Remove {} thread from pool", index);
            } else {
                log.warn("Could not get any free Client during {}", timeOut);
            }

        }

        return index;
    }

}
