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

package com.intellijob.domain;

import com.intellijob.enums.SearchEngineEnum;

import java.util.Date;

/**
 * User profile.
 * <p>
 * Note: This class is not complete.
 */
public class Profile {

    private String firstName;

    private String secondName;

    private String sex;

    private Date lastMailSyncDate;

    private SearchEngineEnum searchEngine = SearchEngineEnum.UNKNOWN;

    /**
     * Returns search engine.
     * <p>
     * Default search engine is UNKNOWN.
     * </p>
     *
     * @return selected search engine.
     */
    public SearchEngineEnum getSearchEngine() {
        return searchEngine;
    }

    /**
     * Sets search engine.
     *
     * @param searchEngine search engine.
     */
    public void setSearchEngine(SearchEngineEnum searchEngine) {
        this.searchEngine = searchEngine;
    }

    /**
     * Returns user first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets user first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns user second name.
     */
    public String getSecondName() {
        return secondName;
    }

    /**
     * Sets user second name.
     */
    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    /**
     * Returns user sex.
     */
    public String getSex() {
        return sex;
    }

    /**
     * Sets user sex.
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * Returns last mail sync date.
     * <p>
     * This date is the last mail search date!
     *
     * @return date.
     */
    public Date getLastMailSyncDate() {
        return lastMailSyncDate;
    }

    /**
     * Sets last mails search date.
     *
     * @param lastMailSyncDate last mail search date.
     */
    public void setLastMailSyncDate(Date lastMailSyncDate) {
        this.lastMailSyncDate = lastMailSyncDate;
    }
}
