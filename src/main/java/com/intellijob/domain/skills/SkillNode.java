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
import com.intellijob.domain.localization.LocalizableObject;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.ArrayList;
import java.util.List;

/**
 * Skill node model to represents skill document.
 */
public class SkillNode extends BaseDocument {

    @Indexed
    private String name;

    private String Description;

    private LocalizableObject localizableObject;

    private List<SkillNode> nodes;

    public SkillNode() {
        setNodes(new ArrayList<>());
    }

    public SkillNode(LocalizableObject localizableObject) {
        setLocalizableObject(localizableObject);
        setNodes(new ArrayList<>());
    }

    public SkillNode(ObjectId objectId, LocalizableObject localizableObject) {
        setId(objectId.toHexString());
        setLocalizableObject(localizableObject);
        setNodes(new ArrayList<>());
    }

    public SkillNode(String name, LocalizableObject localizableObject) {
        setId(new ObjectId().toHexString());
        setName(name);
        setLocalizableObject(localizableObject);
        setNodes(new ArrayList<>());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public List<SkillNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<SkillNode> nodes) {
        this.nodes = nodes;
    }

    public LocalizableObject getLocalizableObject() {
        return localizableObject;
    }

    public void setLocalizableObject(LocalizableObject localizableObject) {
        this.localizableObject = localizableObject;
    }
}
