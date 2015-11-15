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

package com.intellijob.dto;

import com.intellijob.domain.skills.SkillNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Data
 */
public class SkillData implements Serializable {

    private String id;

    private String name;

    private List<SkillData> skills;

    public SkillData() {
    }

    public SkillData(String id, String name) {
        setId(id);
        setName(name);
    }

    public SkillData(SkillNode skillNode) {
        setId(skillNode.getId());
        if(skillNode.getLocalizableObject() != null){
            setName(skillNode.getLocalizableObject().getLabel());
        }else{
            setName(skillNode.getName());
        }
        this.skills = initSubSkills(skillNode.getNodes());
    }

    private List<SkillData> initSubSkills(List<SkillNode> nodes) {
        if (nodes.isEmpty()) {
            return Collections.emptyList();
        }

        List<SkillData> result = new ArrayList<>();
        for (SkillNode skillNode : nodes) {
            SkillData skillData = new SkillData(skillNode.getId(), skillNode.getLocalizableObject().getLabel());
            List<SkillData> skills = initSubSkills(skillNode.getNodes());
            skillData.setSkills(skills);
            result.add(skillData);
        }
        return result;
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

    public List<SkillData> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillData> skills) {
        this.skills = skills;
    }
}
