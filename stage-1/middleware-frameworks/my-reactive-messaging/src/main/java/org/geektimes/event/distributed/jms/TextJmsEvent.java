package org.geektimes.event.distributed.jms;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * Event based on {@link TextMessage}
 *
 * @see TextMessage
 */
public final class TextJmsEvent extends AbstractJmsEvent<String> {


    public TextJmsEvent(String source) {
        super(source);
    }

    @Override
    public TextMessage createMessage(Session session) throws JMSException {
        return setBaseProperties(session.createTextMessage(getSource()));
    }

    @Override
    protected Class<?> getMessageClassType() {
        return TextMessage.class;
    }
}
