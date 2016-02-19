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

package com.intellijob.elasticsearch.domain;

import com.intellijob.Constants;
import com.intellijob.domain.Address;
import com.intellijob.domain.ContactPerson;
import com.intellijob.elasticsearch.EsConstants;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

/**
 * This document represents domain object <code>JobDetail</code> in elasticsearch.
 */
@Document(indexName = EsConstants.INDEX_INTELLIJOB, type = EsConstants.TYPE_JOB_DETAILS, shards = 1, replicas = 0)
public class EsJobDetail extends EsBaseDocument{

    /**
     * Date received job mail.
     */
    @Field(
            type = FieldType.Date,
            index = FieldIndex.not_analyzed,
            format = DateFormat.custom,
            pattern = Constants.DEFAULT_DATE_PATTERN,
            store = true
    )
    private Date receivedDate;

    /**
     * Job name.
     */
    @Field(
            type = FieldType.String,
            index = FieldIndex.analyzed,
            store = true
    )
    private String name;

    /**
     * Link to the job page.
     */
    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String link;

    /**
     * Link unique.
     */
    private String jobId;

    /**
     * Job content.
     */
    @Field(
            type = FieldType.String,
            index = FieldIndex.analyzed,
            searchAnalyzer = "german",
            indexAnalyzer = "german",
            store = true
    )
    private String content;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String contentHash;

    /**
     * Flag is read.
     */
    @Field(
            type = FieldType.Boolean,
            index = FieldIndex.not_analyzed
    )
    private Boolean read;

    /**
     * Flag is favorite.
     */
    @Field(
            type = FieldType.Boolean,
            index = FieldIndex.not_analyzed
    )
    private Boolean favorite;

    /**
     * List of contact persons.
     */
    private List<ContactPerson> contactPersons;

    private List<Address> addresses;

    /**
     * Mail for sending application data.
     */
    private String applicationMail;

    /**
     * Homepages of organisation.
     */
    private List<String> homepages;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<ContactPerson> getContactPersons() {
        return contactPersons;
    }

    public void setContactPersons(List<ContactPerson> contactPersons) {
        this.contactPersons = contactPersons;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public String getApplicationMail() {
        return applicationMail;
    }

    public void setApplicationMail(String applicationMail) {
        this.applicationMail = applicationMail;
    }

    public List<String> getHomepages() {
        return homepages;
    }

    public void setHomepages(List<String> homepages) {
        this.homepages = homepages;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getContentHash() {
        return contentHash;
    }

    public void setContentHash(String contentHash) {
        this.contentHash = contentHash;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }
}
