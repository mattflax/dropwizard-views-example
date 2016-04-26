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
package uk.co.elysian.views;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.sun.org.apache.xpath.internal.operations.Mult;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import io.dropwizard.views.ViewConfigurable;
import io.dropwizard.views.ViewMessageBodyWriter;
import io.dropwizard.views.ViewRenderer;
import uk.co.elysian.config.MultiLocationViewConfiguration;
import uk.co.elysian.config.ViewsConfiguration;

import java.util.Collections;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

import static com.google.common.base.MoreObjects.firstNonNull;

/**
 * Extension to the ViewBundle class allowing for templates to be located
 * in multiple locations.
 * <p>
 * Created by mlp on 26/04/16.
 */
public class MultiLocationViewBundle<T extends MultiLocationViewConfiguration> implements ConfiguredBundle<T>, ViewConfigurable<T> {

	private final Iterable<ViewRenderer> viewRenderers;

	public MultiLocationViewBundle() {
		this(ServiceLoader.load(MultiLocationViewRenderer.class));
	}

	public MultiLocationViewBundle(Iterable<MultiLocationViewRenderer> viewRenderers) {
		this.viewRenderers = ImmutableSet.copyOf(viewRenderers);
	}

	@Override
	public Map<String, Map<String, String>> getViewConfiguration(T configuration) {
		return configuration.getViews().getRenderers();
	}

	@Override
	public void run(T configuration, Environment environment) throws Exception {
		final Map<String, Map<String, String>> options = getViewConfiguration(configuration);
		final Set<String> locationPaths = ImmutableSet.copyOf(configuration.getViews().getTemplatePaths());
		for (ViewRenderer viewRenderer : viewRenderers) {
			final Map<String, String> viewOptions = options.get(viewRenderer.getSuffix());
			viewRenderer.configure(firstNonNull(viewOptions, Collections.<String, String>emptyMap()));
			((MultiLocationViewRenderer) viewRenderer).configureLocations(locationPaths);
		}
		environment.jersey().register(new ViewMessageBodyWriter(environment.metrics(), viewRenderers));
	}

	@Override
	public void initialize(Bootstrap<?> bootstrap) {
		// nothing doing
	}


}
