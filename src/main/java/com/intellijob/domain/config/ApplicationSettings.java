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

package com.intellijob.domain.config;

import com.intellijob.domain.BaseDocument;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Domain object for all application settings.
 */
@Document(collection = "app_settings")
public class ApplicationSettings extends BaseDocument {

    public static final String COLLECTION_NAME = "app_settings";
    public static final String FIELD_MONGO_DATA_IMPORTED = "mongoDataImported";
    public static final String FIELD_MONGO_DATA_IMPORTED_DATE = "mongoDataImportedDate";
    public static final String FIELD_ELASTIC_DATA_IMPORTED = "elasticDataImported";
    public static final String FIELD_ELASTIC_DATA_IMPORTED_DATE = "elasticDataImportedDate";

    private boolean mongoDataImported;

    private Date mongoDataImportedDate;

    private boolean elasticDataImported;

    private Date elasticDataImportedDate;

    public ApplicationSettings() {
    }

    public ApplicationSettings(boolean mongoDataImported, boolean elasticDataImported) {
        setMongoDataImported(mongoDataImported);
        setElasticDataImported(elasticDataImported);
        if (mongoDataImported) {
            setMongoDataImportedDate(new Date());
        }

        if (elasticDataImported) {
            setElasticDataImportedDate(new Date());
        }
    }

    public Boolean getMongoDataImported() {
        return mongoDataImported;
    }

    public void setMongoDataImported(Boolean mongoDataImported) {
        this.mongoDataImported = mongoDataImported;
    }

    public Date getMongoDataImportedDate() {
        return mongoDataImportedDate;
    }

    public void setMongoDataImportedDate(Date mongoDataImportedDate) {
        this.mongoDataImportedDate = mongoDataImportedDate;
    }

    public Boolean getElasticDataImported() {
        return elasticDataImported;
    }

    public void setElasticDataImported(Boolean elasticDataImported) {
        this.elasticDataImported = elasticDataImported;
    }

    public Date getElasticDataImportedDate() {
        return elasticDataImportedDate;
    }

    public void setElasticDataImportedDate(Date elasticDataImportedDate) {
        this.elasticDataImportedDate = elasticDataImportedDate;
    }
}
