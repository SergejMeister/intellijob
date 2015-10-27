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

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Model to represents all skill knowledge.
 */
@Document(collection = "skill_knowledges")
public class SkillKnowledge extends SkillRoot {

    private List<SkillNode> knowledges;

    public SkillKnowledge() {
    }

    public SkillKnowledge(SkillCategory skillCategory) {
        super(skillCategory);
    }



    public List<SkillNode> getKnowledges() {
        return knowledges;
    }

    public void setKnowledges(List<SkillNode> knowledges) {
        this.knowledges = knowledges;
    }
}
