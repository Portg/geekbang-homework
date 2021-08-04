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
  
  

