/**
 * Copyright 2014 the staff of 52°North Initiative for Geospatial Open
 * Source Software GmbH in their free time
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package yadarts.server.websocket;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.atmosphere.annotation.Broadcast;
import org.atmosphere.annotation.Suspend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import yadarts.server.Constants;
import yadarts.server.entity.BasicMessage;

@Path(Constants.ATMOSPHERE_RESOURCE_URL)
@Produces(MediaType.APPLICATION_JSON)
public class AtmosphereResource {
	
	private static final Logger logger = LoggerFactory.getLogger(AtmosphereResource.class);
	
	@Inject
	protected AtmosphereEventHandler handler;
	
	@Suspend
	@GET
	public String suspend() {
		logger.info("Received a websocket suspense request.");
		return "";
	}

	 @Broadcast(writeEntity = false)
	 @POST
	 public BasicMessage broadcast(String message) {
		 return new BasicMessage();
	 }

}
