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

package com.intellijob.elasticsearch;

import com.intellijob.domain.User;
import com.intellijob.enums.SearchEngineEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Search builder to
 */
public class SearchModelBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(SearchModelBuilder.class);

    private SearchModel searchModel;

    public SearchModelBuilder(User user) {
        searchModel = new SearchModel(user);
    }

    public SearchModelBuilder setSearchEngine(String searchEngine) {
        try {
            SearchEngineEnum searchEngineEnum = SearchEngineEnum.valueOf(searchEngine);
            searchModel.setSearchEngineEnum(searchEngineEnum);
        } catch (IllegalArgumentException iae) {
            LOG.warn("Illegal argument exception for SearchEngineEnum: {}", searchEngine);
        }
        return this;
    }

    public SearchModelBuilder setSearchData(String searchData) {
        searchModel.setSearchData(searchData);
        return this;
    }

    public SearchModelBuilder setLimit(int limit) {
        searchModel.setLimit(limit);
        return this;
    }

    public SearchModelBuilder setOffset(int offset) {
        searchModel.setOffset(offset);
        return this;
    }

    public SearchModel build() {
        return searchModel;
    }
}
