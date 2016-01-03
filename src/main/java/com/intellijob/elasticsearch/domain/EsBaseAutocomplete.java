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

package com.intellijob.elasticsearch.domain;

import com.intellijob.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * The abstract base class for all autocomplete documents.
 */
public abstract class EsBaseAutocomplete extends EsBaseDocument {

    public static final String WHITESPACE_SEPARATOR = " ";
    public static final String COMMA_SEPARATOR = ",";

    public EsBaseAutocomplete() {
        super();
    }

    public EsBaseAutocomplete(String id) {
        super(id);
    }

    public EsBaseAutocomplete(Map<String, Object> objectMap) {
        if (objectMap != null) {
            if (objectMap.containsKey(Constants.DB_FIELD_ID)) {
                setId((String) objectMap.get(Constants.DB_FIELD_ID));
            }
        }
    }

    protected Map<String, Object> createBasePayload() {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put(Constants.DB_FIELD_ID, id);

        return objectMap;
    }
}
