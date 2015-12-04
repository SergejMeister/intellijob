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

import com.intellijob.Constants;
import com.intellijob.domain.User;
import com.intellijob.enums.SearchEngineEnum;

/**
 * Represents search data.
 * <p>
 * Default searchEngine is user.profile.searchEngine.
 * Default searchData is user.simpleSearchField.
 * Default limit is 50.
 */
public class SearchModel {

    private User user;

    private int limit;

    private int offset;

    private SearchEngineEnum searchEngineEnum;

    private String searchData;

    public SearchModel(User user) {
        this.user = user;
        setLimit(Constants.DB_RESULT_LIMIT);
        setSearchEngineEnum(user.getProfile().getSearchEngine());
        setSearchData(user.getSimpleSearchField());
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public SearchEngineEnum getSearchEngineEnum() {
        return searchEngineEnum;
    }

    public void setSearchEngineEnum(SearchEngineEnum searchEngineEnum) {
        this.searchEngineEnum = searchEngineEnum;
    }

    public String getSearchData() {
        return searchData;
    }

    public void setSearchData(String searchData) {
        this.searchData = searchData;
    }

    public User getUser() {
        return user;
    }
}
