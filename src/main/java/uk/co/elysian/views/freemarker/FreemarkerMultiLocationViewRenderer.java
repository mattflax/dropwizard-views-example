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
package uk.co.elysian.views.freemarker;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.template.*;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.views.View;
import io.dropwizard.views.freemarker.FreemarkerViewRenderer;
import uk.co.elysian.views.MultiLocationViewRenderer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Multi-location variant of the FreemarkerViewRenderer.
 * <p>
 * Created by mlp on 26/04/16.
 */
public class FreemarkerMultiLocationViewRenderer extends FreemarkerViewRenderer implements MultiLocationViewRenderer {

	private static final Version FREEMARKER_VERSION = Configuration.getVersion();
	private final FreemarkerMultiLocationViewRenderer.TemplateLoader loader;

	private static class TemplateLoader extends CacheLoader<Class<?>, Configuration> {
		private Map<String, String> baseConfig = ImmutableMap.of();
		private Set<String> locationPaths = ImmutableSet.of();

		@Override
		public Configuration load(Class<?> key) throws Exception {
			final Configuration configuration = new Configuration(FREEMARKER_VERSION);
			configuration.setObjectWrapper(new DefaultObjectWrapperBuilder(FREEMARKER_VERSION).build());
			configuration.loadBuiltInEncodingMap();
			configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
			for (Map.Entry<String, String> entry : baseConfig.entrySet()) {
				configuration.setSetting(entry.getKey(), entry.getValue());
			}
			if (locationPaths.isEmpty()) {
				// Use default locations - load from classpath
				configuration.setClassForTemplateLoading(key, "/");
			} else {
				// Build a list of template loaders for the configured locations
				List<freemarker.cache.TemplateLoader> loaders = new ArrayList<>(locationPaths.size());
				for (String path : locationPaths) {
					final freemarker.cache.TemplateLoader loader;
					if (path.equals(CLASSPATH_LOCATION)) {
						loader = new ClassTemplateLoader(key, "/");
					} else {
						if (path.startsWith("/")) {
							// Absolute path
							loader = new FileTemplateLoader(new File(path));
						} else {
							// Relative path - bad idea, hopefully just a test class
							loader = new FileTemplateLoader(new File(ResourceHelpers.resourceFilePath(path)));
						}
					}
					loaders.add(loader);
				}
				freemarker.cache.TemplateLoader multiLoader = new MultiTemplateLoader(
						loaders.toArray(new freemarker.cache.TemplateLoader[loaders.size()]));
				configuration.setTemplateLoader(multiLoader);
			}
			return configuration;
		}

		void setBaseConfig(Map<String, String> baseConfig) {
			this.baseConfig = baseConfig;
		}

		void setLocationPaths(Set<String> locationPaths) {
			this.locationPaths = locationPaths;
		}
	}

	private final LoadingCache<Class<?>, Configuration> configurationCache;

	public FreemarkerMultiLocationViewRenderer() {
		this.loader = new FreemarkerMultiLocationViewRenderer.TemplateLoader();
		this.configurationCache = CacheBuilder.newBuilder()
				.concurrencyLevel(128)
				.build(loader);
	}

	@Override
	public void render(View view,
					   Locale locale,
					   OutputStream output) throws IOException {
		try {
			final Configuration configuration = configurationCache.getUnchecked(view.getClass());
			final Charset charset = view.getCharset().or(() -> Charset.forName(configuration.getEncoding(locale)));
			final Template template = configuration.getTemplate(view.getTemplateName(), locale, charset.name());
			template.process(view, new OutputStreamWriter(output, template.getEncoding()));
		} catch (TemplateException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void configure(Map<String, String> baseConfig) {
		this.loader.setBaseConfig(baseConfig);
	}

	@Override
	public void configureLocations(Set<String> locations) {
		this.loader.setLocationPaths(locations);
	}
}
