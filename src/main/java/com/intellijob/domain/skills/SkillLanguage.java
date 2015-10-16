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

package com.intellijob.domain.skills;

import com.intellijob.domain.BaseDocument;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by Sergej Meister on 10/13/15.
 */
@Document(collection = "skill_languages")
public class SkillLanguage extends BaseDocument {

    /**
     *
     */
    @DBRef
    private SkillCategory category;

    private List<SkillNode> languages;

    public SkillCategory getCategory() {
        return category;
    }

    public void setCategory(SkillCategory category) {
        this.category = category;
    }

    public List<SkillNode> getLanguages() {
        return languages;
    }

    public void setLanguages(List<SkillNode> languages) {
        this.languages = languages;
    }
}
