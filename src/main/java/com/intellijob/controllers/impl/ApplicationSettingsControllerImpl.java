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

package com.intellijob.controllers.impl;

import com.intellijob.controllers.ApplicationSettingsController;
import com.intellijob.controllers.SkillController;
import com.intellijob.domain.config.ApplicationSettings;
import com.intellijob.repository.config.ApplicationSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Date;

/**
 * Implementation of <code>ApplicationSettingsController</code>.
 */
@Controller
public class ApplicationSettingsControllerImpl implements ApplicationSettingsController {

    @Autowired
    private ApplicationSettingsRepository applicationSettingsRepository;

    @Autowired
    private SkillController skillController;

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean initApplication() {
        ApplicationSettings applicationSettings = applicationSettingsRepository.findTopByOrderByIdDesc();
        if (!applicationSettings.getElasticDataImported()) {
            skillController.createAutocompleteKnowledgeIndexes();
            skillController.createAutocompleteLanguageIndexes();
            applicationSettings.setElasticDataImported(true);
            applicationSettings.setElasticDataImportedDate(new Date());
            applicationSettingsRepository.save(applicationSettings);
        }

        return true;
    }
}
