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
package yadarts.server.json;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import spare.n52.yadarts.entity.Player;
import spare.n52.yadarts.games.Score;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import yadarts.server.entity.GameState;

@Provider
@Singleton
public class GameStateEncoder extends AbstractJSONMessageBodyWriter<GameState> {

	private ScoreEncoder scoreEncoder;

	@Inject
	public GameStateEncoder(ScoreEncoder se) {
		super(GameState.class);
		this.scoreEncoder = se;
	}
	
	@Override
	public ObjectNode encodeJSON(GameState t, MediaType mt) {
		ObjectNode node = emptyObject();
		node.put("name", t.getName());
		ObjectNode scores = emptyObject();
		
		for (Player p : t.getPlayers()) {
			Score s = t.getScores().get(p);
			scores.put(p.getName(), (s == null ? emptyObject() : scoreEncoder.encodeJSON(s, mt)));
		}
		
		node.put("scores", scores);
		return node;
	}

}
