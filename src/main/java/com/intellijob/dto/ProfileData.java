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

import com.intellijob.domain.Profile;
import com.intellijob.enums.SearchEngineEnum;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * Data transfer object represents domain object <code>Profile</code>
 */
public class ProfileData implements Serializable {

    private String firstName;

    private String secondName;

    private String sex;

    private String fullName;

    private Date lastMailSyncDate;

    private String searchEngine;

    private String selectedEducationLevel;

    private String selectedEducationDescription;

    private String selectedPraxisLevel;

    private int selectedPraxisExperience;


    public ProfileData() {
        this.searchEngine = SearchEngineEnum.UNKNOWN.name();
    }

    public ProfileData(String firstName, String secondName, String sex) {
        setFirstName(firstName);
        setSecondName(secondName);
        setSex(sex);
        setFullName(createFullName(firstName, secondName));
    }

    public ProfileData(Profile profile) {
        this(profile.getFirstName(), profile.getSecondName(), profile.getSex());
        setLastMailSyncDate(profile.getLastMailSyncDate());
        setSearchEngine(profile.getSearchEngine().name());
        setSelectedEducationLevel(profile.getSelectedEducationLevel());
        setSelectedEducationDescription(profile.getSelectedEducationDescription());
        setSelectedPraxisLevel(profile.getSelectedPraxisLevel());
        setSelectedPraxisExperience(profile.getSelectedPraxisExperience());
    }

    private String createFullName(String firstName, String secondName) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.hasLength(firstName)) {
            sb.append(firstName);
        }
        if (StringUtils.hasLength(secondName) && StringUtils.hasLength(firstName)) {
            sb.append("-");
            sb.append(secondName);
        }
        if (StringUtils.hasLength(secondName) && !StringUtils.hasLength(firstName)) {
            sb.append(secondName);
        }

        return sb.toString();
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getLastMailSyncDate() {
        return lastMailSyncDate;
    }

    public void setLastMailSyncDate(Date lastMailSyncDate) {
        this.lastMailSyncDate = lastMailSyncDate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSelectedEducationLevel() {
        return selectedEducationLevel;
    }

    public void setSelectedEducationLevel(String selectedEducationLevel) {
        this.selectedEducationLevel = selectedEducationLevel;
    }

    public String getSelectedEducationDescription() {
        return selectedEducationDescription;
    }

    public void setSelectedEducationDescription(String selectedEducationDescription) {
        this.selectedEducationDescription = selectedEducationDescription;
    }

    public String getSelectedPraxisLevel() {
        return selectedPraxisLevel;
    }

    public void setSelectedPraxisLevel(String selectedPraxisLevel) {
        this.selectedPraxisLevel = selectedPraxisLevel;
    }

    public int getSelectedPraxisExperience() {
        return selectedPraxisExperience;
    }

    public void setSelectedPraxisExperience(int selectedPraxisExperience) {
        this.selectedPraxisExperience = selectedPraxisExperience;
    }
}
