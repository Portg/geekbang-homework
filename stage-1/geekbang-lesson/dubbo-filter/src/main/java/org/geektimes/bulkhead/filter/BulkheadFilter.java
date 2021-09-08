package org.geektimes.bulkhead.filter;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.vavr.control.Try;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import java.util.function.Supplier;

import static org.apache.dubbo.common.constants.CommonConstants.CONSUMER;
import static org.apache.dubbo.common.constants.CommonConstants.PROVIDER;

@Activate(group = {CONSUMER, PROVIDER}, value = "bulkhead", order = 10000)
public class BulkheadFilter implements Filter {

    private final BulkheadConfig config;

    private final BulkheadRegistry bulkheadRegistry;

    public BulkheadFilter(BulkheadConfig config) {
        this.config = config;
        // Create a BulkheadRegistry with a custom global configuration
        this.bulkheadRegistry = BulkheadRegistry.of(config);
    }

    /**
     * Perform the validation of before invoking the actual method based on <b>validation</b> attribute value.
     * @param invoker    service
     * @param invocation invocation.
     * @return Method invocation result
     * @throws RpcException Throws RpcException if  validation failed or any other runtime exception occurred.
     */
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        Bulkhead bulkhead = bulkheadRegistry
                .bulkhead(String.format("%s:%s", invocation.getServiceName(), invocation.getMethodName()));

        Supplier<Result> decoratedSupplier = Bulkhead
                .decorateSupplier(bulkhead, () -> invoker.invoke(invocation) );

        Try<Result> result = Try.ofSupplier(decoratedSupplier);

        if (result.isEmpty())
            throw new RpcException(result.getCause());

        return result.get();
    }
}
