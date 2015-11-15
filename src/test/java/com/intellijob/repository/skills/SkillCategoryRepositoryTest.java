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

package com.intellijob.repository.skills;

import com.intellijob.BaseTester;
import com.intellijob.domain.LocalizableObject;
import com.intellijob.domain.skills.SkillCategory;
import com.intellijob.enums.SkillCategoryEnum;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


public class SkillCategoryRepositoryTest extends BaseTester {

    @Autowired
    private SkillCategoryRepository skillCategoryRepository;

    @Test
    @Ignore
    public void testCreate() {
        List<SkillCategory> categories = new ArrayList<>();
        LocalizableObject localizableObjectExperience = new LocalizableObject("Berufserfahrung");
        SkillCategory skillCategoryExp =
                new SkillCategory("Experience", localizableObjectExperience, SkillCategoryEnum.EXPERIENCE.getTypeId());
        skillCategoryExp.setDescription("type for all experience data");
        categories.add(skillCategoryExp);

        LocalizableObject localizableObjectEduc = new LocalizableObject("Bildung");
        SkillCategory skillCategoryEduc =
                new SkillCategory("Experience", localizableObjectEduc, SkillCategoryEnum.EDUCATION.getTypeId());
        skillCategoryEduc.setDescription("type for all education data.");
        categories.add(skillCategoryEduc);

        LocalizableObject localizableObjectKnow = new LocalizableObject("Kenntnisse und Fertigkeiten");
        SkillCategory skillCategoryKnow =
                new SkillCategory("Knowledges", localizableObjectKnow, SkillCategoryEnum.KNOWLEDGE.getTypeId());
        skillCategoryKnow.setDescription("type for all knowledges categories, like it,bau etc.");
        categories.add(skillCategoryKnow);

        LocalizableObject localizableObjectLanguage = new LocalizableObject("Sprachen");
        SkillCategory skillCategoryLanguage =
                new SkillCategory("Languages", localizableObjectLanguage, SkillCategoryEnum.LANGUAGE.getTypeId());
        skillCategoryLanguage.setDescription("type for all language skills.");
        categories.add(skillCategoryLanguage);

        LocalizableObject localizableObjectPers = new LocalizableObject("Persönliche Stärken");
        SkillCategory skillCategoryPers =
                new SkillCategory("Personal strengths", localizableObjectPers,
                        SkillCategoryEnum.PERSONAL_STRENGTH.getTypeId());
        skillCategoryPers.setDescription("type for all categories of personal strength");
        categories.add(skillCategoryPers);

        LocalizableObject localizableObjectMob = new LocalizableObject("Mobilität");
        SkillCategory skillCategoryMob =
                new SkillCategory("Mobility", localizableObjectMob, SkillCategoryEnum.MOBILITY.getTypeId());
        skillCategoryMob.setDescription("type for Driving licences and driving cars");
        categories.add(skillCategoryMob);

        skillCategoryRepository.save(categories);
    }

}