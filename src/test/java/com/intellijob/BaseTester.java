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

package com.intellijob;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellijob.domain.LocalizableObject;
import com.intellijob.domain.Profile;
import com.intellijob.domain.User;
import com.intellijob.domain.skills.SkillCategory;
import com.intellijob.domain.skills.SkillNode;
import com.intellijob.dto.SkillData;
import com.intellijob.dto.SkillRatingData;
import com.intellijob.repository.skills.SkillCategoryRepository;
import com.intellijob.repository.user.UserRepository;
import junit.framework.Assert;
import org.apache.commons.lang.RandomStringUtils;
import org.bson.types.ObjectId;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//RunWith and WebAppConfiguration should be placed here!!!
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@DefaultTestAnnotations
public abstract class BaseTester {

    protected final static Logger LOG = LoggerFactory.getLogger(BaseTester.class);

    protected static final String USER_PROFILE_DEFAULT_FIRSTNAME = "TestFirstName";
    protected static final String USER_PROFILE_DEFAULT_SECONDNAME = "TestSecondName";
    protected static final String USER_PROFILE_DEFAULT_SEX = "M";
    protected final static String DEFAULT_ENCODING = "UTF-8";

    /**
     * Production or test flag.
     */
    @Value("${spring.data.mongodb.production}")
    protected boolean isProduction;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected SkillCategoryRepository skillCategoryRepository;

    /**
     * Production or test flag.
     */
    @Value("${spring.data.mongodb.replication.storage}")
    private String replicationStorage;

    /**
     * Mongo Host.
     */
    @Value("${spring.data.mongodb.host}")
    private String mongoHost;

    /**
     * Mongo port.
     */
    @Value("${spring.data.mongodb.port}")
    private Integer mongoPort;

    /**
     * Mongo database.
     */
    @Value("${spring.data.mongodb.database}")
    private String mongoDatabase;


    protected static SkillData createSkillData(String name) {
        String id = RandomStringUtils.random(5);
        return new SkillData(id, name);
    }

    protected static SkillRatingData createSkillRatingData(String name, int rating) {
        SkillData skillData = createSkillData(name);

        SkillRatingData skillRatingData = new SkillRatingData();
        skillRatingData.setSkillData(skillData);
        skillRatingData.setRating(rating);

        return skillRatingData;
    }

    protected User initDefaultUser() {
        Profile profile = new Profile();
        profile.setFirstName(USER_PROFILE_DEFAULT_FIRSTNAME);
        profile.setSecondName(USER_PROFILE_DEFAULT_SECONDNAME);
        profile.setSex(USER_PROFILE_DEFAULT_SEX);

        User defaultUser = new User();
        defaultUser.setProfile(profile);
        return defaultUser;
    }

    protected List<SkillNode> readResourceData(String resourcePath) {
        List<SkillNode> result = new ArrayList<>();

        BufferedReader br;
        String line;
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(resourcePath)) {
            br = new BufferedReader(new InputStreamReader(inputStream, DEFAULT_ENCODING));
            while ((line = br.readLine()) != null) {
                LocalizableObject localizableObject = new LocalizableObject(line);
                SkillNode skillNode = new SkillNode(new ObjectId(), localizableObject);
                skillNode.setName(line);
                result.add(skillNode);
            }
        } catch (Exception e) {
            LOG.error("Error occurred while read file (" + resourcePath + ")", e);
            Assert.fail("Can not read file");
        }

        return result;
    }

    protected List<SkillNode> addSubSkills(String skillName, List<SkillNode> list, String resPath) {
        List<SkillNode> result = new ArrayList<>();
        for (SkillNode skillNode : list) {
            if (skillNode.getName().equals(skillName)) {
                List<SkillNode> subSkills = readResourceData(resPath);
                skillNode.setNodes(subSkills);
                result.add(skillNode);
            } else {
                result.add(skillNode);
            }
        }

        return result;
    }

    /**
     * Reload collection skill_categories.
     * <p>
     * Drop collection if exist, create a new collection and load data.
     * Read data from skill_categories.json
     *
     * @throws IOException exception.
     */
    protected void reloadCollectionSkillCategories() throws Exception {
        skillCategoryRepository.deleteAll();
        URL skillCategoriesURL = Thread.currentThread().getContextClassLoader()
                .getResource("imports/skill_categories.json");

        TypeReference<List<SkillCategory>> typeRef = new TypeReference<List<SkillCategory>>() {
        };
        List<SkillCategory> categories = new ObjectMapper().readValue(new File(skillCategoriesURL.getFile()), typeRef);
        skillCategoryRepository.save(categories);
    }

    /**
     * Reload collection users.
     * <p>
     * Drop collection if exist, create a new collection and load data.
     * Read data from collection_users.json
     *
     * @throws IOException exception.
     */
    protected void reloadCollectionUsers() throws Exception {
        userRepository.deleteAll();
        URL collectionUserURL = Thread.currentThread().getContextClassLoader()
                .getResource("imports/collection_users.json");

        User user = new ObjectMapper().readValue(new File(collectionUserURL.getFile()), User.class);
        userRepository.save(user);
    }


    protected void initMongoDatabase() throws Exception {
        reloadCollectionSkillCategories();
        reloadCollectionUsers();
    }
}
