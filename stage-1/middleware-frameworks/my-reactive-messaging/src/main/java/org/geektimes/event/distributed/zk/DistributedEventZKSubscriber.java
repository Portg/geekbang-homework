package org.geektimes.event.distributed.zk;

import org.apache.curator.x.async.AsyncCuratorFramework;
import org.apache.curator.x.async.modeled.cached.CachedModeledFramework;
import org.apache.curator.x.async.modeled.typed.TypedModeledFramework2;
import org.geektimes.event.distributed.zk.pubsub.Clients;
import org.geektimes.event.distributed.zk.pubsub.messages.LocationAvailable;
import org.geektimes.event.distributed.zk.pubsub.messages.UserCreated;
import org.geektimes.event.distributed.zk.pubsub.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class DistributedEventZKSubscriber {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final AsyncCuratorFramework client;

    public DistributedEventZKSubscriber(AsyncCuratorFramework client) {
        this.client = Objects.requireNonNull(client, "client cannot be null");
    }

    /**
     * Start a subscriber (a CachedModeledFramework instance) using the LocationAvailable client template
     *
     * @param group group to listen for
     * @param priority priority to listen for
     * @return CachedModeledFramework instance (already started)
     */
    public CachedModeledFramework<LocationAvailable> startLocationAvailableSubscriber(Group group, Priority priority) {
        return startSubscriber(Clients.locationAvailableClient, group, priority);
    }

    /**
     * Start a subscriber (a CachedModeledFramework instance) using the UserCreated client template
     *
     * @param group group to listen for
     * @param priority priority to listen for
     * @return CachedModeledFramework instance (already started)
     */
    public CachedModeledFramework<UserCreated> startUserCreatedSubscriber(Group group, Priority priority) {
        return startSubscriber(Clients.userCreatedClient, group, priority);
    }

    /**
     * Start a subscriber (a CachedModeledFramework instance) using the Instance client template
     *
     * @param instanceType type to listen for
     * @return CachedModeledFramework instance (already started)
     */
    public CachedModeledFramework<Instance> startInstanceSubscriber(InstanceType instanceType) {
        CachedModeledFramework<Instance> resolved = Clients.instanceClient.resolved(client, instanceType).cached();
        resolved.start();
        return resolved;
    }

    private <T extends Message> CachedModeledFramework<T> startSubscriber(
            TypedModeledFramework2<T, Group, Priority> typedClient, Group group, Priority priority) {
        CachedModeledFramework<T> resolved = typedClient.resolved(client, group, priority).cached();
        resolved.start();
        return resolved;
    }
}
