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

**工作原理**

如果参数使用@javax.validation.Valid或Spring自己的@org.springframework.validation.annotation.Validated注释，则可以应用验证\

Spring boot 入口

- org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration

执行处理器

- org.springframework.validation.beanvalidation.MethodValidationPostProcessor

拦截器处理类

- MethodValidationInterceptor

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
