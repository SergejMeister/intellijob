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

package com.intellijob.dto.request;

import com.intellijob.dto.ProfileData;
import com.intellijob.dto.SkillRatingData;

import java.util.List;

/**
 * Request data transfer object to save user data.
 */
public class RequestUserData extends RequestData {

    private String userId;

    private ProfileData profileData;

    private String simpleSearchField;

    private List<SkillRatingData> knowledges;

    private List<SkillRatingData> languages;

    private List<SkillRatingData> personalStrengths;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ProfileData getProfileData() {
        return profileData;
    }

    public void setProfileData(ProfileData profileData) {
        this.profileData = profileData;
    }

    public String getSimpleSearchField() {
        return simpleSearchField;
    }

    public void setSimpleSearchField(String simpleSearchField) {
        this.simpleSearchField = simpleSearchField;
    }

    public List<SkillRatingData> getLanguages() {
        return languages;
    }

    public void setLanguages(List<SkillRatingData> languages) {
        this.languages = languages;
    }

    public List<SkillRatingData> getPersonalStrengths() {
        return personalStrengths;
    }

    public void setPersonalStrengths(List<SkillRatingData> personalStrengths) {
        this.personalStrengths = personalStrengths;
    }

    public List<SkillRatingData> getKnowledges() {
        return knowledges;
    }

    public void setKnowledges(List<SkillRatingData> knowledges) {
        this.knowledges = knowledges;
    }
}
