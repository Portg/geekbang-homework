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
