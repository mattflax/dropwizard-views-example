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

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import uk.co.elysian.health.DummyHealthCheck;
import uk.co.elysian.resources.IndexResource;
import uk.co.elysian.resources.SourceResource;
import uk.co.elysian.views.MultiLocationViewBundle;

import java.util.Map;

/**
 * Created by mlp on 07/04/16.
 */
public class DropwizardViewsApplication extends Application<DropwizardViewsConfiguration> {

	@Override
	public void initialize(Bootstrap<DropwizardViewsConfiguration> bootstrap) {
		// Add the views bundle, with override to pass optional additional config
		bootstrap.addBundle(new MultiLocationViewBundle<>());
	}

	@Override
	public void run(DropwizardViewsConfiguration dropwizardViewsConfiguration, Environment environment) throws Exception {
		environment.jersey().register(new IndexResource());
		environment.jersey().register(new SourceResource());

		environment.healthChecks().register("dummy", new DummyHealthCheck());
	}

	public static void main(String[] args) throws Exception {
		new DropwizardViewsApplication().run(args);
	}

}
