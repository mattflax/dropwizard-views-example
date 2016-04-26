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

import io.dropwizard.views.ViewRenderer;

import java.util.Set;

/**
 * Extension to the base ViewRenderer interface to allow Views to be
 * rendered from multiple locations. Templates will be looked for in
 * the location order given, and the first found will be used.
 *
 * Created by mlp on 26/04/16.
 */
public interface MultiLocationViewRenderer extends ViewRenderer {

	static final String DEFAULT_LOCATION = "default";
	static final String CLASSPATH_LOCATION = "classpath";

	/**
	 * Configure the locations from which templates may be read.
	 * @param locations the set of template locations.
	 */
	void configureLocations(Set<String> locations);

}
