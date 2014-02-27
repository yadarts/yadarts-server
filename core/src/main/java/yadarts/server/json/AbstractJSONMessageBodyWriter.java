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

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;


@Produces(MediaType.APPLICATION_JSON)
public abstract class AbstractJSONMessageBodyWriter<T>
        implements MessageBodyWriter<T> {
	
    private ObjectWriter writer;
    private final Class<T> classType;

    public AbstractJSONMessageBodyWriter(Class<T> classType) {
        this.classType = classType;
        createWriter();
    }

    private void createWriter() {
    	ObjectMapper mapper = objectMapper(jsonNodeFactory());
		writer = objectWriter(mapper);
	}
    
    protected JsonNodeFactory jsonNodeFactory() {
        return JsonNodeFactory.withExactBigDecimals(false);
    }

    private ObjectWriter objectWriter(ObjectMapper mapper) {
        return mapper.writer();
    }

    public ObjectMapper objectMapper(JsonNodeFactory factory) {
        return new ObjectMapper().setNodeFactory(factory)
                .disable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
    }

	@Override
    public boolean isWriteable(Class<?> type, Type genericType,
                               Annotation[] annotations, MediaType mediaType) {
        return this.classType.isAssignableFrom(type) &&
               mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE);
    }

    @Override
    public void writeTo(T t, Class<?> c, Type gt, Annotation[] a, MediaType mt,
                        MultivaluedMap<String, Object> h,
                        OutputStream out) throws IOException,
                                                 WebApplicationException {
        writer.writeValue(out, encodeJSON(t, mt));
        out.flush();
    }

    @Override
    public long getSize(T t, Class<?> type, Type genericType,
                        Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    public ObjectNode emptyObject() {
    	return new ObjectNode(jsonNodeFactory());
    }
    
    public abstract ObjectNode encodeJSON(T t, MediaType mt);
}
