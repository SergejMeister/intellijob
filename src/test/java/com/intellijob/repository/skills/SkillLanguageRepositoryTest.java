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
import com.intellijob.domain.skills.SkillLanguage;
import com.intellijob.domain.skills.SkillNode;
import com.intellijob.enums.SkillCategoryEnum;
import junit.framework.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class SkillLanguageRepositoryTest extends BaseTester {

    public static final String DEFAULT_LANGUAGE_RES = "skills/languages";
    private final static Logger LOG = LoggerFactory.getLogger(SkillLanguageRepositoryTest.class);

    @Autowired
    private SkillCategoryRepository skillCategoryRepository;

    @Autowired
    private SkillLanguageRepository skillLanguageRepository;

    @Test
    public void testCreateSkillLanguage() {
        SkillCategory category = skillCategoryRepository.findByType(SkillCategoryEnum.LANGUAGE.getTypeId());
        SkillLanguage skillLanguage = new SkillLanguage();
        skillLanguage.setCategory(category);
        List<SkillNode> languageNodes = readLanguageData();
        skillLanguage.setLanguages(languageNodes);
        skillLanguageRepository.save(skillLanguage);

        SkillLanguage skillLanguageForTest = skillLanguageRepository.findFirstByOrderByIdAsc();
        Assert.assertEquals(SkillCategoryEnum.LANGUAGE.getTypeId(), skillLanguageForTest.getCategory().getType());
        Assert.assertEquals("Languages", skillLanguageForTest.getCategory().getName());
        Assert.assertEquals("69 languages", 69, skillLanguageForTest.getLanguages().size());
    }

    protected List<SkillNode> readLanguageData() {
        List<SkillNode> result = new ArrayList<>();

        BufferedReader br = null;
        String line = "";
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(DEFAULT_LANGUAGE_RES)) {
            br = new BufferedReader(new InputStreamReader(inputStream, DEFAULT_ENCODING));
            while ((line = br.readLine()) != null) {
                SkillNode skillNode = new SkillNode();
                skillNode.setName(line);
                result.add(skillNode);
            }
        } catch (Exception e) {
            LOG.error("Error occurred while read file (" + DEFAULT_LANGUAGE_RES + ")", e);
            Assert.fail("Can not read file");
        }

        return result;
    }
}