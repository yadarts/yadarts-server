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
package yadarts.server.entity;

import java.util.HashMap;
import java.util.Map;

import spare.n52.yadarts.entity.Player;
import spare.n52.yadarts.entity.PointEvent;
import spare.n52.yadarts.games.GameStatusUpdateAdapter;
import spare.n52.yadarts.games.GameStatusUpdateListener;
import spare.n52.yadarts.games.Score;

public abstract class AbstractGame {
	
	private Map<Player, Score> playerScores = new HashMap<>();
	private GameStatusUpdateListener listener = new Listener();
	
	public void start(GameConfiguration g) {
	}
	
	public GameStatusUpdateListener getUpdateListener() {
		return this.listener;
	}
	
	private class Listener extends GameStatusUpdateAdapter {
		
		
		@Override
		public void onPointEvent(PointEvent event) {
		}
		
		@Override
		public void onTurnFinished(Player player, Score score) {
		}
		
		@Override
		public void onCurrentPlayerChanged(Player currentPlayer, Score score) {
		}
		
		@Override
		public void onRemainingScoreForPlayer(Player player, Score score) {
		}
	}
	
}
