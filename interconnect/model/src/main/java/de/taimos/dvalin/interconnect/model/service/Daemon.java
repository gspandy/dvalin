package de.taimos.dvalin.interconnect.model.service;

/*
 * #%L
 * Dvalin interconnect transfer data model
 * %%
 * Copyright (C) 2016 Taimos GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The @Daemon annotation is used with interfaces that extend {@link IDaemon} to provide information that clients can interact with that
 * daemon.<br>
 * This information are:<br>
 * - The name of the JMS queue ${name}.request<br>
 * - some {@link DaemonRequestMethod}<br>
 * - some {@link DaemonReceiverMethod}<br>
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Daemon {

	/**
	 * @return Daemon name
	 */
    String name();

}
