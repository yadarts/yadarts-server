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
package yadarts.server.servlet;

import java.util.Collections;
import java.util.Map;
import java.util.ServiceLoader;

import org.atmosphere.guice.AtmosphereGuiceServlet;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.json.JSONConfiguration;

public class BaseServletListener extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new ServletModule() {
			@Override
			protected void configureServlets() {
				ServiceLoader<Module> modules = ServiceLoader.load(Module.class);
				
				for (Module module : modules) {
					install(module);
				}
			
				bind(new TypeLiteral<Map<String, String>>() {
		        }).annotatedWith(Names.named(AtmosphereGuiceServlet.PROPERTIES)).toInstance(
		                Collections.<String, String>emptyMap());
				
				JSONConfiguration.natural().build();
			}
		});
	}

}
