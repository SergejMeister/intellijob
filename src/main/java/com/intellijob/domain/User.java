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

import com.intellijob.domain.skills.Skills;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * User document to represent all user data.
 */
@Document(collection = "users")
public class User extends BaseDocument {

    private Profile profile;

    private String simpleSearchField;

    private Skills skills;

    public User() {
        setProfile(new Profile());
        setSimpleSearchField("");
        setSkills(new Skills());
    }

    public User(String id) {
        super(id);
    }

    /**
     * Returns user profile data.
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Sets user profile data.
     */
    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getSimpleSearchField() {
        return simpleSearchField;
    }

    public void setSimpleSearchField(String simpleSearchField) {
        this.simpleSearchField = simpleSearchField;
    }

    public Skills getSkills() {
        return skills;
    }

    public void setSkills(Skills skills) {
        this.skills = skills;
    }
}
