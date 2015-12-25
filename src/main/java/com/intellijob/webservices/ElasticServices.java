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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Services for elasticsearch to create, update or delete indexes.
 */
@RestController
public class ElasticServices extends BaseServices {

    @Autowired
    private JobDetailController jobDetailController;

    @Autowired
    private SkillController skillController;

    @RequestMapping(value = Endpoints.ES_JOBDEATAILS_INDEX, method = RequestMethod.PUT)
    public ResponseEntity createJobDetailsIndexes() {
        jobDetailController.createElasticsearchIndexes();
        return ResponseEntity.accepted().build();
    }

    @RequestMapping(value = Endpoints.ES_AUTOCOMPLETE_LANGUAGE_INDEX, method = RequestMethod.PUT)
    public ResponseEntity createAutocompleteLanguageIndexes() {
        skillController.createAutocompleteLanguageIndexes();
        return ResponseEntity.accepted().build();
    }
}
