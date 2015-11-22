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

package com.intellijob.dto.response;

import com.intellijob.domain.User;
import com.intellijob.domain.skills.SkillRatingNode;
import com.intellijob.dto.ProfileData;
import com.intellijob.dto.SkillRatingData;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Data transfer object represents domain object <code>User</code>
 */
public class ResponseUserData extends ResponseData {

    private String userId;

    private ProfileData profileData;

    private String simpleSearchField;

    private List<SkillRatingData> languages;

    private List<SkillRatingData> personalStrengths;

    private List<SkillRatingData> knowledges;

    public ResponseUserData() {
    }

    public ResponseUserData(User user) {
        setUserId(user.getId());
        setProfileData(new ProfileData(user.getProfile()));
        setSimpleSearchField(user.getSimpleSearchField());
        this.languages = initUserSkills(user.getSkills().getLanguages());
        this.personalStrengths = initUserSkills(user.getSkills().getPersonalStrengths());
        this.knowledges = initUserSkills(user.getSkills().getKnowledges());
    }

    private List<SkillRatingData> initUserSkills(List<SkillRatingNode> userSkills) {
        return userSkills.stream().map(skillRatingNode -> new SkillRatingData(skillRatingNode))
                .collect(Collectors.toList());
    }

    public ProfileData getProfileData() {
        return profileData;
    }

    public void setProfileData(ProfileData profileData) {
        this.profileData = profileData;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
