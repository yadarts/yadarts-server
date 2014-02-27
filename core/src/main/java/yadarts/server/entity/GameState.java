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

import java.util.List;
import java.util.Map;

import spare.n52.yadarts.entity.Player;
import spare.n52.yadarts.games.Score;

public class GameState {

	private String name;
	private Map<Player, Score> scores;
	private List<Player> players;

	public void setName(String shortName) {
		this.name = shortName;
	}

	public void setScores(Map<Player, Score> scores) {
		this.scores = scores;
	}

	public String getName() {
		return name;
	}

	public Map<Player, Score> getScores() {
		return scores;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public List<Player> getPlayers() {
		return players;
	}
	

}
