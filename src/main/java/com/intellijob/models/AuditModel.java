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

package com.intellijob.models;

import com.intellijob.domain.AuditData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;
import java.util.List;

/**
 * This is a model to represent current audit data without id and list of histories.
 * <p>
 * <p>
 * This model should be initialize with list or pages of histories audit data.
 * </p>
 */
public class AuditModel {

    /**
     * Current audit data.
     */
    private AuditData currentData;

    /**
     * List of histories audit data.
     */
    private List<AuditData> listHistories;

    /**
     * Page of histories audit data.
     */
    private Page<AuditData> pageHistories;

    /**
     * Default constructor.
     */
    public AuditModel() {
    }

    /**
     * Init list and page of histories with list param.
     *
     * @param histories list of histories.
     */
    public AuditModel(List<AuditData> histories) {
        this.listHistories = histories;
    }

    /**
     * Init list and page histories with page param.
     *
     * @param histories pages of histories.
     */
    public AuditModel(Page<AuditData> histories) {
        this.pageHistories = histories;
    }

    /**
     * Returns current audit data.
     * <p>
     * Current audit data doesn't have id because not stored in db.
     * </p>
     *
     * @return audit data without id.
     */
    public AuditData getCurrentData() {
        return currentData;
    }

    /**
     * Sets current audit data.
     *
     * @param currentData current audit data.
     */
    public void setCurrentData(AuditData currentData) {
        this.currentData = currentData;
    }


    /**
     * Returns list of audit data saved in db.
     * <p>
     * IF list of histories is null, try to get List from pages <code><pageHistories.getContent()</code>.
     * </p>
     *
     * @return list of histories audit data.
     */
    public List<AuditData> getHistoriesList() {
        if (listHistories != null) {
            return listHistories;
        }

        if (pageHistories != null) {
            return pageHistories.getContent();
        }

        return Collections.EMPTY_LIST;
    }


    /**
     * Returns pages of audit data saved in db.
     * <p>
     * IF pages of histories audit data is null, try to init new pages with not empty list of histories
     * <code>new PageImpl<>(listHistories);</code>
     * </p>
     *
     * @return pages of histories audit data.
     */
    public Page<AuditData> getHistoriesPage() {
        if (pageHistories != null) {
            return pageHistories;
        }

        if (listHistories != null) {
            return this.pageHistories;
        }

        return new PageImpl<>((Collections.EMPTY_LIST));
    }
}
