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

import com.intellijob.elasticsearch.EsConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.completion.Completion;

/**
 * This document represents the language index of domain object <code>SkillNode</code> in elasticsearch.
 */
@Document(indexName = EsConstants.INDEX_AUTOCOMPLETE, type = EsConstants.TYPE_LANGUAGES, shards = 1, replicas = 0)
public class EsAutocompleteLanguage {

    public static final String SEPARATOR = " ";

    /**
     * The id of SkillNode id.
     */
    @Id
    private String id;

    private String name;

    /**
     * Thi field is required to create an index for autocomplete service.
     */
    @CompletionField(payloads = true)
    private Completion suggest;

    public EsAutocompleteLanguage() {
    }

    public EsAutocompleteLanguage(String id, String name) {
        this(id, name, Boolean.FALSE);
    }

    public EsAutocompleteLanguage(String id, String name, Boolean withSuggest) {
        setId(id);
        setName(name);
        if (withSuggest) {
            this.suggest = new Completion(name.split(SEPARATOR));
            this.suggest.setPayload(id);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Completion getSuggest() {
        return suggest;
    }

    public void setSuggest(Completion suggest) {
        this.suggest = suggest;
    }
}
