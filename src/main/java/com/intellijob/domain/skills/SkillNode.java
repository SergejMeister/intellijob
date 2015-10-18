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
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Skill node model to represents skill document.
 */
public class SkillNode extends BaseDocument {

    List<SkillNode> nodes;
    private String name;
    private String Description;

    public SkillNode() {
    }

    public SkillNode(final ObjectId id) {
        setId(id.toHexString());
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
}
