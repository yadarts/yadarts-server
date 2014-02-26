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
package yadarts.server.resources;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import yadarts.server.RuntimeEngine;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
@Path("/")
public class RootResource {

	@Inject
	private RuntimeEngine engine;
	
	@GET
	@Produces("text/plain")
	public String get(@QueryParam("x") String x) {
		return "Howdy Guice. Injected query parameter "
				+ (x != null ? "x = " + x : "x is not injected");
	}

	@Consumes("application/json")
	@Produces("application/json")
	@POST
	public Map<String, Object> startGame() {
		Map<String, Object> node = new HashMap<>();
		
		if (!engine.hasActiveGame()) {
			node.put("success", true);
			engine.startGame();
		}
		else {
			node.put("success", false);
		}
		
		return node;
	}
}
