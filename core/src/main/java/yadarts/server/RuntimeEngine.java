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
package yadarts.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spare.n52.yadarts.EventEngine;
import spare.n52.yadarts.InitializationException;
import spare.n52.yadarts.games.AbstractGame;
import spare.n52.yadarts.games.GameEventAdapter;
import spare.n52.yadarts.games.GameEventBus;
import spare.n52.yadarts.games.GameEventListener;

import com.google.inject.Singleton;

@Singleton
public class RuntimeEngine {
	
	private static final Logger logger = LoggerFactory.getLogger(RuntimeEngine.class);
	private EventEngine eventEngine;
	private GameEventListener gameEventListener = new LocalListener();
	
	private boolean gameRunning;
	public AbstractGame activeGame;
	
	public RuntimeEngine() {
		try {
			eventEngine = EventEngine.instance();
			GameEventBus.instance().registerListener(gameEventListener);
		} catch (InitializationException e) {
			logger.warn(e.getMessage(), e);
			throw new IllegalStateException(e);
		}
	}
	
	public AbstractGame getActiveGame() {
		return activeGame;
	}

	public boolean hasActiveGame() {
		return gameRunning;
	}

	public EventEngine getEventEngine() {
		return eventEngine;
	}
	
	private class LocalListener extends GameEventAdapter {
		
		@Override
		public void onGameStarted(AbstractGame game) {
			gameRunning = true;
			activeGame = game;
		}
		
		@Override
		public void onGameFinished(AbstractGame game) {
			gameRunning = false;
			activeGame = null;
		}
		
	}

}
