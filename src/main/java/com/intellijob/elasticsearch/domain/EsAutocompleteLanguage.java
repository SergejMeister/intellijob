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
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.completion.Completion;

import java.util.Map;

/**
 * This document represents the language index of domain object <code>SkillNode</code> in elasticsearch.
 */
@Document(indexName = EsConstants.INDEX_AUTOCOMPLETE, type = EsConstants.TYPE_LANGUAGES, shards = 1, replicas = 0)
public class EsAutocompleteLanguage extends EsBaseAutocomplete {

    /**
     * Thi field is required to create an index for autocomplete service.
     */
    @CompletionField(payloads = true)
    protected Completion suggestLanguage;

    public EsAutocompleteLanguage() {
    }

    public EsAutocompleteLanguage(String id, String name) {
        super(id, name);
    }

    public EsAutocompleteLanguage(String id, String name, Boolean withSuggest) {
        super(id, name);
        if (withSuggest) {
            this.suggestLanguage = new Completion(name.split(WHITESPACE_SEPARATOR));
            Map<String, Object> payload = createPayload();
            this.suggestLanguage.setPayload(payload);
        }
    }

    public EsAutocompleteLanguage(Map<String, Object> objectMap) {
        super(objectMap);
    }

    protected Map<String, Object> createPayload() {
        return createBasePayload();
    }

    public Completion getSuggestLanguage() {
        return suggestLanguage;
    }

    public void setSuggestLanguage(Completion suggestLanguage) {
        this.suggestLanguage = suggestLanguage;
    }
}
