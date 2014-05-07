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

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Singleton;

import yadarts.server.entity.GameFinishedEntity;
import yadarts.server.entity.ScoreEntity;

@Provider
@Singleton
public class GameFinishedEntityEncoder extends AbstractJSONWriter<GameFinishedEntity> {

	public GameFinishedEntityEncoder() {
		super(GameFinishedEntity.class);
	}

	@Override
	public ObjectNode encode(GameFinishedEntity t, MediaType mt) {
		ObjectNode result = new ObjectNode(createJSONNodeFactory());
		
		result.put("time", t.time);
		result.put("event", t.event);
		
		result.put("scores", encodeScores(t.scores));
		result.put("winners", encodeWinners(t.winners));
		
		return result;
	}

	private JsonNode encodeWinners(List<String> winners) {
		ArrayNode result = new ArrayNode(createJSONNodeFactory());
		
		for (String w : winners) {
			result.add(w);
		}
		
		return result;
	}

	private JsonNode encodeScores(Map<String, ScoreEntity> scores) {
		ObjectNode result = new ObjectNode(createJSONNodeFactory());
		
		for (String player : scores.keySet()) {
			result.put(player, encodeScore(scores.get(player)));
		}
		
		return result;
	}

	private JsonNode encodeScore(ScoreEntity scoreEntity) {
		ObjectNode result = new ObjectNode(createJSONNodeFactory());
		result.put("thrownDarts", scoreEntity.thrownDarts);
		result.put("totalScore", scoreEntity.totalScore);
		result.put("totalTime", scoreEntity.totalTime);
		return result;
	}

}
