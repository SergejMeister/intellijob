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

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Document HistoryAuditData.
 */
@Document(collection = "auditdata")
public class AuditData extends BaseDocument {

    /**
     * All contact details.
     */
    private long countJobDetails;

    /**
     * Contact details with empty list of contact persons.
     */
    private long countEmptyContactPersons;

    /**
     * Contact details with not empty list of contact persons.
     */
    private long countNotEmptyContactPersons;

    /**
     * Contact details with empty list of address.
     */
    private long countEmptyAddress;

    /**
     * Contact details with not empty list of address.
     */
    private long countNotEmptyAddress;

    /**
     * Received date.
     */
    @Indexed
    private Date createdDate;

    public AuditData() {
    }

    /**
     * Returns count of all job details.
     *
     * @return long count value.
     */
    public long getCountJobDetails() {
        return countJobDetails;
    }

    /**
     * Sets count for all contact person.
     *
     * @param countJobDetails count.
     */
    public void setCountJobDetails(long countJobDetails) {
        this.countJobDetails = countJobDetails;
    }

    /**
     * Returns count of job details with empty contact persons.
     *
     * @return long count value.
     */
    public long getCountEmptyContactPersons() {
        return countEmptyContactPersons;
    }

    /**
     * Sets count for empty contact person.
     *
     * @param countEmptyContactPersons count.
     */
    public void setCountEmptyContactPersons(long countEmptyContactPersons) {
        this.countEmptyContactPersons = countEmptyContactPersons;
    }

    /**
     * Returns count of job details with not empty contact persons.
     *
     * @return long count value.
     */
    public long getCountNotEmptyContactPersons() {
        return countNotEmptyContactPersons;
    }

    /**
     * Sets count for not empty contact person.
     *
     * @param countNotEmptyContactPersons count.
     */
    public void setCountNotEmptyContactPersons(long countNotEmptyContactPersons) {
        this.countNotEmptyContactPersons = countNotEmptyContactPersons;
    }

    /**
     * Returns createdDate.
     * <p>
     * This date is a date, when the audit data stored in db.
     *
     * @return createdDate.
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * Sets createdDate.
     *
     * @param createdDate created date.
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Count of JobDetails, witch doesn't have an address.
     */
    public long getCountEmptyAddress() {
        return countEmptyAddress;
    }

    public void setCountEmptyAddress(long countEmptyAddress) {
        this.countEmptyAddress = countEmptyAddress;
    }

    /**
     * Count of JobDetails with address.
     */
    public long getCountNotEmptyAddress() {
        return countNotEmptyAddress;
    }

    public void setCountNotEmptyAddress(long countNotEmptyAddress) {
        this.countNotEmptyAddress = countNotEmptyAddress;
    }
}
