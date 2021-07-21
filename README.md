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

  