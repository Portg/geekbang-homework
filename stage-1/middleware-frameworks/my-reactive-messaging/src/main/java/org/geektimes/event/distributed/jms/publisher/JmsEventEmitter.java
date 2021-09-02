package org.geektimes.event.distributed.jms.publisher;

import org.geektimes.commons.event.ConditionalEventListener;
import org.geektimes.event.distributed.jms.AbstractJmsEvent;
import org.geektimes.event.distributed.jms.LocalSessionProvider;
import org.geektimes.event.distributed.jms.publisher.activemq.ActiveMQTextEventEmitter;

import javax.jms.*;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;


/**
 * generic jms event emitter
 *
 * @see ConditionalEventListener
 * @see ActiveMQTextEventEmitter
 */
public abstract class JmsEventEmitter<E extends AbstractJmsEvent<?>> extends LocalSessionProvider implements ConditionalEventListener<E> {

    protected final Properties properties = loadProperties();

    protected Properties loadProperties() {
        Properties properties = new Properties();
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/jms.properties"));
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
        return properties;
    }

    @Override
    public boolean accept(E event) {
        return true;
    }

    @Override
    public void onEvent(E event) {
        try {
            Session session = getSession(this.properties);
            MessageProducer producer = session.createProducer(getDestination());
            Message message = event.createMessage(session);
            producer.send(message);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract Set<org.geektimes.event.distributed.jms.Destination> getSupportedDestinations();

    protected Destination getDestination() {
        return (Topic) () -> AbstractJmsEvent.topic;
    }

}
