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


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spare.n52.yadarts.entity.Player;
import spare.n52.yadarts.entity.PointEvent;
import spare.n52.yadarts.games.AbstractGame;
import spare.n52.yadarts.games.GameEventAdapter;
import spare.n52.yadarts.games.GameEventBus;
import spare.n52.yadarts.games.GameStatusUpdateAdapter;
import spare.n52.yadarts.games.GameStatusUpdateListener;
import spare.n52.yadarts.games.Score;
import yadarts.server.RuntimeEngine;
import yadarts.server.entity.BasicMessage;
import yadarts.server.entity.BounceOutInteractionEntity;
import yadarts.server.entity.CurrentPlayerChangedEntity;
import yadarts.server.entity.DartMissedInteractionEntity;
import yadarts.server.entity.GameEntity;
import yadarts.server.entity.GameFinishedEntity;
import yadarts.server.entity.GameStatus;
import yadarts.server.entity.NextPlayerPressedInteractionEntity;
import yadarts.server.entity.PlayerBustedEntity;
import yadarts.server.entity.PlayerFinishedEntity;
import yadarts.server.entity.PointEventEntity;
import yadarts.server.entity.RemainingScoreEntity;
import yadarts.server.entity.RoundStartedEntity;
import yadarts.server.entity.TurnFinishedEntity;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class AtmosphereEventHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(AtmosphereEventHandler.class);

	private GameEventBus gameEventBus;
	
	private Map<AbstractGame, GameStatusUpdateListener> gameListeners = new HashMap<>();
	
	@Inject
	public AtmosphereEventHandler(GameEventBus bus, RuntimeEngine engine) {
		this.gameEventBus = bus;
		
		this.gameEventBus.registerListener(new GameEventAdapter() {
			
			@Override
			public void onGameStarted(AbstractGame game) {
				broadcastMessage(new GameStatus(new GameEntity(game), "started"));
				
				LocalGameStatusListener l = new LocalGameStatusListener();
				game.registerGameListener(l);
				synchronized (AtmosphereEventHandler.this) {
					gameListeners.put(game, l);	
				}
			}
			
			@Override
			public void onGameFinished(AbstractGame game) {
				broadcastMessage(new GameStatus(new GameEntity(game), "finished"));
				
				GameStatusUpdateListener l = gameListeners.get(game);
				synchronized (AtmosphereEventHandler.this) {
					if (l != null) {
						gameListeners.remove(game);
					}
				}
			}
			
		});
		
		logger.info("initialized!");
	}
	
	protected void broadcastMessage(BasicMessage msg) {
		logger.info("broadcasting message: "+ msg);
		Broadcaster b = BroadcasterFactory.getDefault().lookup("/*");
		
		if (b != null) {
			b.broadcast(msg);
		}
	}
	
	private class LocalGameStatusListener extends GameStatusUpdateAdapter {
		
		@Override
		public void onPointEvent(PointEvent event) {
			broadcastMessage(new PointEventEntity(event));
		}
		
		@Override
		public void onBounceOutPressed() {
			broadcastMessage(new BounceOutInteractionEntity());
		}
		
		@Override
		public void onDartMissedPressed() {
			broadcastMessage(new DartMissedInteractionEntity());
		}
		
		@Override
		public void onNextPlayerPressed() {
			broadcastMessage(new NextPlayerPressedInteractionEntity());
		}
		
		@Override
		public void onCurrentPlayerChanged(Player currentPlayer, Score score) {
			broadcastMessage(new CurrentPlayerChangedEntity(currentPlayer, score));
		}
		
		@Override
		public void onTurnFinished(Player player, Score score) {
			broadcastMessage(new TurnFinishedEntity(player, score));
		}
		
		@Override
		public void onRoundStarted(int roundNumber) {
			broadcastMessage(new RoundStartedEntity(roundNumber));
		}
		
		@Override
		public void onBust(Player currentPlayer, Score score) {
			broadcastMessage(new PlayerBustedEntity(currentPlayer, score));
		}
		
		@Override
		public void onRemainingScoreForPlayer(Player player, Score score) {
			broadcastMessage(new RemainingScoreEntity(player, score));
		}
		
		@Override
		public void onPlayerFinished(Player player) {
			broadcastMessage(new PlayerFinishedEntity(player));
		}
		
		@Override
		public void onGameFinished(Map<Player, Score> playerScoreMap,
				List<Player> winner) {
			broadcastMessage(new GameFinishedEntity(playerScoreMap, winner));
		}
		
	}
	
}
