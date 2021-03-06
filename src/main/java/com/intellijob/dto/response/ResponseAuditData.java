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

package com.intellijob.dto.response;

import com.intellijob.domain.AuditData;
import com.intellijob.models.AuditModel;

import java.util.Date;

/**
 * Data transfer object to represents <code>AuditData</code>.
 */
public class ResponseAuditData extends ResponseData {

    private String id;

    /**
     * count of all job details.
     */
    private long countJobDetails;

    /**
     * Count of job details with empty contact persons.
     */
    private long countEmptyContactPersons;

    /**
     * Count of job details with not empty contact persons.
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

    private Date createdDate;


    public ResponseAuditData() {
    }

    /**
     * This constructor initialize ResponseAuditData parameter with currentAuditData <code>auditModel.getCurrentData()</code>.
     */
    public ResponseAuditData(AuditModel auditModel) {
        setId(auditModel.getCurrentData().getId());
        setCountJobDetails(auditModel.getCurrentData().getCountJobDetails());
        setCountNotEmptyContactPersons(auditModel.getCurrentData().getCountNotEmptyContactPersons());
        setCountEmptyContactPersons(auditModel.getCurrentData().getCountEmptyContactPersons());
        setCountEmptyAddress(auditModel.getCurrentData().getCountEmptyAddress());
        setCountNotEmptyAddress(auditModel.getCurrentData().getCountNotEmptyAddress());
    }

    public ResponseAuditData(AuditData auditData) {
        setId(auditData.getId());
        setCountJobDetails(auditData.getCountJobDetails());
        setCountNotEmptyContactPersons(auditData.getCountNotEmptyContactPersons());
        setCountEmptyContactPersons(auditData.getCountEmptyContactPersons());
        setCountEmptyAddress(auditData.getCountEmptyAddress());
        setCountNotEmptyAddress(auditData.getCountNotEmptyAddress());
        setCreatedDate(auditData.getCreatedDate());
    }

    public ResponseAuditData(String auditId) {
        this.id = auditId;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public long getCountEmptyAddress() {
        return countEmptyAddress;
    }

    public void setCountEmptyAddress(long countEmptyAddress) {
        this.countEmptyAddress = countEmptyAddress;
    }

    public long getCountNotEmptyAddress() {
        return countNotEmptyAddress;
    }

    public void setCountNotEmptyAddress(long countNotEmptyAddress) {
        this.countNotEmptyAddress = countNotEmptyAddress;
    }
}
