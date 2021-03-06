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
package uk.co.elysian;

import com.codahale.metrics.health.HealthCheckRegistry;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Environment;
import org.junit.Before;
import org.junit.Test;
import uk.co.elysian.resources.IndexResource;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by mlp on 07/04/16.
 */
public class DropwizardViewsApplicationTest {

	private final Environment environment = mock(Environment.class);
	private final JerseyEnvironment jersey = mock(JerseyEnvironment.class);
	private final HealthCheckRegistry healthChecks = mock(HealthCheckRegistry.class);
	private final DropwizardViewsApplication application = new DropwizardViewsApplication();
	private final DropwizardViewsConfiguration config = new DropwizardViewsConfiguration();

	@Before
	public void setup() throws Exception {
		when(environment.jersey()).thenReturn(jersey);
		when(environment.healthChecks()).thenReturn(healthChecks);
	}

	@Test
	public void buildsIndexResource() throws Exception {
		application.run(config, environment);

		verify(jersey).register(isA(IndexResource.class));
	}

}
