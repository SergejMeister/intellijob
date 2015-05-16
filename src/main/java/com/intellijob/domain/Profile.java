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

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * User profile.
 * <p>
 * Note: This class is not complete.
 */
@Document(collection = "profiles")
public class Profile {

    @Id
    private String id;

    private Date lastMailSyncDate;

    /**
     * Returns profile unique id.
     *
     * @return profile unique id.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets profile unique id.
     *
     * @param id profile id.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns lasd mail sync date.
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
