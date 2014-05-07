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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spare.n52.yadarts.AlreadyRunningException;
import spare.n52.yadarts.EventEngine;
import spare.n52.yadarts.InitializationException;
import spare.n52.yadarts.entity.InteractionEvent;
import spare.n52.yadarts.entity.PointEvent;
import spare.n52.yadarts.event.EventListener;
import spare.n52.yadarts.games.AbstractGame;
import spare.n52.yadarts.games.GameEventAdapter;
import spare.n52.yadarts.games.GameEventBus;
import yadarts.server.Constants;
import yadarts.server.RuntimeEngine;
import yadarts.server.entity.GameScoreSummary;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;

@Path(Constants.ROOT_RESOURCE_URL)
public class RootResource {

	private static final Logger logger = LoggerFactory
			.getLogger(RootResource.class);
	private RuntimeEngine engine;

	@Inject
	private JsonNodeFactory factory;

	protected PointEvent lastPointHit;
	
	@Context
	private UriInfo uriInfo;

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
	public JsonNode get() {
		
		ObjectNode root = factory.objectNode();
		root.put("game", this.uriInfo.getAbsolutePathBuilder()
				.path("game").build().toString());

		return root;
	}

	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@POST
	public GameScoreSummary startGame(AbstractGame game)
			throws InitializationException, AlreadyRunningException {
		if (this.engine.hasActiveGame()) {
			throw new AlreadyRunningException(
					"There is already an active game!");
		}

		GameScoreSummary map = new GameScoreSummary();

		if (game != null) {
			map.setName(game.getShortName());
			map.setPlayers(game.getPlayers());
			map.setScores(game.getScores());

			initializeEventEngine(game);
		} else {
			map.setName("null");
		}

		return map;
	}

	private void initializeEventEngine(AbstractGame game)
			throws InitializationException, AlreadyRunningException {
		EventEngine eventEngine = this.engine.getEventEngine();

		eventEngine.registerListener(game);

		GameEventBus.instance().startGame(game);
		
		GameEventBus.instance().registerListener(new LocalGameEventListener());

		eventEngine.start();

	}
	
	private final class LocalGameEventListener extends GameEventAdapter {
		
		@Override
		public void onGameStarted(AbstractGame game) {
			logger.info("Game started!");
		}
		
		@Override
		public void onGameFinished(AbstractGame game) {
			logger.info("Game finished!");
			GameEventBus.instance().unRegisterListener(this);
		}
		
	}
}
