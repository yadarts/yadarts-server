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
import javax.ws.rs.core.MediaType;

import spare.n52.yadarts.EventEngine;
import spare.n52.yadarts.entity.InteractionEvent;
import spare.n52.yadarts.entity.PointEvent;
import spare.n52.yadarts.event.EventListener;
import spare.n52.yadarts.games.AbstractGame;
import yadarts.server.RuntimeEngine;
import yadarts.server.entity.GameState;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
@Path("/yadarts")
public class RootResource {

	private RuntimeEngine engine;
	
	protected PointEvent lastPointHit;
	
	@Inject
	public RootResource(RuntimeEngine e) {
		this.engine = e;
		EventEngine ee = engine.getEventEngine();
		ee.registerListener(new EventListener() {
			
			@Override
			public void receiveEvent(InteractionEvent event) {
				if (event instanceof PointEvent) {
					lastPointHit = (PointEvent) event;
				}
			}
		});
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
	public Map<String, Object> startGame() {
		Map<String, Object> node = new HashMap<>();
		
		if (!engine.hasActiveGame()) {
			node.put("success", true);
		}
		else {
			node.put("success", false);
		}
		
		return node;
	}
}
