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

/**
 * Skill node with rating attribute.
 */
public class SkillRatingNode {

    private SkillNode skillNode;

    private int rating;

    public SkillRatingNode() {
    }

    public SkillRatingNode(SkillNode skillNode, int rating) {
        setSkillNode(skillNode);
        setRating(rating);
    }

    public SkillRatingNode(int rating) {
        setRating(rating);
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public SkillNode getSkillNode() {
        return skillNode;
    }

    public void setSkillNode(SkillNode skillNode) {
        this.skillNode = skillNode;
    }

    @Override
    public String toString() {
        return "SkillRatingNode{" +
                "skillNode=" + skillNode +
                ", rating=" + rating +
                '}';
    }
}
