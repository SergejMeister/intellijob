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

import com.civis.utils.opennlp.models.contactperson.ContactPersonSpan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This Domain object represents job details.
 */
@Document(collection = "jobdetails")
public class JobDetail {

    /**
     * Object id.
     */
    @Id
    private String id;


    /**
     * Job name.
     */
    private String name;

    /**
     * Received date.
     */
    @Indexed
    private Date receivedDate;

    /**
     * Link unique.
     */
    @Indexed(unique = true)
    private String link;

    /**
     * Job content.
     */
    private String content;

    /**
     * List of contact persons.
     */
    private List<ContactPerson> contactPersons;

    /**
     * Mail for sending application data.
     */
    private String applicationMail;

    /**
     * Homepages of organisation.
     */
    private List<String> homepages;


    /**
     * Default constructor to init list of contact persons.
     */
    public JobDetail() {
        this.contactPersons = new ArrayList<>();
    }

    public JobDetail(Job job, List<ContactPersonSpan> contactPersonSpans) {
        this.name = job.getJobLink().getValue();
        this.receivedDate = job.getReceivedDate();
        this.link = job.getJobLink().getHref();
        this.content = job.getContent();
        this.contactPersons = new ArrayList<>();
        for (ContactPersonSpan contactPersonSpan : contactPersonSpans) {
            ContactPerson contactPerson = new ContactPerson(contactPersonSpan);
            this.contactPersons.add(contactPerson);
        }
    }

    /**
     * Returns document id.
     *
     * @return id.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets document id.
     *
     * @param id id.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns job name.
     *
     * @return job name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets job name.
     *
     * @param name job name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns receive date.
     *
     * @return receive date.
     */
    public Date getReceivedDate() {
        return receivedDate;
    }

    /**
     * Sets receive date.
     *
     * @param receivedDate receive date.
     */
    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    /**
     * Returns link to the job.
     * <p>Note: can not be active with the time!</p>
     *
     * @return link.
     */
    public String getLink() {
        return link;
    }

    /**
     * Sets link to the job.
     *
     * @param link job link.
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Returns job content in html form.
     *
     * @return job content in html form.
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets job content.
     *
     * @param content job content.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * List of contact persons.
     * <p>
     * Maybe more than one.
     *
     * @return list of contact persons.
     */
    public List<ContactPerson> getContactPersons() {
        return contactPersons;
    }

    /**
     * Sets list of contact persons.
     *
     * @param contactPersons
     */
    public void setContactPersons(List<ContactPerson> contactPersons) {
        this.contactPersons = contactPersons;
    }

    /**
     * Returns all founded company homepages.
     *
     * @return web links, like www.test.de, http://www.web.de, https://www.web.de.
     */
    public List<String> getHomepages() {
        return homepages;
    }

    /**
     * Sets company homepages.
     *
     * @param homepages homepages.
     */
    public void setHomepages(List<String> homepages) {
        this.homepages = homepages;
    }

    /**
     * Returns mail for sending job application.
     *
     * @return mail.
     */
    public String getApplicationMail() {
        return applicationMail;
    }

    /**
     * Sets mail for sending job application.
     *
     * @param applicationMail mail format.
     */
    public void setApplicationMail(String applicationMail) {
        this.applicationMail = applicationMail;
    }
}
