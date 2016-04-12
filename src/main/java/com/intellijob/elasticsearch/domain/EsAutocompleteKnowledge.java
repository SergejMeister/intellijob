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
import com.intellijob.elasticsearch.EsConstants;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.completion.Completion;

import java.util.Map;

/**
 * This document represents the knowledge index of domain object <code>SkillNode</code> in elasticsearch.
 */
@Document(indexName = EsConstants.INDEX_AUTOCOMPLETE, type = EsConstants.TYPE_KNOWLEDGES, shards = 1, replicas = 0)
public class EsAutocompleteKnowledge extends EsBaseAutocomplete {

    @Field(
            type = FieldType.String,
            index = FieldIndex.analyzed,
            searchAnalyzer = "simple",
            indexAnalyzer = "simple",
            store = true
    )
    protected String name;

//    /**
//     * Use simple index analyze for suggestion query.
//     */
//    @Field(
//            type = FieldType.String,
//            index = FieldIndex.analyzed,
//            searchAnalyzer = "simple",
//            indexAnalyzer = "simple",
//            store = true
//    )
//    protected String nameSimple;

    /**
     * Thi field is required to create an index for autocomplete service.
     */
    @CompletionField(payloads = true)
    protected Completion suggestKnowledge;

    public EsAutocompleteKnowledge() {
    }

    public EsAutocompleteKnowledge(Map<String, Object> objectMap) {
        super(objectMap);
        if (objectMap.containsKey(Constants.DB_FIELD_NAME)) {
            setName((String) objectMap.get(Constants.DB_FIELD_NAME));
        }
    }

    public EsAutocompleteKnowledge(String id, String name, Boolean withSuggest) {
        super(id);
        setName(name);
        if (withSuggest) {
            //setNameSimple(name);
//            Set<String> inputs = new HashSet<>();
//            inputs.add(name);
//
//            String plainName = name.replaceAll("[-()/.]", WHITESPACE_SEPARATOR);
//            for (String value : plainName.split(COMMA_SEPARATOR)) {
//                for (String input : value.split(WHITESPACE_SEPARATOR)) {
//                    if (!input.isEmpty()) {
//                        inputs.add(input);
//                    }
//                }
//            }

//            this.suggestKnowledge = new Completion(inputs.toArray(new String[inputs.size()]));
            this.suggestKnowledge =
                    new Completion(name.replaceAll("[-()/.]", WHITESPACE_SEPARATOR).split(WHITESPACE_SEPARATOR));
            Map<String, Object> payload = createPayload();
            this.suggestKnowledge.setPayload(payload);
        }
    }

    protected Map<String, Object> createPayload() {
        Map<String, Object> payload = createBasePayload();
        payload.put(Constants.DB_FIELD_NAME, name);
        return payload;
    }

    public Completion getSuggestKnowledge() {
        return suggestKnowledge;
    }

    public void setSuggestKnowledge(Completion suggestKnowledge) {
        this.suggestKnowledge = suggestKnowledge;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public String getNameSimple() {
//        return nameSimple;
//    }
//
//    public void setNameSimple(String nameSimple) {
//        this.nameSimple = nameSimple;
//    }
}
