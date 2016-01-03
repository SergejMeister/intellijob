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
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = EsConstants.INDEX_INTELLIJOB, type = EsConstants.TYPE_USER_SKILLS, shards = 1, replicas = 0)
public class EsUserSkills extends EsBaseDocument {

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String userId;

    @Field(
            type = FieldType.String,
            index = FieldIndex.analyzed,
            store = true
    )
    private String name;

    private float rating;

    @Field(
            type = FieldType.Boolean,
            index = FieldIndex.analyzed
    )
    private boolean parent;


    public EsUserSkills() {
    }

    public EsUserSkills(String id) {
        super(id);
    }

    public EsUserSkills(String id, String userId) {
        super(id);
        setUserId(userId);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void increaseRating(float value) {
        this.rating = this.rating + value;
    }

    public boolean isParent() {
        return parent;
    }

    public void setParent(boolean parent) {
        this.parent = parent;
    }
}
