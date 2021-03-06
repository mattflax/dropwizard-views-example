/**
 * Copyright (c) 2016 Elysian Software Limited.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.co.elysian.resources;

import org.junit.Before;
import org.junit.Test;
import uk.co.elysian.view.IndexView;

import static org.junit.Assert.assertNotNull;

/**
 * Created by mlp on 07/04/16.
 */
public class IndexResourceTest {

	private IndexResource resource;

	@Before
	public void setup() {
		this.resource = new IndexResource();
	}

	@Test
	public void handleGet() {
		IndexView view = resource.handleGet();
		assertNotNull(view);
	}

}
