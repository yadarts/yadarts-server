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


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import spare.n52.yadarts.AlreadyRunningException;
import spare.n52.yadarts.entity.PointEvent;
import spare.n52.yadarts.games.AbstractGame;
import spare.n52.yadarts.games.GameEventBus;
import yadarts.server.RuntimeEngine;
import yadarts.server.entity.GameState;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;

@Path(RootResource.ROOT_RESOURCE_URL+ "/game")
public class GameManagerResource {
	
	private RuntimeEngine engine;
	
	@Inject
	private JsonNodeFactory factory;
	
	protected PointEvent lastPointHit;
	
	@Inject
	public GameManagerResource(RuntimeEngine e) {
		this.engine = e;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public GameState get() {
		GameState map = new GameState();
		AbstractGame game = engine.getActiveGame();
		
		if (game != null) {
			map.setName(game.getShortName());
			map.setPlayers(game.getPlayers());
			map.setScores(game.getScores());	
		}
		else {
			map.setName("null");
		}
		
		return map;
	}

	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@POST
	public JsonNode stopGame(JsonNode stopNode) throws AlreadyRunningException {
		if (!this.engine.hasActiveGame()) {
			throw new AlreadyRunningException("There is no active game!");
		}
		
		AbstractGame g = this.engine.getActiveGame();
		
		GameEventBus.instance().endGame(g);
		
		ObjectNode node = factory.objectNode();
		
		node.put("game", g.getShortName());
		node.put("stopped", true);
		
		return node;
	}

}
