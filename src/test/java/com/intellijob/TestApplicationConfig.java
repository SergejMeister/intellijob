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


import com.mongodb.Mongo;
import com.mongodb.MongoClientOptions;
import de.flapdoodle.embed.mongo.MongoImportExecutable;
import de.flapdoodle.embed.mongo.MongoImportStarter;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongoImportConfig;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongoImportConfigBuilder;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.net.URL;

@ActiveProfiles("test")
@ContextConfiguration(classes = {ApplicationConfig.class})
@ComponentScan
public class TestApplicationConfig {

    /**
     * Constants.
     */
    public static final Boolean LIVE_MONGODB = Boolean.FALSE;
    private static final Logger LOG = LoggerFactory.getLogger(TestApplicationConfig.class);
    private static final String MONGO_DB_NAME = "db_intellijob";
    private static final String MONGO_DB_NAME_TEST = "test";
    private static final String MONGO_LOCALHOST = "localhost";
    private static final Integer MONGO_DB_PORT = 27017;
    private static final Integer MONGO_DB_PORT_TEST = 45678;
    private static Net net;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private MongoProperties properties;

    @Autowired(required = false)
    private MongoClientOptions options;

    /**
     * Reload collection skill_categories.
     * <p>
     * Drop collection if exist, create a new collection and load data.
     * Read data from skill_categories.json
     *
     * @throws IOException exception.
     */
    public static void reloadCollectionSkillCategories() throws IOException {
        URL skillCategoriesURL = Thread.currentThread().getContextClassLoader()
                .getResource("imports/skill_categories.json");
        IMongoImportConfig mongoImportConfig = new MongoImportConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(net)
                .db(MONGO_DB_NAME_TEST)
                .collection("skill_categories")
                .upsert(true)
                .dropCollection(true)
                .jsonArray(true)
                .importFile(skillCategoriesURL.getPath())
                .build();

        MongoImportExecutable mongoImportExecutable =
                MongoImportStarter.getDefaultInstance().prepare(mongoImportConfig);
        mongoImportExecutable.start();
    }

    private static void loadCollections() throws IOException {
        reloadCollectionSkillCategories();
    }

    /**
     * Mongo embedded client.
     */
    @Bean(destroyMethod = "close")
    public Mongo mongo() throws IOException {
        if (LIVE_MONGODB) {
            properties.setDatabase(MONGO_DB_NAME);
            properties.setHost(MONGO_LOCALHOST);
            properties.setPort(MONGO_DB_PORT);
        } else {
            net = mongod().getConfig().net();
            properties.setHost(net.getServerAddress().getHostName());
            properties.setPort(net.getPort());
        }

        LOG.info("Mongo client is running on host {} port {}", net.getServerAddress().getHostName(), net.getPort());
        Mongo client = properties.createMongoClient(this.options);
        loadCollections();
        return client;
    }

    /**
     * Mongo embedded Server.
     */
    @Bean(destroyMethod = "stop")
    public MongodProcess mongod() throws IOException {
        IMongodConfig mongoConfig = new MongodConfigBuilder().version(Version.Main.PRODUCTION).build();
        MongodExecutable mongodExecutable = MongodStarter.getDefaultInstance().prepare(mongoConfig);
        return mongodExecutable.start();
    }

}
