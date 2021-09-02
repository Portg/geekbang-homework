package org.geektimes.event;

import org.geektimes.commons.event.EventListener;
import org.geektimes.event.distributed.jms.TextJmsEvent;

public class MyListener implements EventListener<TextJmsEvent> {

    @Override
    public void onEvent(TextJmsEvent event) {
        System.out.println(event.getSource());
    }
}
