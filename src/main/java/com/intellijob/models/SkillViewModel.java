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

package com.intellijob.models;

import com.intellijob.domain.skills.SkillNode;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a model to represent all supported skills.
 */
public class SkillViewModel {

    private List<SkillNode> languages;

    public SkillViewModel() {
        setLanguages(new ArrayList<>());
    }

    public List<SkillNode> getLanguages() {
        return languages;
    }

    public void setLanguages(List<SkillNode> languages) {
        this.languages = languages;
    }
}
