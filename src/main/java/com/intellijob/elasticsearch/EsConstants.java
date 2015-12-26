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

package com.intellijob.elasticsearch;

/**
 * Constants for elastic search.
 */
public final class EsConstants {


    /**
     * Index represents a database name in elasticsearch.
     */
    public static final String INDEX_INTELLIJOB = "intellijob";

    /**
     * Index represents a database name in elasticsearch.
     * <p>
     * This index include data only for  autocomplete fields.
     */
    public static final String INDEX_AUTOCOMPLETE = "autocomplete";

    /**
     * Type represents a collection name in elasticsearch.
     */
    public static final String TYPE_JOB_DETAILS = "jobDetails";

    /**
     * Type represents a collection name in elasticsearch.
     */
    public static final String TYPE_LANGUAGES = "languages";

    /**
     * Type represents a collection name in elasticsearch.
     */
    public static final String TYPE_KNOWLEDGES = "knowledges";

    public static final String FIELD_SUGGEST_KNOWLEDGE = "suggestKnowledge";

    public static final String FIELD_SUGGEST_LANGUAGE = "suggestLanguage";

    private EsConstants() {
    }

}
