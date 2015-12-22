package de.taimos.dao.mongo;

/*
 * #%L
 * Spring DAO Mongo
 * %%
 * Copyright (C) 2013 - 2015 Taimos GmbH
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


import org.springframework.beans.factory.FactoryBean;

import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;

/**
 *
 * Copyright 2014 Hoegernet<br>
 * <br>
 * Special factory for MongoClientOptions which is setting timeouts
 *
 * @author Thorsten Hoeger
 *
 */
public class MongoClientOptionsFactory implements FactoryBean<MongoClientOptions.Builder> {
	
	private int socketTimeout;
	private int connectTimeout;
	
	
	@Override
	public MongoClientOptions.Builder getObject() throws Exception {
		Builder builder = MongoClientOptions.builder();
		builder.socketTimeout(this.socketTimeout);
		builder.connectTimeout(this.connectTimeout);
		return builder;
	}
	
	@Override
	public Class<?> getObjectType() {
		return MongoClientOptions.Builder.class;
	}
	
	@Override
	public boolean isSingleton() {
		return false;
	}
	
	/**
	 * @param socketTimeout the socketTimeout to set
	 */
	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}
	
	/**
	 * @param connectTimeout the connectTimeout to set
	 */
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	
}
