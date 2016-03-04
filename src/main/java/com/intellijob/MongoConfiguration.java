/*
 * Copyright 2015 Sergej Meister
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intellijob;

import com.intellijob.exceptions.ApplicationRunException;
import com.mongodb.MongoClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * Embedded Mongo Configuration class.
 * <p>
 * Mongo Server is started with spring configuration!
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.intellijob.repository")
public class MongoConfiguration {

    private static final Logger LOG = Logger.getLogger(MongoConfiguration.class);

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.port}")
    private String port;

    @Value("${spring.data.mongodb.database}")
    private String database;

    @Bean(name = "mongoClient")
    public MongoClient mongoClient() throws IOException, ApplicationRunException {
        LOG.info("Execute mongo client!");
        MongoClient mongoClient = new MongoClient(host, Integer.parseInt(port));

        //To insert some json data!

        return mongoClient;
    }

    @PreDestroy
    public void shutdownEmbeddedMongoDB() {
        try {
            if (this.mongoClient() != null) {
                this.mongoClient().close();
            }
        } catch (IOException | ApplicationRunException e) {
            LOG.error("Mongo Client can not close connection!", e);
        }
    }
}
