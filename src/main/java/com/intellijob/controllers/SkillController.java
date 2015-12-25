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

package com.intellijob.controllers;

import com.intellijob.domain.skills.SkillNode;
import com.intellijob.elasticsearch.domain.EsAutocompleteLanguage;
import com.intellijob.models.SkillViewModel;

import java.util.List;

/**
 * Interface skill controller.
 * <p>
 * This controller include methods for all skills repositories, SkillCategory,SkillKnowledge, SkillLanguageRepository.
 */
public interface SkillController {

    /**
     * Returns list with all supported languages.
     *
     * @return list of languages.
     */
    List<SkillNode> getAllLanguages();

    /**
     * Returns list with all supported skills for personal strengths.
     *
     * @return list of personal strengths.
     */
    List<SkillNode> getPersonalStrengths();

    /**
     * Returns list with all supported knowledges.
     *
     * @return list of knowledges.
     */
    List<SkillNode> getKnowledges();

    /**
     * Returns full SkillViewModel.
     *
     * @return full SkillViewModel.
     */
    SkillViewModel getSkillViewModel();

    /**
     * Create elasticsearch indexes for autocomplete field language.
     * <p>
     * Read all supported languages in database and create for their a elasticsearch index.
     */
    void createAutocompleteLanguageIndexes();

    /**
     * Create a autocomplete language index for specified skillNode.
     *
     * @param skillNode skill node.
     *
     * @return create autocomplete language index.
     */
    EsAutocompleteLanguage createAutocompleteLanguageIndex(SkillNode skillNode);
}
