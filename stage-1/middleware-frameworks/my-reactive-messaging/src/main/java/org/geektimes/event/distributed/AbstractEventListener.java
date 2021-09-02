package org.geektimes.event.distributed;

import org.geektimes.event.EventListener;

import java.util.EventObject;

public abstract class AbstractEventListener implements EventListener {

    @Override
    public void onEvent(EventObject event ) {
        //send
        if ( event instanceof DistributedEventObject ) {
            // Event -> Pub/Sub
            onMessageEvent( event );
        }
    }

    public abstract void onMessageEvent( EventObject event );
}
