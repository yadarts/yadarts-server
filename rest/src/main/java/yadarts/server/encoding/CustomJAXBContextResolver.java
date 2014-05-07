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
package yadarts.server.encoding;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import com.google.inject.Singleton;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

import yadarts.server.entity.BasicMessage;
import yadarts.server.entity.BounceOutInteractionEntity;
import yadarts.server.entity.CurrentPlayerChangedEntity;
import yadarts.server.entity.DartMissedInteractionEntity;
import yadarts.server.entity.GameEntity;
import yadarts.server.entity.GameEventEntity;
import yadarts.server.entity.GameStatus;
import yadarts.server.entity.NextPlayerPressedInteractionEntity;
import yadarts.server.entity.PlayerBustedEntity;
import yadarts.server.entity.PlayerFinishedEntity;
import yadarts.server.entity.PointEventEntity;
import yadarts.server.entity.RemainingScoreEntity;
import yadarts.server.entity.RoundStartedEntity;
import yadarts.server.entity.ScoreEntity;
import yadarts.server.entity.TurnFinishedEntity;
import yadarts.server.entity.UserInteractionEntity;

@Provider
@Singleton
public class CustomJAXBContextResolver implements ContextResolver<JAXBContext> {

    private JAXBContext context;
    private Class<?>[] types = {
    		BasicMessage.class,
    		BounceOutInteractionEntity.class,
    		CurrentPlayerChangedEntity.class,
    		DartMissedInteractionEntity.class,
    		GameEntity.class,
    		GameEventEntity.class,
    		GameStatus.class,
    		NextPlayerPressedInteractionEntity.class,
    		PlayerBustedEntity.class,
    		PlayerFinishedEntity.class,
    		PointEventEntity.class,
    		RemainingScoreEntity.class,
    		RoundStartedEntity.class,
    		ScoreEntity.class,
    		TurnFinishedEntity.class,
    		UserInteractionEntity.class
    		};

    public CustomJAXBContextResolver() throws Exception {
        this.context = new JSONJAXBContext(JSONConfiguration.natural().build(), types);
    }

    public JAXBContext getContext(Class<?> objectType) {
        for (Class<?> type : types) {
            if (type == objectType) {
                return context;
            }
        }
        return null;
    }
}
