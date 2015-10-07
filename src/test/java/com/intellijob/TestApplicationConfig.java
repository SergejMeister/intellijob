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
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;

@ActiveProfiles("test")
@ContextConfiguration(classes = {ApplicationConfig.class})
@ComponentScan
public class TestApplicationConfig {

    /**
     * Constants.
     */
    public static final Boolean LIVE_MONGODB = Boolean.TRUE;
    private static final Logger LOG = Logger.getLogger(TestApplicationConfig.class);
    private static final String MONGO_DB_NAME = "db_intellijob";
    private static final String MONGO_LOCALHOST = "localhost";
    private static final Integer MONGO_DB_PORT = 27017;
    private static final MongodStarter starter = MongodStarter.getDefaultInstance();

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private MongoProperties properties;

    @Autowired(required = false)
    private MongoClientOptions options;

    @Bean(destroyMethod = "close")
    public Mongo mongo() throws IOException {
        if(LIVE_MONGODB){
            properties.setDatabase(MONGO_DB_NAME);
            properties.setHost(MONGO_LOCALHOST);
            properties.setPort(MONGO_DB_PORT);
        }else{
            Net net = mongod().getConfig().net();
            properties.setHost(net.getServerAddress().getHostName());
            properties.setPort(net.getPort());
        }

        return properties.createMongoClient(this.options);
    }

    @Bean(destroyMethod = "stop")
    public MongodProcess mongod() throws IOException {
        return mongodExe().start();
    }

    @Bean(destroyMethod = "stop")
    public MongodExecutable mongodExe() throws IOException {
        return starter.prepare(mongodConfig());
    }

    @Bean
    public IMongodConfig mongodConfig() throws IOException {
        return new MongodConfigBuilder().version(Version.Main.PRODUCTION).build();
    }
}
