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
package yadarts.server.decoding;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import spare.n52.yadarts.entity.Player;
import spare.n52.yadarts.entity.impl.PlayerImpl;
import spare.n52.yadarts.games.AbstractGame;
import spare.n52.yadarts.games.x01.GenericX01Game;

import com.fasterxml.jackson.databind.JsonNode;

@Provider
public class GameDecoder extends AbstractJSONReader<AbstractGame> {

    public GameDecoder() {
        super(AbstractGame.class);
    }

    @Override
    public AbstractGame decode(JsonNode j, MediaType mediaType) {
    	String gameName = j.get("game").textValue();
    	
    	AbstractGame game = null;
    	
    	List<Player> players = decodePlayers(j.get("players"));
    	
    	if ("301".equals(gameName)) {
    		game = GenericX01Game.create(players, 301);
    	}
    	else if ("501".equals(gameName)) {
    		game = GenericX01Game.create(players, 501);
    	}
    	else if ("701".equals(gameName)) {
    		game = GenericX01Game.create(players, 701);
    	}
    	
        return game;
    }

	private List<Player> decodePlayers(JsonNode jsonNode) {
		List<Player> result = new ArrayList<>();
		if (jsonNode.isArray()) {
			for (JsonNode p : jsonNode) {
				result.add(new PlayerImpl(p.textValue()));
			}
		}
		return result;
	}
}
