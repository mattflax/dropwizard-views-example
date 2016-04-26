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
package io.dropwizard.views.mustache;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.github.mustachejava.MustacheResolver;
import com.github.mustachejava.resolver.DefaultResolver;
import com.google.common.base.Charsets;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableSet;
import io.dropwizard.views.View;
import uk.co.elysian.views.MultiLocationViewRenderer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Multi-location variant of the Mustache View Renderer.
 *
 * Created by mlp on 26/04/16.
 */
public class MustacheMultiLocationViewRenderer extends MustacheViewRenderer implements MultiLocationViewRenderer {

	private final FactoryLoader loader;

	private static class FactoryLoader extends CacheLoader<Class<? extends View>, MustacheFactory> {
		private Set<String> locationPaths = ImmutableSet.of();

		@Override
		public MustacheFactory load(Class<? extends View> key) throws Exception {
			final MustacheFactory factory;

			if (locationPaths.isEmpty()) {
				// Use default - per-class factory
				factory = new DefaultMustacheFactory(new PerClassMustacheResolver(key));
			} else {
				// Work out how to load template
				List<MustacheResolver> resolvers = new ArrayList<>(locationPaths.size());
				for (String path : locationPaths) {
					MustacheResolver resolver;
					if (CLASSPATH_LOCATION.equals(path)) {
						resolver = new PerClassMustacheResolver(key);
					} else {
						resolver = new DefaultResolver(path);
					}
					resolvers.add(resolver);
				}
				factory = new DefaultMustacheFactory(new MustacheMultiLocationResolver(ImmutableSet.copyOf(resolvers)));
			}

			return factory;
		}

		void setLocationPaths(Set<String> locationPaths) {
			this.locationPaths = locationPaths;
		}
	}

	private final LoadingCache<Class<? extends View>, MustacheFactory> factoryCache;

	public MustacheMultiLocationViewRenderer() {
		this.loader = new FactoryLoader();
		this.factoryCache = CacheBuilder.newBuilder()
				.concurrencyLevel(128)
				.build(loader);
	}

	@Override
	public void render(View view, Locale locale, OutputStream output) throws IOException {
		try {
			final Mustache template = factoryCache.get(view.getClass())
					.compile(view.getTemplateName());
			final Charset charset = view.getCharset().or(Charsets.UTF_8);
			try (OutputStreamWriter writer = new OutputStreamWriter(output, charset)) {
				template.execute(writer, view);
			}
		} catch (Throwable e) {
			throw new RuntimeException("Mustache template error: " + view.getTemplateName(), e);
		}
	}

	@Override
	public void configureLocations(Set<String> locations) {
		this.loader.setLocationPaths(locations);
	}

}
