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
import com.intellijob.domain.skills.SkillCategory;
import com.intellijob.domain.skills.SkillNode;
import com.intellijob.domain.skills.SkillPersonalStrength;
import com.intellijob.enums.SkillCategoryEnum;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class SkillPersonalStrengthTest extends BaseTester {

    protected static final String PERSONAL_STRENGTH_ROOT_RES_PATH = "skills/personalStrengthRootCategories.txt";
    protected static final String METHODS_RES_PATH = "skills/personalMethods.txt";
    protected static final String ACTIVITY_RES_PATH = "skills/personalActivity.txt";
    protected static final String SOCIAL_COMMUNICATIVE_RES_PATH = "skills/personalSocialCommunicative.txt";
    protected static final String PERSONAL_SKILL_RES_PATH = "skills/personalSkills.txt";

    @Autowired
    private SkillCategoryRepository skillCategoryRepository;

    @Autowired
    private SkillPersonalStrengthRepository skillPersonalStrengthRepository;

    @Test
    public void testSave() {
        SkillCategory category = skillCategoryRepository.findByType(SkillCategoryEnum.PERSONAL_STRENGTH.getTypeId());
        SkillPersonalStrength skillKnowledge = new SkillPersonalStrength(category);
        List<SkillNode> personalStrengths = initAllPersonalStrength();
        skillKnowledge.setPersonalStrength(personalStrengths);
        skillPersonalStrengthRepository.save(skillKnowledge);

        SkillPersonalStrength testFind = skillPersonalStrengthRepository.findFirstByOrderByIdAsc();
        Assert.assertNotNull(testFind);
        Assert.assertFalse("Should not be empty!", testFind.getPersonalStrength().isEmpty());
    }

    private List<SkillNode> initAllPersonalStrength() {
        List<SkillNode> rootCategories = readResourceData(PERSONAL_STRENGTH_ROOT_RES_PATH);

        List<SkillNode> newList = addSubSkills("Methodenkompetenz", rootCategories, METHODS_RES_PATH);
        newList = addSubSkills("Aktivitäts- und Umsetzungskompetenz", newList, ACTIVITY_RES_PATH);
        newList = addSubSkills("Sozial-kommunikative Kompetenz", newList, SOCIAL_COMMUNICATIVE_RES_PATH);
        newList = addSubSkills("Personale Kompetenz", newList, PERSONAL_SKILL_RES_PATH);

        return newList;
    }
}
