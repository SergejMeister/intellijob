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
import com.intellijob.dto.SkillData;
import com.intellijob.models.SkillViewModel;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Data transfer object represents user page.
 */
public class ResponseUserForm extends ResponseData {

    private ResponseUserData userData;

    private List<SkillData> supportedLanguages;

    private List<SkillData> supportedPersonalStrengths;

    public ResponseUserForm() {
    }

    public ResponseUserForm(User user) {
        this(user, null);
    }

    public ResponseUserForm(User user, SkillViewModel skillViewModel) {
        setUserData(new ResponseUserData(user));
        List<SkillData> languagesData =
                skillViewModel.getLanguages().stream().map(skillNode -> new SkillData(skillNode))
                        .collect(Collectors.toList());
        setSupportedLanguages(languagesData);

        List<SkillData> personalStrengths =
                skillViewModel.getPersonalStrengths().stream().map(skillNode -> new SkillData(skillNode))
                        .collect(Collectors.toList());
        setSupportedPersonalStrengths(personalStrengths);
    }

    public List<SkillData> getSupportedPersonalStrengths() {
        return supportedPersonalStrengths;
    }

    public void setSupportedPersonalStrengths(List<SkillData> supportedPersonalStrengths) {
        this.supportedPersonalStrengths = supportedPersonalStrengths;
    }

    public ResponseUserData getUserData() {
        return userData;
    }

    public void setUserData(ResponseUserData userData) {
        this.userData = userData;
    }

    public List<SkillData> getSupportedLanguages() {
        return supportedLanguages;
    }

    public void setSupportedLanguages(List<SkillData> supportedLanguages) {
        this.supportedLanguages = supportedLanguages;
    }
}
