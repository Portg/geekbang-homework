package org.geektimes.event.distributed.zk.pubsub;

import org.apache.curator.x.async.modeled.JacksonModelSerializer;
import org.apache.curator.x.async.modeled.ModelSpec;
import org.apache.curator.x.async.modeled.ModelSpecBuilder;
import org.apache.curator.x.async.modeled.ModeledFramework;
import org.apache.curator.x.async.modeled.typed.TypedModeledFramework;
import org.apache.curator.x.async.modeled.typed.TypedModeledFramework2;
import org.apache.zookeeper.CreateMode;
import org.geektimes.event.distributed.zk.pubsub.messages.LocationAvailable;
import org.geektimes.event.distributed.zk.pubsub.messages.UserCreated;
import org.geektimes.event.distributed.zk.pubsub.model.Group;
import org.geektimes.event.distributed.zk.pubsub.model.Instance;
import org.geektimes.event.distributed.zk.pubsub.model.InstanceType;
import org.geektimes.event.distributed.zk.pubsub.model.Priority;

import java.util.concurrent.TimeUnit;

public class Clients {
    /**
     * A client template for LocationAvailable instances
     */
    public static final TypedModeledFramework2<LocationAvailable, Group, Priority> locationAvailableClient = TypedModeledFramework2
            .from(ModeledFramework.builder(), builder(LocationAvailable.class),
                    "/root/pubsub/messages/locations/{group}/{priority}/{id}");

    /**
     * A client template for UserCreated instances
     */
    public static final TypedModeledFramework2<UserCreated, Group, Priority> userCreatedClient = TypedModeledFramework2
            .from(ModeledFramework.builder(), builder(UserCreated.class),
                    "/root/pubsub/messages/users/{group}/{priority}/{id}");

    /**
     * A client template for Instance instances
     */
    public static final TypedModeledFramework<Instance, InstanceType> instanceClient = TypedModeledFramework
            .from(ModeledFramework.builder(), builder(Instance.class), "/root/pubsub/instances/{instance-type}/{id}");

    private static <T> ModelSpecBuilder<T> builder(Class<T> clazz) {
        return ModelSpec.builder(JacksonModelSerializer.build(clazz))
                .withTtl(TimeUnit.MINUTES.toMillis(10)) // for our pub-sub example, messages are valid for 10 minutes
                .withCreateMode(CreateMode.PERSISTENT_WITH_TTL);
    }

    private Clients() {
    }
}
