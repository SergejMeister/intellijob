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

package com.intellijob.webservices.mappers;

import com.intellijob.domain.Profile;
import com.intellijob.domain.User;
import com.intellijob.domain.skills.SkillNode;
import com.intellijob.domain.skills.SkillRatingNode;
import com.intellijob.domain.skills.Skills;
import com.intellijob.dto.ProfileData;
import com.intellijob.dto.SkillData;
import com.intellijob.dto.SkillRatingData;
import com.intellijob.dto.request.RequestUserData;
import com.intellijob.enums.SearchEngineEnum;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class to map user request and response data.
 */
public final class UserServiceMapper {

    private UserServiceMapper() {
    }

    public static Profile mapTo(ProfileData profileData) {
        Profile profile = new Profile();
        profile.setFirstName(profileData.getFirstName());
        profile.setSecondName(profileData.getSecondName());
        profile.setSex(profileData.getSex());
        SearchEngineEnum searchEngineEnum = SearchEngineEnum.valueOf(profileData.getSearchEngine());
        profile.setSearchEngine(searchEngineEnum);
        profile.setLastMailSyncDate(profileData.getLastMailSyncDate());
        return profile;
    }

    public static User mapTo(RequestUserData requestUserData) {

        User user = new User(requestUserData.getUserId());
        Profile profile = mapTo(requestUserData.getProfileData());
        user.setProfile(profile);
        user.setSimpleSearchField(requestUserData.getSimpleSearchField());
        Skills skills = initSkills(requestUserData);
        user.setSkills(skills);
        return user;
    }

    public static Skills initSkills(RequestUserData requestUserData) {
        Skills skills = new Skills();

        List<SkillRatingNode> knowledges = mapToListSkillRatingNode(requestUserData.getKnowledges());
        skills.setKnowledges(knowledges);

        List<SkillRatingNode> personalStrengths = mapToListSkillRatingNode(requestUserData.getPersonalStrengths());
        skills.setPersonalStrengths(personalStrengths);

        List<SkillRatingNode> languages = mapToListSkillRatingNode(requestUserData.getLanguages());
        skills.setLanguages(languages);

        return skills;
    }

    public static List<SkillRatingNode> mapToListSkillRatingNode(List<SkillRatingData> skillRatingDataList) {
        if (skillRatingDataList != null) {
            return skillRatingDataList.stream().map(UserServiceMapper::mapTo).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    public static List<SkillData> mapToListSkillData(List<SkillNode> skillNodes) {
        return skillNodes.stream().map(skillNode -> new SkillData(skillNode)).collect(Collectors.toList());
    }

    public static SkillRatingNode mapTo(SkillRatingData skillRatingData) {
        SkillNode skillNode = mapTo(skillRatingData.getSkillData());
        return new SkillRatingNode(skillNode, skillRatingData.getRating());
    }

    public static SkillNode mapTo(SkillData skillData) {
        SkillNode skillNode = new SkillNode();
        skillNode.setName(skillData.getName());
        if (isValidId(skillData.getId())) {
            skillNode.setId(skillData.getId());
        }
        return skillNode;
    }

    public static Boolean isValidId(String id) {
        return id != null && !id.isEmpty();
    }
}
