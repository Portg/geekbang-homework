# geekbang-homework



## 7月17日课后作业（第3周）

- 通过 MicroProfile REST Client 实现 POST 接⼝去请求项⽬中的 ShutdownEndpoint，URI：[ http://127.0.0.1:8080/actuator/shutdown](http://127.0.0.1:8080/actuator/shutdown)

  1、定义服务接口`ShutdownEndpointService.java`

  ```java
  package com.salesmanager.shop.rest.service;
  
  import javax.ws.rs.POST;
  import javax.ws.rs.Path;
  
  /**
   *
   * ShutdownEndpoint Service
   *
   */
  @Path("/actuator")
  public interface ShutdownEndpointService {
  	@POST
  	@Path("/shutdown")
  	String shutdown();
  }
  ```

  2、定义服务接口`ShutdownEndpointServiced`的测试类`ShutdownEndpointServiceTest.java`

  ```java
  package com.salesmanager.test.shop.rest.service;
  
  import com.salesmanager.shop.rest.service.ShutdownEndpointService;
  import org.eclipse.microprofile.rest.client.RestClientBuilder;
  import org.junit.Test;
  
  import java.net.URI;
  import java.net.URISyntaxException;
  
  /**
   * {@link ShutdownEndpointService} Test
   *
   * @author
   * @since 1.0.0
   */
  public class ShutdownEndpointServiceTest {
  
  	/**
  	 * Test {@link ShutdownEndpointService#shutdown()}
  	 */
  	@Test
  	public void testShutdown() throws URISyntaxException {
  		URI uri = new URI("http://127.0.0.1:8080");
  		ShutdownEndpointService shutdownEndpointService = RestClientBuilder.newBuilder()
  				.baseUri(uri)
  				.build(ShutdownEndpointService.class);
  
  		String shutdown = shutdownEndpointService.shutdown();
  
  		System.out.println(shutdown);
  	}
  }
  ```

## 7月31日课后作业（第五周）

  参考实现类org.geektimes.cache.interceptor.CachePutInterceptor，实现@javax.cache.annotation.CacheRemove 注解的 @Interceptor Class

  

  **步骤1**、实现@javax.cache.annotation.CacheRemove 注解的 @Interceptor Class

  ```java
  @Interceptor
  public class CacheRemoveInterceptor extends CacheOperationInterceptor<CacheRemove> {
  
      @Override
      protected CacheOperationAnnotationInfo getCacheOperationAnnotationInfo(CacheRemove cacheOperationAnnotation,
                                                                             CacheDefaults cacheDefaults) {
          return new CacheOperationAnnotationInfo(cacheOperationAnnotation, cacheDefaults);
      }
  
      @Override
      protected Object beforeExecute(CacheRemove cacheOperationAnnotation,
                                     CacheKeyInvocationContext<CacheRemove> cacheKeyInvocationContext,
                                     CacheOperationAnnotationInfo cacheOperationAnnotationInfo, Cache cache,
                                     Optional<GeneratedCacheKey> cacheKey) {
          if (!cacheOperationAnnotationInfo.isAfterInvocation()) {
              manipulateCache(cacheKeyInvocationContext, cache, cacheKey);
          }
          return null;
      }
  
      @Override
      protected void afterExecute(CacheRemove cacheOperationAnnotation,
                                  CacheKeyInvocationContext<CacheRemove> cacheKeyInvocationContext,
                                  CacheOperationAnnotationInfo cacheOperationAnnotationInfo, Cache cache,
                                  Optional<GeneratedCacheKey> cacheKey, Object result) {
          if (cacheOperationAnnotationInfo.isAfterInvocation()) {
              manipulateCache(cacheKeyInvocationContext, cache, cacheKey);
          }
      }
  
      private void manipulateCache(CacheKeyInvocationContext<CacheRemove> cacheKeyInvocationContext,
                                   Cache cache, Optional<GeneratedCacheKey> cacheKey) {
          cacheKey.ifPresent(key -> {
              CacheInvocationParameter valueParameter = cacheKeyInvocationContext.getValueParameter();
              if (valueParameter != null) {
                  cache.remove(key, valueParameter.getValue());
              } else {
                  cache.remove(key);
              }
          });
      }
  
      @Override
      protected void handleFailure(CacheRemove cacheOperationAnnotation,
                                   CacheKeyInvocationContext<CacheRemove> cacheKeyInvocationContext,
                                   CacheOperationAnnotationInfo cacheOperationAnnotationInfo, Cache cache,
                                   Optional<GeneratedCacheKey> cacheKey, Throwable failure) {
          cacheKey.ifPresent(key -> {
              cache.remove(key, failure);
          });
      }
  }
  ```

  **步骤2**、在 src/main/resources下建立 /META-INF/services 目录， 新增一个以接口命名的文件 (`org.geektimes.interceptor.Interceptor`文件)，内容是要应用的实现类

  ```
  org.geektimes.cache.annotation.interceptor.CacheRemoveInterceptor
  ```

  **步骤3**、2、定义服务实现`CacheRemoveInterceptor`的测试类`CacheRemoveInterceptorTest.java`

  ```java
  public class CacheRemoveInterceptorTest {
  
      private DataRepository dataRepository = new InMemoryDataRepository();
  
      private InterceptorEnhancer enhancer = new DefaultInterceptorEnhancer();
  
      @Test
      public void test() {
          DataRepository repository = enhancer.enhance(dataRepository, DataRepository.class, new CachePutInterceptor(), new CacheRemoveInterceptor());
          assertTrue(repository.create("A", 1));
          assertTrue(repository.remove("A"));
          assertNull(repository.get("A"));
      }
  }
  ```

## 8月7日课后作业（第六周）

### 作业

增加一个注解名为 @ConfigSources，使其能够关联多个 @ConfigSource，并且在 @ConfigSource 使用 Repeatable；
可以对比参考 Spring 中 @PropertySources 与 @PropertySource，并且文字说明 Java 8 @Repeatable 实现原理。

- 可选作业，根据 URL 与 URLStreamHandler 的关系，扩展一个自定义协议，可参考 sun.net.www.protocol.classpath.Handler

## 8月14日课后作业（第七周）

### 作业

描述 Spring 校验注解 org.springframework.validation.annotation.Validated 的工作原理，它与 Spring Validator 以及 JSR-303 Bean Validation @javax.validation.Valid 之间的关系

#### `@Valid`和`@Validated`

**简述JSR303，hibernate validation，spring validation之间的关系。JSR303是一项标准，他们规定一些校验规范即校验注解，如@Null，@NotNull，@Pattern，他们位于javax.validation.constraints包下，只提供规范不提供实现。而hibernate validation是对这个规范的实践，他提供了相应的实现，并增加了一些其他校验注解，如@Email，@Length，@Range等等，他们位于org.hibernate.validator.constraints包下。而万能的spring为了给开发者提供便捷，对hibernate validation进行了二次封装，显示校验validated bean时，你可以使用spring validation或者hibernate validation，而spring validation另一个特性，便是其在springmvc模块中添加了自动校验，并将校验信息封装进了特定的类中。**

`@Valid`和`@Validated`区别

| 区别             | @Valid                                          | @Validated              |
| ---------------- | ----------------------------------------------- | ----------------------- |
| 提供者           | JSR-303规范                                     | Spring                  |
| **是否支持分组** | 不支持                                          | 支持                    |
| 标注位置         | METHOD, FIELD, CONSTRUCTOR, PARAMETER, TYPE_USE | TYPE, METHOD, PARAMETER |
| **嵌套校验**     | 支持                                            | 不支持                  |

org.springframework.validation.Validator来提供类的验证行为
org.springframework.validation.Validator接口的方法：

- support（Class）-此验证程序可以验证提供的Class的实例吗？
- validate（Object，org.springframework.validation.Errors）-验证给定对象，并在验证错误的情况下，向给定Errors对象注册

**Spring中使用**

- 参考自： `MethodValidationPostProcessor`

> `@Validated`需要标记在类上才会生成代理对象,
>
> 参考`MethodValidationInterceptor.determineValidationGroups`，可以在方法上加@Validated实现分组

**SprinvMVC中**

- 参考自： `RequestResponseBodyMethodProcessor`

`@Valid`不支持分组校验

> `@Valid`注解内部是空的,没有任何属性

`@Validated`支持分组,但是无法在嵌套中分组

> 两个注解在代码中唯一的不同就是`determineValidationHints`方法,该方法返回的校验的分组标识类,**@Validated及扩展注解支持分组**
>
> 但是Spring仅仅是封装了`org.hibernate.validator.internal.engine.ValidatorImpl`,校验的执行逻辑也是该类负责执行; 对于嵌套的校验,`@Validated`属于外来注解, 因此嵌套内只识别`@Valid`注解 **`@Validated不支持嵌套分组`**

**源码跟踪<`Version:SpringBoot spring-boot.version,Spring: 5.3.9`>**

**两个注解类代码**

```
//类, 方法, 参数
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Validated {
	Class<?>[] value() default {};
}

//方法, 字段, 构造器, 参数, 泛型
@Target({ METHOD, FIELD, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Documented
public @interface Valid {

}
```

**`LocalValidatorFactoryBean`的来源**

- `PrimaryDefaultValidatorPostProcessor`

```
class PrimaryDefaultValidatorPostProcessor implements ImportBeanDefinitionRegistrar, BeanFactoryAware {
    //validator在springBoot中默认的bean名称
	private static final String VALIDATOR_BEAN_NAME = "defaultValidator";
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		BeanDefinition definition = getAutoConfiguredValidator(registry);
        //如果容器中没有其他Validator,这个默认的就是Primary的Bean
		if (definition != null) {
			definition.setPrimary(!hasPrimarySpringValidator());
		}
	}
	private BeanDefinition getAutoConfiguredValidator(BeanDefinitionRegistry registry) {
		if (registry.containsBeanDefinition(VALIDATOR_BEAN_NAME)) {
			BeanDefinition definition = registry.getBeanDefinition(VALIDATOR_BEAN_NAME);
            //如果没有指定,那么默认情况下Validator的工厂bean为:`LocalValidatorFactoryBean`
			if (definition.getRole() == BeanDefinition.ROLE_INFRASTRUCTURE
					&& isTypeMatch(VALIDATOR_BEAN_NAME, LocalValidatorFactoryBean.class)) {
				return definition;
			}
		}
		return null;
	}
}
```

**Spring**

**测试代码**

```
@Service
@Validated
public class ValidateService {
    Random random = new Random();
    public @NotBlank String getMessage(@NotBlank String message) {
        if (random.nextBoolean()) {
            return message;
        }
        return null;
    }
}
```

**实例化入口**

- `org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration`

```
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(ExecutableValidator.class)
//资源对应在`hibernate-validator`包中
@ConditionalOnResource(resources = "classpath:META-INF/services/javax.validation.spi.ValidationProvider")
//创建Validator的入口
@Import(PrimaryDefaultValidatorPostProcessor.class)
public class ValidationAutoConfiguration {
}
```

**执行处理器**

- `MethodValidationPostProcessor`

```
/**
 * 简单的翻译：
 * 一个BeanPostprocessor的实现类
 * 适用于入参和返回值
 * 目标类需要标记Spring的Validated注解在他的类级别上以便于他们方法建立约束
 *
 * A convenient {@link BeanPostProcessor} implementation that delegates to a
 * JSR-303 provider for performing method-level validation on annotated methods.

 * <p>Applicable methods have JSR-303 constraint annotations on their parameters
 * and/or on their return value (in the latter case specified at the method level,
 * typically as inline annotation), e.g.:
 *
 * <pre class="code">
 * public @NotNull Object myValidMethod(@NotNull String arg1, @Max(10) int arg2)
 * </pre>
 *

 * <p>Target classes with such annotated methods need to be annotated with Spring's
 * {@link Validated} annotation at the type level, for their methods to be searched for
 * inline constraint annotations. Validation groups can be specified through {@code @Validated}
 * as well. By default, JSR-303 will validate against its default group only.
 *
 * <p>As of Spring 5.0, this functionality requires a Bean Validation 1.1+ provider.
 * @see javax.validation.executable.ExecutableValidator
 */
@SuppressWarnings("serial")
public class MethodValidationPostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor
		implements InitializingBean {
    
	@Override
	public void afterPropertiesSet() {
		Pointcut pointcut = new AnnotationMatchingPointcut(this.validatedAnnotationType, true);
		this.advisor = new DefaultPointcutAdvisor(pointcut, createMethodValidationAdvice(this.validator));
	}
}
```

**拦截器处理类**

- `MethodValidationInterceptor`

```
public class MethodValidationInterceptor implements MethodInterceptor {
	@Override
	@Nullable
	public Object invoke(MethodInvocation invocation) throws Throwable {
		//根据group进行分组 ， 进支持Validated注解； 不支持扩展注解了
		Class<?>[] groups = determineValidationGroups(invocation);
		//...
        //proceed执行前对入参校验
        result = execVal.validateParameters(target, methodToValidate, invocation.getArguments(), groups);
		//方法执行
		Object returnValue = invocation.proceed();
		//proceed执行后对结果校验
		result = execVal.validateReturnValue(target, methodToValidate, returnValue, groups);
		//...
	}
}
```

**`SpringMVC`入口**

**测试代码**

```
    @PostMapping("/valid")
    public ResponseEntity valid(@Valid @RequestBody User user) {
        System.out.println("user = " + user);
        return ResponseEntity.ok().build();
    }
```

**实例化入口**

- `springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration`

```
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class })
@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 10)
@AutoConfigureAfter({ DispatcherServletAutoConfiguration.class, TaskExecutionAutoConfiguration.class,
		ValidationAutoConfiguration.class })
public class WebMvcAutoConfiguration {
    ...
    //默认取得就是Spring中实例化的对象
    @Bean
    public Validator mvcValidator() {
        return !ClassUtils.isPresent("javax.validation.Validator", this.getClass().getClassLoader()) ? super.mvcValidator() : 		  	ValidatorAdapter.get(this.getApplicationContext(), this.getValidator());
    }
    ...
}
```

**执行处理器：**

- `org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor`

```
/**
	 * Throws MethodArgumentNotValidException if validation fails.
	 * @throws HttpMessageNotReadableException if {@link RequestBody#required()}
	 * is {@code true} and there is no body content or if there is no suitable
	 * converter to read the content with.
	 */
@Override
public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
                              NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {

    parameter = parameter.nestedIfOptional();
    Object arg = readWithMessageConverters(webRequest, parameter, parameter.getNestedGenericParameterType());
    String name = Conventions.getVariableNameForParameter(parameter);

    if (binderFactory != null) {
        WebDataBinder binder = binderFactory.createBinder(webRequest, arg, name);
        if (arg != null) {
            validateIfApplicable(binder, parameter);
            if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
                throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
            }
        }
        if (mavContainer != null) {
            mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());
        }
    }

    return adaptArgumentIfNecessary(arg, parameter);
}	

/**
* Validate the binding target if applicable.
* <p>The default implementation checks for {@code @javax.validation.Valid},
* Spring's {@link org.springframework.validation.annotation.Validated},
* and custom annotations whose name starts with "Valid".
* @param binder the DataBinder to be used
* @param parameter the method parameter descriptor
*/
protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
    Annotation[] annotations = parameter.getParameterAnnotations();
    for (Annotation ann : annotations) {
        Object[] validationHints = ValidationAnnotationUtils.determineValidationHints(ann);
        if (validationHints != null) {
            binder.validate(validationHints);
            break;
        }
    }
}
```

**`@Valid`处理不了分组的原因代码**

- `determineValidationHints(ann)`

```
	/**
	 *  Valid返回空；
	 * Validated和Valid前缀的； 返回value对应的类<分组标志>
	 * Determine any validation hints by the given annotation.
	 * <p>This implementation checks for {@code @javax.validation.Valid},
	 * Spring's {@link org.springframework.validation.annotation.Validated},
	 * and custom annotations whose name starts with "Valid".
	 * @param ann the annotation (potentially a validation annotation)
	 * @return the validation hints to apply (possibly an empty array),
	 * or {@code null} if this annotation does not trigger any validation
	 */
	@Nullable
	public static Object[] determineValidationHints(Annotation ann) {
		Class<? extends Annotation> annotationType = ann.annotationType();
		String annotationName = annotationType.getName();
		if ("javax.validation.Valid".equals(annotationName)) {
			return EMPTY_OBJECT_ARRAY;
		}
		Validated validatedAnn = AnnotationUtils.getAnnotation(ann, Validated.class);
		if (validatedAnn != null) {
			Object hints = validatedAnn.value();
			return convertValidationHints(hints);
		}
		if (annotationType.getSimpleName().startsWith("Valid")) {
			Object hints = AnnotationUtils.getValue(ann);
			return convertValidationHints(hints);
		}
		return null;
	}
```

## 8月21日 课后作业（第八周）

### 作业

利用 Reactor Mono API
配合 Reactive Streams Publisher 实现，让 Subscriber 实现能够获取到数据，可以参考以下代码

```java
SimplePublisher();
Mono.from(publisher)
    .subscribe(new BusinessSubscriber(5));
for (int i = 0; i < 5; i++) {
    publisher.publish(i);
}
```



## 8月28日课后作业（第九周）

### 作业

实现分布式事件，基于 Zookeeper 或者 JMS 来实现

#### 基于JMS消息的事件封装

抽象封装`AbstractJmsEvent.java`

```java
public abstract class AbstractJmsEvent<S> extends GenericEvent<S> {

    public static final String topic = "distribute-event-topic";

    public AbstractJmsEvent(S source) {
        super(source);
    }
    
    public abstract Message createMessage(Session session) throws JMSException;

    protected <M extends Message> M setBaseProperties(Message message) throws JMSException;

    protected abstract Class<?> getMessageClassType();
}
```

- `createMessage` 根据当前事件，创建相应的`javax.jms.Message`
- `setBaseProperties` 酌情设置`javax.jms.Message`的属性，即消息头
- `getMessageClassType` 创建出来的真实`javax.jms.Message`类型

提供下列默认实现

- `TextJmsEvent`对应`javax.jms.TextMessage`

#### 本地事件发送

##### 1.本地事件发布

`JmsEventPublisher.java`

```java
public class JmsEventPublisher extends ParallelEventDispatcher {

    public JmsEventPublisher() {
        super();
    }
    public JmsEventPublisher(Executor executor) {
        super(executor);
    }

    @Override
    protected void loadEventListenerInstances();

    public void publish(Destination destination, AbstractJmsEvent<?> event);
}
```

[Destination](https://github.com/Kurok1/geek-project/blob/main/09-distributed-event/src/main/java/org/geektimes/commons/event/jms/Destination.java) 接口定义了目标JMS服务设施，即这个事件需要发送到哪个JMS上

`loadEventListenerInstances`用于加载已实现的本地事件发射器

##### 2.JMS消息推送

本地事件发射器抽象 `JmsEventEmitter.java`主要作用是创建本地`javax.jms.Session`并将事件转换成`javax.jms.Message`,根据`Destination`发送给JMS服务

```java
public abstract class JmsEventEmitter<E extends AbstractJmsEvent<?>> extends LocalSessionProvider implements ConditionalEventListener<E> {

    protected final Properties properties = loadProperties();

    /**
     * @return 从配置文件加载JMS连接相关配置
     */
    protected Properties loadProperties();

    @Override
    public boolean accept(E event);

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

    protected abstract Set<org.geektimes.commons.event.jms.Destination> getSupportedDestinations();

    protected Destination getDestination() {
        return (Topic) () -> AbstractJmsEvent.topic;
    }

}
```

目前实现：

- 基于ActiveMQ, `ActiveMQEventEmitter.java`

#### 本地事件接收

##### 1.JMS消息订阅

定义抽象JMS消息订阅 `JmsEventSubscriber.java`

```java
public abstract class JmsEventSubscriber extends LocalSessionProvider implements MessageListener, EventDispatcher, Runnable {

    private final CopyOnWriteArrayList<EventListener<? extends Event>> listeners = new CopyOnWriteArrayList<>();
    protected final Properties properties;
    private final MessageEventResolver resolver = new MessageEventResolver();

    public JmsEventSubscriber() {
        this.properties = loadProperties();
        loadListenersFromSpi();
    }
    protected void loadListenersFromSpi();

    @Override
    public void onMessage(Message message) {
        try {
            dispatch(resolver.resolveMessage(message));
        } catch (JMSException e) {
            //不要终止进程
            System.err.println(e.getMessage());
        }
    }
    protected Properties loadProperties();
    protected Destination getDestination() {
        return (Topic) () -> AbstractJmsEvent.topic;
    }

    /**
     * 分发事件给本地
     * @param event
     */
    @Override
    public void dispatch(Event event);
    @Override
    public void addEventListener(EventListener<?> listener) throws NullPointerException, IllegalArgumentException;
    @Override
    public void removeEventListener(EventListener<?> listener) throws NullPointerException, IllegalArgumentException;
    @Override
    public List<EventListener<?>> getAllEventListeners();

    @Override
    public void run() {//监听
        try {
            MessageConsumer consumer = getSession(this.properties).createConsumer(this.getDestination());
            consumer.setMessageListener(this);
            while (true) {
                //阻塞当前线程
            }
        } catch (Throwable t) {
            onDestroy();
            throw new RuntimeException(t);
        }
    }
    /**
     * 订阅线程出现异常时触发
     */
    protected void onDestroy();
}
```

其中`MessageEventResolver.java`用于将订阅得到的`javax.jms.Message`转换成本地事件

目前实现

- ActiveMQ `ActiveMQEventSubscriber.java`

##### 2.本地消息推送

本地事件推送的核心处理定义在`org.geektimes.event.distributed.jms.subscriber.JmsEventSubscriber#dispatch(Event)`方法中

```java
    public void dispatch(Event event) {
        Executor executor = getExecutor();

        // execute in sequential or parallel execution model
        executor.execute(() -> {
            for (EventListener listener : listeners) {
                Class<? extends Event> eventType = EventListener.findEventType(listener);
                //判断类型
                if (!event.getClass().equals(eventType))
                    continue;

                if (listener instanceof ConditionalEventListener) {
                    ConditionalEventListener predicateEventListener = (ConditionalEventListener) listener;
                    if (!predicateEventListener.accept(event)) { // No accept
                        return;
                    }
                }
                // Handle the event
                listener.onEvent(event);
            }
        });
    }
```

根据已注册的`EventListener`,判断监听事件类型，完成分发

#### 代码测试

事件的监听

```java
public class MyListener implements EventListener<TextJmsEvent> {

    @Override
    public void onEvent(TextJmsEvent event) {
        System.out.println(event.getSource());
    }
}
```

订阅

```java
public class Subscriber {

    public static void main(String[] args) {
        new Thread(new ActiveMQEventSubscriber()).start();
    }

}
```

发布事件

```java
public class Publisher{

    public static void main(String[] args) {
        JmsEventPublisher jmsEventPublisher = new JmsEventPublisher();
        jmsEventPublisher.publish(Destination.fromActiveMQ(), new TextJmsEvent("Hello"));
    }

}
```

#### 基于ZK消息的事件封装

##### 本地事件发送`DistributedZKEventPublisher.java`

```java
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.geektimes.event.distributed.zk;

import org.apache.curator.framework.api.transaction.CuratorOp;
import org.apache.curator.x.async.AsyncCuratorFramework;
import org.apache.curator.x.async.modeled.ModeledFramework;
import org.apache.curator.x.async.modeled.typed.TypedModeledFramework2;
import org.geektimes.event.distributed.zk.pubsub.Clients;
import org.geektimes.event.distributed.zk.pubsub.messages.LocationAvailable;
import org.geektimes.event.distributed.zk.pubsub.messages.UserCreated;
import org.geektimes.event.distributed.zk.pubsub.model.Group;
import org.geektimes.event.distributed.zk.pubsub.model.Instance;
import org.geektimes.event.distributed.zk.pubsub.model.Message;
import org.geektimes.event.distributed.zk.pubsub.model.Priority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DistributedZKEventPublisher {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final AsyncCuratorFramework client;

    public DistributedZKEventPublisher(AsyncCuratorFramework client) {
        this.client = Objects.requireNonNull(client, "client cannot be null");
    }

    /**
     * Publish the given instance using the Instance client template
     *
     * @param instance instance to publish
     */
    public void publishInstance(Instance instance) {
        ModeledFramework<Instance> resolvedClient = Clients.instanceClient.resolved(client, instance.getType());
        resolvedClient.set(instance).exceptionally(e -> {
            log.error("Could not publish instance: " + instance, e);
            return null;
        });
    }

    /**
     * Publish the given instances using the Instance client template in a transaction
     *
     * @param instances instances to publish
     */
    public void publishInstances(List<Instance> instances) {
        List<CuratorOp> operations = instances.stream()
                .map(instance -> Clients.instanceClient.resolved(client, instance.getType()).createOp(instance))
                .collect(Collectors.toList());
        client.transaction().forOperations(operations).exceptionally(e -> {
            log.error("Could not publish instances: " + instances, e);
            return null;
        });
    }

    /**
     * Publish the given LocationAvailable using the LocationAvailable client template
     *
     * @param group group
     * @param locationAvailable message to publish
     */
    public void publishLocationAvailable(Group group, LocationAvailable locationAvailable) {
        publishMessage(Clients.locationAvailableClient, group, locationAvailable);
    }

    /**
     * Publish the given UserCreated using the UserCreated client template
     *
     * @param group group
     * @param userCreated message to publish
     */
    public void publishUserCreated(Group group, UserCreated userCreated) {
        publishMessage(Clients.userCreatedClient, group, userCreated);
    }

    /**
     * Publish the given LocationAvailables using the LocationAvailable client template in a transaction
     *
     * @param group group
     * @param locationsAvailable messages to publish
     */
    public void publishLocationsAvailable(Group group, List<LocationAvailable> locationsAvailable) {
        publishMessages(Clients.locationAvailableClient, group, locationsAvailable);
    }

    /**
     * Publish the given UserCreateds using the UserCreated client template in a transaction
     *
     * @param group group
     * @param usersCreated messages to publish
     */
    public void publishUsersCreated(Group group, List<UserCreated> usersCreated) {
        publishMessages(Clients.userCreatedClient, group, usersCreated);
    }

    private <T extends Message> void publishMessage(TypedModeledFramework2<T, Group, Priority> typedClient, Group group,
            T message) {
        ModeledFramework<T> resolvedClient = typedClient.resolved(client, group, message.getPriority());
        resolvedClient.set(message).exceptionally(e -> {
            log.error("Could not publish message: " + message, e);
            return null;
        });
    }

    private <T extends Message> void publishMessages(TypedModeledFramework2<T, Group, Priority> typedClient,
            Group group, List<T> messages) {
        List<CuratorOp> operations = messages.stream()
                .map(message -> typedClient.resolved(client, group, message.getPriority()).createOp(message))
                .collect(Collectors.toList());
        client.transaction().forOperations(operations).exceptionally(e -> {
            log.error("Could not publish messages: " + messages, e);
            return null;
        });
    }

}
```

##### 本地事件接收`DistributedEventZKSubscriber.java`

```java
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
```

##### 客户端封装`Clients.java`

```java
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
```

