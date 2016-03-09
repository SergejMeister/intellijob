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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.config.Storage;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Embedded Mongo Configuration class.
 * <p>
 * Start embedded server and load skill_data for FIRST application start"
 */
@Configuration
@ComponentScan
@EnableMongoRepositories(basePackages = "com.intellijob.repository")
public class MongoConfiguration extends MongoAutoConfiguration {

    public static final Integer DEFAULT_MONGO_PORT = 27017;

    public static final String DEFAULT_MONGO_HOST = "localhost";

    public static final String DEFAULT_MONGO_DATABASE = "test";

    private static final Logger LOG = LoggerFactory.getLogger(MongoConfiguration.class);

    /**
     * Include all default spring mongodb properties.
     */
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private MongoProperties properties;

    /**
     * replication to persist data from memory into file storage.
     */
    @Value("${spring.data.mongodb.replication.storage}")
    private String replicationStorage;

    /**
     * Production or test flag.
     */
    @Value("${spring.data.mongodb.production}")
    private boolean isProduction;


    /**
     * Override embeddedMongoConfiguration to set a replication storage.
     * <p>
     * This is required because there is no property to set replication storage!
     *
     * @return Mongo server configuration.
     * @throws IOException bad configurations.
     */
    @Bean
    @ConditionalOnMissingBean
    public IMongodConfig embeddedMongoConfiguration() throws IOException {
        if (this.properties.getPort() == null) {
            LOG.warn("Mongo Port is null. Default port {} will be seated!", DEFAULT_MONGO_PORT);
            this.properties.setPort(DEFAULT_MONGO_PORT);
        }

        if (this.properties.getHost() == null || this.properties.getHost().isEmpty()) {
            LOG.warn("Mongo Host is null or empty! Default host {} will be seated!", DEFAULT_MONGO_HOST);
            this.properties.setHost(DEFAULT_MONGO_HOST);
        }

        if (this.properties.getDatabase() == null || this.properties.getDatabase().isEmpty()) {
            LOG.warn("Mongo Database is null or empty! Default database {} will be seated!", DEFAULT_MONGO_DATABASE);
            this.properties.setDatabase(DEFAULT_MONGO_DATABASE);
        }

        Net net = new Net(this.properties.getHost(), this.properties.getPort(), Network.localhostIsIPv6());
        MongodConfigBuilder mongodConfigBuilder = new MongodConfigBuilder().version(Version.Main.PRODUCTION)
                .net(net).configServer(false);

        if (isProduction && replicationStorage != null && !replicationStorage.isEmpty()) {
            mongodConfigBuilder.replication(new Storage(replicationStorage, null, 0));
        }

        return mongodConfigBuilder.build();
    }

    @Autowired
    @Bean
    public Boolean doImportData(MongoClient mongoClient) throws IOException {

        DBCollection sys_import_collection = mongoClient
                .getDB(this.properties.getDatabase()).getCollection("sys_import");
        if (isProduction && sys_import_collection.count() == 0) {
            LOG.info("IMPORT DATA =============================================>");

            //Import collection skill_caegories.
            loadCollectionSkillCategories(mongoClient);

            //Import languages
            loadSkillsData(mongoClient, "skill_languages.json", "skill_languages");

            //Import knowledges
            loadSkillsData(mongoClient, "skill_knowledge.json", "skill_knowledges");

            //Import personal strength
            loadSkillsData(mongoClient, "skill_personalstrengths.json", "skill_personalstrengths");

            DBObject row = new BasicDBObject();
            row.put("Imported", 1);
            row.put("Date", LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));

            sys_import_collection.insert(row);
            LOG.info("IMPORT DATA FINISHED!");
            return true;
        }

        return false;
    }

    /**
     * Import collection skill_categories.
     *
     * @throws IOException exception.
     */
    private void loadCollectionSkillCategories(MongoClient mongoClient) {
        String collectionName = "skill_categories";
        try {
            LOG.info("LOAD {} DATA ....................................", collectionName);
            URL skillCategoriesURL = Thread.currentThread().getContextClassLoader()
                    .getResource("imports/skills_categories.json");

            DBCollection col = mongoClient.getDB(this.properties.getDatabase()).getCollection(collectionName);

            List<Map<String, Object>> categories =
                    new ObjectMapper().readValue(new File(skillCategoriesURL.getFile()), TypeFactory
                            .defaultInstance().constructCollectionType(List.class, HashMap.class));

            for (Map<String, Object> category : categories) {
                DBObject dbObject = new BasicDBObject(category);
                dbObject.put("_id", new ObjectId(category.get("_id").toString()));
                col.insert(dbObject);
            }
            LOG.info("DONE!");
        } catch (Exception e) {
            LOG.error("Collection (" + collectionName + ") could not be imported successfully!", e);
        }
    }

    /**
     * Import supported skill data.
     *
     * @throws IOException exception.
     */
    private void loadSkillsData(MongoClient mongoClient, String jsonFile, String collectionName) {
        try {
            LOG.info("LOAD {} DATA .........................................", collectionName);
            URL jsonUrl = Thread.currentThread().getContextClassLoader()
                    .getResource("imports/" + jsonFile);

            DBCollection col = mongoClient.getDB(this.properties.getDatabase()).getCollection(collectionName);

            TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
            };
            Map<String, Object> mapData = new ObjectMapper().readValue(new File(jsonUrl.getFile()), typeRef);

            setObjectIdRecursive(mapData);
            DBObject dbObject = new BasicDBObject(mapData);
            col.insert(dbObject);
            LOG.info("DONE!");
        } catch (Exception e) {
            LOG.error("Collection (" + collectionName + ") could not be imported successfully!", e);
        }
    }

    private void setObjectIdRecursive(Map<String, Object> mapObject) {
        if (mapObject.containsKey("_id")) {
            String objectId = ((Map) mapObject.get("_id")).get("$oid").toString();
            mapObject.put("_id", new ObjectId(objectId));
        }

        //set category reference.
        if (mapObject.containsKey("category")) {
            Map<String, Object> categoryRef = (Map) mapObject.get("category");
            String refNamespace = categoryRef.get("$ref").toString();
            String objectId = ((Map) categoryRef.get("$id")).get("$oid").toString();
            mapObject.put("category", new DBRef(refNamespace, new ObjectId(objectId)));
        }

        for (Object object : mapObject.values()) {
            if (object instanceof Map) {
                Map<String, Object> innerMap = (Map) object;
                setObjectIdRecursive(innerMap);
            }

            if (object instanceof List) {
                List<Map<String, Object>> innerListMap = (List) object;
                innerListMap.forEach(this::setObjectIdRecursive);
            }
        }
    }
}
