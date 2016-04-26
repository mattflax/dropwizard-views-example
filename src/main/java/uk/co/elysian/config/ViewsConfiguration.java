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
package uk.co.elysian.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuration for the extended views bundle.
 *
 * Created by mlp on 21/04/16.
 */
public class ViewsConfiguration {

	@JsonProperty("renderers")
	private Map<String, Map<String, String>> renderers = new HashMap<>();

	@JsonProperty("templatePaths")
	private List<String> templatePaths;


	public Map<String, Map<String, String>> getRenderers() {
		return renderers;
	}

	public void setRenderers(Map<String, Map<String, String>> renderers) {
		this.renderers = renderers;
	}

	public List<String> getTemplatePaths() {
		return templatePaths;
	}

	public void setTemplatePaths(List<String> templatePaths) {
		this.templatePaths = templatePaths;
	}

}
