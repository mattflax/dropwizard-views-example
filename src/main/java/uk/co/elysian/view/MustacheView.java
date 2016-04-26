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
package uk.co.elysian.view;

import io.dropwizard.views.View;

/**
 * Created by mlp on 26/04/16.
 */
public class MustacheView extends View {

	public static final String TEMPLATE_NAME = "page.mustache";

	public MustacheView() {
		super(TEMPLATE_NAME);
	}

	public String getViewClass() {
		return MustacheView.class.getName();
	}

}
