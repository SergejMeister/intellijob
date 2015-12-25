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

import com.intellijob.controllers.SkillController;
import com.intellijob.domain.skills.SkillKnowledge;
import com.intellijob.domain.skills.SkillLanguage;
import com.intellijob.domain.skills.SkillNode;
import com.intellijob.domain.skills.SkillPersonalStrength;
import com.intellijob.elasticsearch.EsConstants;
import com.intellijob.elasticsearch.domain.EsAutocompleteLanguage;
import com.intellijob.elasticsearch.repository.EsAutocompleteLanguageRepository;
import com.intellijob.models.SkillViewModel;
import com.intellijob.repository.skills.SkillKnowledgeRepository;
import com.intellijob.repository.skills.SkillLanguageRepository;
import com.intellijob.repository.skills.SkillPersonalStrengthRepository;
import org.elasticsearch.action.suggest.SuggestResponse;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionFuzzyBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents skill controllers.
 */
@Controller
public class SkillControllerImpl implements SkillController {

    @Autowired
    private EsAutocompleteLanguageRepository esAutocompleteLanguageRepository;

    @Autowired
    private SkillLanguageRepository skillLanguageRepository;

    @Autowired
    private SkillPersonalStrengthRepository skillPersonalStrengthRepository;

    @Autowired
    private SkillKnowledgeRepository skillKnowledgeRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SkillNode> getAllLanguages() {
        SkillLanguage skillLanguage = skillLanguageRepository.findFirstByOrderByIdAsc();
        return skillLanguage.getLanguages();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SkillNode> getPersonalStrengths() {
        SkillPersonalStrength skillPersonalStrength = skillPersonalStrengthRepository.findFirstByOrderByIdAsc();
        return skillPersonalStrength.getPersonalStrength();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SkillNode> getKnowledges() {
        SkillKnowledge skillKnowledge = skillKnowledgeRepository.findFirstByOrderByIdAsc();
        return skillKnowledge.getKnowledges();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SkillViewModel getSkillViewModel() {
        SkillViewModel skillViewModel = new SkillViewModel();

        List<SkillNode> personalStrengths = getPersonalStrengths();
        skillViewModel.setPersonalStrengths(personalStrengths);

        List<SkillNode> knowledges = getKnowledges();
        skillViewModel.setKnowledges(knowledges);

        return skillViewModel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createAutocompleteLanguageIndexes() {
        List<SkillNode> supportedLanguages = getAllLanguages();
        supportedLanguages.forEach(this::createAutocompleteLanguageIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EsAutocompleteLanguage createAutocompleteLanguageIndex(SkillNode skillNode) {
        if (esAutocompleteLanguageRepository.exists(skillNode.getId())) {
            esAutocompleteLanguageRepository.delete(skillNode.getId());
        }
        EsAutocompleteLanguage autocompleteLanguage =
                new EsAutocompleteLanguage(skillNode.getId(), skillNode.getLocalizableObject().getLabel(),
                        Boolean.TRUE);
        return esAutocompleteLanguageRepository.index(autocompleteLanguage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EsAutocompleteLanguage> getLanguagesForAutocomplete() {
        List<EsAutocompleteLanguage> result = new ArrayList<>();
        esAutocompleteLanguageRepository.findAll().forEach(result::add);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EsAutocompleteLanguage> suggestLanguage(String searchWord) {
        CompletionSuggestionFuzzyBuilder completionSuggestionFuzzyBuilder =
                new CompletionSuggestionFuzzyBuilder(EsConstants.FIELD_SUGGEST).text(searchWord).field(
                        EsConstants.FIELD_SUGGEST);

        SuggestResponse suggestResponse =
                elasticsearchTemplate.suggest(completionSuggestionFuzzyBuilder, EsAutocompleteLanguage.class);
        CompletionSuggestion completionSuggestion =
                suggestResponse.getSuggest().getSuggestion(EsConstants.FIELD_SUGGEST);
        List<CompletionSuggestion.Entry.Option> options = completionSuggestion.getEntries().get(0).getOptions();

        return options.stream()
                .map(option -> new EsAutocompleteLanguage(option.getPayloadAsString(), option.getText().toString()))
                .collect(Collectors.toList());
    }
}
