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

import java.util.ArrayList;
import java.util.List;

/**
 * User professional  and individual skills.
 */
public class Skills {

    private List<SkillRatingNode> languages;

    private List<SkillRatingNode> personalStrengths;

    private List<SkillRatingNode> knowledges;

    public Skills() {
        setLanguages(new ArrayList<>());
        setPersonalStrengths(new ArrayList<>());
        setKnowledges(new ArrayList<>());
    }

    public List<SkillRatingNode> getLanguages() {
        return languages;
    }

    public void setLanguages(List<SkillRatingNode> languages) {
        this.languages = languages;
    }

    public List<SkillRatingNode> getPersonalStrengths() {
        return personalStrengths;
    }

    public void setPersonalStrengths(List<SkillRatingNode> personalStrengths) {
        this.personalStrengths = personalStrengths;
    }

    public List<SkillRatingNode> getKnowledges() {
        return knowledges;
    }

    public void setKnowledges(List<SkillRatingNode> knowledges) {
        this.knowledges = knowledges;
    }

    /**
     * This is a help methods to get all user skills in one list.
     *
     * @return list with all user skills.
     */
    public List<SkillRatingNode> getAllSkills() {
        List<SkillRatingNode> skillRatingNodes = new ArrayList<>();
        skillRatingNodes.addAll(getLanguages());
        skillRatingNodes.addAll(getKnowledges());
        skillRatingNodes.addAll(getPersonalStrengths());

        return skillRatingNodes;
    }
}
