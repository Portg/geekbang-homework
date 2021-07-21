package com.salesmanager.test.shop.rest.service;

import com.salesmanager.shop.model.catalog.product.ReadableProductList;
import com.salesmanager.shop.rest.service.ProductItemsService;
import com.salesmanager.shop.rest.service.ShutdownEndpointService;
import org.apache.commons.lang.builder.ToStringBuilder;
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
