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

import com.intellijob.domain.skills.SkillRatingNode;

import java.io.Serializable;

/**
 * Data transfer object to transfer user skill data with affected rating.
 */
public class SkillRatingData implements Serializable {

    private SkillData skillData;

    private int rating;

    public SkillRatingData() {
    }

    public SkillRatingData(SkillData skillData, int rating) {
        setSkillData(skillData);
        setRating(rating);
    }

    public SkillRatingData(SkillRatingNode skillRatingNode) {
        this(new SkillData(skillRatingNode.getSkillNode()), skillRatingNode.getRating());
    }

    public SkillData getSkillData() {
        return skillData;
    }

    public void setSkillData(SkillData skillData) {
        this.skillData = skillData;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
