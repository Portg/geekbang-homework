package org.geektimes.event.distributed.jms.subscriber;

import org.geektimes.event.distributed.jms.AbstractJmsEvent;
import org.geektimes.event.distributed.jms.TextJmsEvent;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

public class MessageEventResolver {

    public AbstractJmsEvent resolveMessage(Message message) throws JMSException {

        if (isTextMessage(message))
            return resolveToTextEvent((TextMessage) message);

        throw new UnsupportedOperationException("no message type supported");
    }

    protected TextJmsEvent resolveToTextEvent(TextMessage message) throws JMSException {
        return new TextJmsEvent(message.getText());
    }

    private boolean isTextMessage(Message message) throws JMSException {
        return TextMessage.class.getName().equals(message.getStringProperty(AbstractJmsEvent.MESSAGE_TYPE_PROPERTY));
    }

}
