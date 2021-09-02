package org.geektimes.event.distributed.jms;

import org.geektimes.event.distributed.jms.publisher.JmsEventPublisher;

public class Publisher{

    public static void main(String[] args) {
        JmsEventPublisher jmsEventPublisher = new JmsEventPublisher();
        jmsEventPublisher.publish(Destination.fromActiveMQ(), new TextJmsEvent("Hello"));
    }

}
