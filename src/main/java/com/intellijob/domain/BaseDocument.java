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

package com.intellijob.domain;

import org.springframework.data.annotation.Id;

/**
 * Base document to represent default data for all documents.
 */
public abstract class BaseDocument {

    @Id
    private String id;

    protected BaseDocument() {
    }

    protected BaseDocument(String id) {
        setId(id);
    }

    /**
     * Returns id.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     */
    public void setId(String id) {
        this.id = id;
    }
}
