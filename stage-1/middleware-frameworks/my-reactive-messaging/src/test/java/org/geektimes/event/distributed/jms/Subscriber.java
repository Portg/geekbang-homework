package org.geektimes.event.distributed.jms;


import org.geektimes.event.distributed.jms.subscriber.activemq.ActiveMQEventSubscriber;

public class Subscriber {

    public static void main(String[] args) {
        new Thread(new ActiveMQEventSubscriber()).start();
    }

}
