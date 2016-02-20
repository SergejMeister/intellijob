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

package com.intellijob.webservices;

import com.intellijob.controllers.JobDetailController;
import com.intellijob.controllers.SkillController;
import com.intellijob.controllers.UserController;
import com.intellijob.dto.SkillData;
import com.intellijob.elasticsearch.domain.EsUserSkills;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Services for elasticsearch to create, update or delete indexes.
 */
@RestController
public class ElasticServices extends BaseServices {

    @Autowired
    private JobDetailController jobDetailController;

    @Autowired
    private SkillController skillController;


    @Autowired
    private UserController userController;

    @RequestMapping(value = Endpoints.ES_JOBDEATAILS_INDEX, method = RequestMethod.PUT)
    public ResponseEntity createJobDetailsIndexes() {
        jobDetailController.createIndexes();
        return ResponseEntity.accepted().build();
    }

    @RequestMapping(value = Endpoints.ES_USERS_ID_SKILLS, method = RequestMethod.GET)
    public List<EsUserSkills> getUserSkills(@PathVariable String userId) {
        return userController.getUserSkills(userId);
    }

    @RequestMapping(value = Endpoints.ES_AUTOCOMPLETE_LANGUAGE_INDEX, method = RequestMethod.PUT)
    public ResponseEntity createAutocompleteLanguageIndexes() {
        skillController.createAutocompleteLanguageIndexes();
        return ResponseEntity.accepted().build();
    }

    @RequestMapping(value = Endpoints.ES_AUTOCOMPLETE_LANGUAGE_INDEX, method = RequestMethod.GET)
    public List<SkillData> getSupportedLanguages() {
        return skillController.getSupportedLanguages().stream()
                .map(skillNode -> new SkillData(skillNode.getId(), skillNode.getName())).collect(Collectors.toList());
    }

    /**
     * This Service is for language auto complete.
     *
     * @param value search value.
     *
     * @return suggested list of languages.
     */
    @RequestMapping(value = Endpoints.ES_AUTOCOMPLETE_LANGUAGE_NAME_VALUE, method = RequestMethod.GET)
    public List<SkillData> getSuggestedLanguagesBy(@PathVariable String value) {
        return skillController.suggestLanguage(value).stream()
                .map(skillNode -> new SkillData(skillNode.getId(), skillNode.getName())).collect(Collectors.toList());
    }

    @RequestMapping(value = Endpoints.ES_AUTOCOMPLETE_KNOWLEDGE_INDEX, method = RequestMethod.PUT)
    public ResponseEntity createAutocompleteKnowledgeIndexes() {
        skillController.createAutocompleteKnowledgeIndexes();
        return ResponseEntity.accepted().build();
    }

    @RequestMapping(value = Endpoints.ES_AUTOCOMPLETE_KNOWLEDGE_INDEX, method = RequestMethod.GET)
    public List<SkillData> getSupportedKnowledges() {
        return skillController.getSupportedKnowledges().stream()
                .map(skillNode -> new SkillData(skillNode.getId(), skillNode.getName())).collect(Collectors.toList());
    }

    /**
     * This Service is for knowledge auto complete.
     *
     * @param value search value.
     *
     * @return suggested list of knowledges.
     */
    @RequestMapping(value = Endpoints.ES_AUTOCOMPLETE_KNOWLEDGE_NAME_VALUE, method = RequestMethod.GET)
    public List<SkillData> getSuggestedKnowledgesBy(@PathVariable String value) {
        return skillController.suggestKnowledge(value).stream()
                .map(skillNode -> new SkillData(skillNode.getId(), skillNode.getName())).collect(Collectors.toList());
    }
}
