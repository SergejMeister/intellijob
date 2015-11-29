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

import com.intellijob.domain.Address;
import com.intellijob.domain.ContactPerson;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

/**
 * This document represents domain object <code>JobDetail</code>.
 */
@Document(indexName = "intellijob", type = "jobDetail", shards = 1, replicas = 0)
public class EsJobDetail {

    @Id
    private String id;

    /**
     * Job name.
     */
    //@Field(type = string, index = analyzed, store = true)
    private String name;

    /**
     * Link to the job page.
     */
    private String link;

    /**
     * Link unique.
     */
    //@Indexed(unique = true)
    private String jobId;

    /**
     * Job content.
     */
    private String content;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
}