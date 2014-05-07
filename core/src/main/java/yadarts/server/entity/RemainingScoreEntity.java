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

import javax.xml.bind.annotation.XmlRootElement;

import spare.n52.yadarts.entity.Player;
import spare.n52.yadarts.games.Score;
import yadarts.server.entity.GameEventEntity;
import yadarts.server.entity.ScoreEntity;

@XmlRootElement
public class RemainingScoreEntity extends GameEventEntity {

	public String player = "";
	public ScoreEntity score;
	
	public RemainingScoreEntity() {
		event = "remainingScore";
		score = new ScoreEntity();
	}

	public RemainingScoreEntity(Player p, Score s) {
		this();
		this.player = p.getName();
		this.score = new ScoreEntity(s);
	}
	
}
