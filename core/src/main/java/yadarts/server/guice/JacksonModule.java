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


import yadarts.server.json.JsonWriter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;

public class JacksonModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(JsonWriter.class).in(Scopes.SINGLETON);
	}

	@Provides
	@Singleton
	public JsonNodeFactory jsonNodeFactory() {
		return JsonNodeFactory.withExactBigDecimals(false);
	}

	@Provides
	@Singleton
	public ObjectReader objectReader(ObjectMapper mapper) {
		return mapper.reader();
	}

	@Provides
	@Singleton
	public ObjectWriter objectWriter(ObjectMapper mapper) {
		return mapper.writer();
	}

	@Provides
	@Singleton
	public ObjectMapper objectMapper(JsonNodeFactory factory) {
		return new ObjectMapper().setNodeFactory(factory).disable(
				DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
	}
}
