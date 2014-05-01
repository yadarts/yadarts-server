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

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.MessageBodyReader;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;

@Consumes(MediaType.APPLICATION_JSON)
public abstract class AbstractJSONReader<T> implements MessageBodyReader<T>,
		JSONDecoder<T> {

	private final Class<T> classType;

	@Inject
	private JsonNodeFactory factory;
	@Inject
	private ObjectReader reader;

	public AbstractJSONReader(Class<T> classType) {
		this.classType = classType;
	}

	public abstract T decode(JsonNode j, MediaType mt);

	@Override
	public boolean isReadable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return this.classType.isAssignableFrom(type)
				&& mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE);
	}

	@Override
	public T readFrom(Class<T> c, Type gt, Annotation[] a, MediaType mt,
			MultivaluedMap<String, String> h, InputStream in)
			throws IOException, WebApplicationException {
		try {
			return decode(reader.readTree(in), mt);
		} catch (JsonParseException e) {
			ObjectNode error = factory.objectNode();
			error.put("error", e.getMessage());
			throw new WebApplicationException(Response
					.status(Status.BAD_REQUEST)
					.type(MediaType.APPLICATION_JSON_TYPE).entity(error)
					.build());
		}
	}

}
