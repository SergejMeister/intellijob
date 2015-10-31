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

import com.civis.utils.csv.common.CSVData;
import com.civis.utils.csv.common.CSVReader;
import com.intellijob.BaseTester;
import com.intellijob.domain.localization.LocalizableObject;
import com.intellijob.domain.skills.SkillCategory;
import com.intellijob.domain.skills.SkillLanguage;
import com.intellijob.domain.skills.SkillNode;
import com.intellijob.enums.SkillCategoryEnum;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


public class SkillLanguageRepositoryTest extends BaseTester {

    public static final String DEFAULT_LANGUAGE_RES = "skills/languages";

    @Autowired
    private SkillCategoryRepository skillCategoryRepository;

    @Autowired
    private SkillLanguageRepository skillLanguageRepository;

    @Test
    public void testCreateSkillLanguage() {
        SkillCategory category = skillCategoryRepository.findByType(SkillCategoryEnum.LANGUAGE.getTypeId());

        SkillLanguage skillLanguage = new SkillLanguage(category);
        List<SkillNode> languageNodes = readLanguageData();
        skillLanguage.setLanguages(languageNodes);
        skillLanguageRepository.save(skillLanguage);

        SkillLanguage skillLanguageForTest = skillLanguageRepository.findFirstByOrderByIdAsc();
        Assert.assertEquals(SkillCategoryEnum.LANGUAGE.getTypeId(), skillLanguageForTest.getCategory().getType());
        Assert.assertEquals("Languages", skillLanguageForTest.getCategory().getName());
        Assert.assertEquals("69 languages", 69, skillLanguageForTest.getLanguages().size());
    }

    protected List<SkillNode> readLanguageData() {
        List<CSVData> csvDataList = CSVReader.read(DEFAULT_LANGUAGE_RES, ",");
        Assert.assertNotNull(csvDataList);
        Assert.assertFalse(csvDataList.isEmpty());

        List<SkillNode> result = new ArrayList<>();
        for (CSVData language : csvDataList) {
            Assert.assertNotNull(language.getItems());
            Assert.assertEquals("Should be exact 2 items", 2, language.getItems().size());
            String languageName = language.getItems().get(0);
            LocalizableObject localizableObject = new LocalizableObject(language.getItems().get(1));
            result.add(new SkillNode(languageName, localizableObject));
        }

        return result;
    }
}