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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This document represents the knowledge index of domain object <code>SkillNode</code> in elasticsearch.
 */
@Document(indexName = EsConstants.INDEX_AUTOCOMPLETE, type = EsConstants.TYPE_KNOWLEDGES, shards = 1, replicas = 0)
public class EsAutocompleteKnowledge extends EsBaseAutocomplete {

    /**
     * Thi field is required to create an index for autocomplete service.
     */
    @CompletionField(payloads = true)
    protected Completion suggestKnowledge;

    public EsAutocompleteKnowledge() {
        super();
    }

    public EsAutocompleteKnowledge(String id, String name) {
        super(id, name);
    }

    public EsAutocompleteKnowledge(Map<String, Object> objectMap) {
        super(objectMap);
    }

    public EsAutocompleteKnowledge(String id, String name, Boolean withSuggest) {
        super(id, name);
        if (withSuggest) {
            Set<String> inputs = new HashSet<>();
            inputs.add(name);

            String plainName = name.replaceAll("[-()/.]", WHITESPACE_SEPARATOR);
            for (String value : plainName.split(COMMA_SEPARATOR)) {
                for (String input : value.split(WHITESPACE_SEPARATOR)) {
                    if (!input.isEmpty()) {
                        inputs.add(input);
                    }
                }
            }

            this.suggestKnowledge = new Completion(inputs.toArray(new String[inputs.size()]));
            Map<String, Object> payload = createPayload();
            this.suggestKnowledge.setPayload(payload);
        }
    }

    protected Map<String, Object> createPayload() {
        return createBasePayload();
    }

    public Completion getSuggestKnowledge() {
        return suggestKnowledge;
    }

    public void setSuggestKnowledge(Completion suggestKnowledge) {
        this.suggestKnowledge = suggestKnowledge;
    }
}
