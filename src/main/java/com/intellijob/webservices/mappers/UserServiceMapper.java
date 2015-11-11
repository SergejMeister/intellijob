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
import com.intellijob.dto.ProfileData;
import com.intellijob.dto.request.RequestUserData;
import com.intellijob.enums.SearchEngineEnum;

/**
 *
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
        return user;
    }
}
