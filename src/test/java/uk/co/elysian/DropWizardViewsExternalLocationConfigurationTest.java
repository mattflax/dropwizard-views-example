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

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test the DW views example application using a configuration,
 * with an external location for index.ftl.
 *
 * Created by mlp on 12/04/16.
 */
public class DropWizardViewsExternalLocationConfigurationTest {

	static final String CONFIG_FILE = "config/external_location_config.yml";
	static final String CHECK_STRING = "templates/freemarker/index.ftl";
	static final String LOCATION = "/index.ftl";

	@ClassRule
	public static final DropwizardAppRule<DropwizardViewsConfiguration> RULE =
			new DropwizardAppRule<>(DropwizardViewsApplication.class, ResourceHelpers.resourceFilePath(CONFIG_FILE));

	@Test
	public void indexViewReturnedFromDefault() {
		Client client = new JerseyClientBuilder().build();

		Response response = client.target(
				String.format("http://localhost:%d/source?path=%s", RULE.getLocalPort(), LOCATION))
				.request()
				.get();

		String responseBody = response.readEntity(String.class);
		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(responseBody).contains(CHECK_STRING);

		// Verify that regular index resource works
		response = client.target(
				String.format("http://localhost:%d/", RULE.getLocalPort()))
				.request()
				.get();

		responseBody = response.readEntity(String.class);
		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(responseBody).contains(DropWizardViewsBasicConfigurationTest.CHECK_STRING);

	}

}
