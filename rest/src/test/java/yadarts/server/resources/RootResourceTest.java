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
package yadarts.server.resources;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import yadarts.server.guice.CoreModule;
import yadarts.server.guice.RestModule;

import com.google.inject.Inject;

@Guice(modules = {CoreModule.class, RestModule.class})
public class RootResourceTest {

	@Inject
	private RootResource res;
	
	@Test
	public void testResource() {
		Map<String, Object> result = res.startGame();
		
		Assert.assertEquals(result.get("success"), true);
		
		result = res.startGame();
		
		Assert.assertEquals(result.get("success"), false);
	}
	
}
