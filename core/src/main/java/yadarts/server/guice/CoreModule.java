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
package yadarts.server.guice;

import spare.n52.yadarts.games.GameEventBus;
import yadarts.server.RuntimeEngine;
import yadarts.server.json.GameFinishedEntityEncoder;
import yadarts.server.json.GameStateEncoder;
import yadarts.server.json.PlayerEncoder;
import yadarts.server.json.ScoreEncoder;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class CoreModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(RuntimeEngine.class).in(Scopes.SINGLETON);
		
		bind(GameEventBus.class).toInstance(GameEventBus.instance());
		
		bind(ScoreEncoder.class);
		bind(PlayerEncoder.class);
		bind(GameStateEncoder.class);
		
		bind(GameFinishedEntityEncoder.class);
	}

}
