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

package com.intellijob.domain.builder;

import com.civis.utils.opennlp.models.address.AddressSpan;
import com.civis.utils.opennlp.models.contactperson.ContactPersonSpan;
import com.intellijob.domain.Address;
import com.intellijob.domain.ContactPerson;
import com.intellijob.domain.Job;
import com.intellijob.domain.JobDetail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Utility class to build jobDetail object.
 */
public class JobDetailBuilder {

    /**
     * Object id.
     */
    private String id;


    /**
     * Job name.
     */
    private String name;

    /**
     * Received date.
     */
    private Date receivedDate;

    /**
     * Link unique.
     */
    private String link;

    /**
     * Link unique.
     */
    private String jobId;

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
     * Addresses of organisation.
     */
    private List<Address> addresses;

    /**
     * Constructor with job param.
     * <p>
     * set job id.
     * set job content.
     * set job name.
     * set received date.
     * set job href tag.
     */
    public JobDetailBuilder(Job job) {
        setJobId(job.getId());
        setContent(job.getContent());
        setName(job.getJobLink().getValue());
        setReceivedDate(job.getReceivedDate());
        setLink(job.getJobLink().getHref());
    }

    /**
     * Convert model contactPersonSpan to document contact person.
     *
     * @param contactPersonSpan open nlp span model.
     *
     * @return document ContactPerson
     */
    public static ContactPerson convert(ContactPersonSpan contactPersonSpan) {
        return new ContactPerson(contactPersonSpan.getFirstName(), contactPersonSpan.getSecondName(),
                contactPersonSpan.getSex());
    }

    /**
     * Convert model AddressSpan to document address.
     *
     * @param addressSpan open nlp span model.
     *
     * @return document address.
     */
    public static Address convert(AddressSpan addressSpan) {
        Address address = new Address();
        address.setStreet(addressSpan.getStreet());
        address.setStreetNumber(addressSpan.getStreetNumber());
        address.setZip(addressSpan.getZip());
        address.setCity(addressSpan.getCity());
        address.setCountry(addressSpan.getCountry());
        return address;
    }

    public JobDetailBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public JobDetailBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public JobDetailBuilder setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
        return this;
    }

    public JobDetailBuilder setLink(String link) {
        this.link = link;
        return this;
    }

    public JobDetailBuilder setJobId(String jobId) {
        this.jobId = jobId;
        return this;
    }

    public JobDetailBuilder setContent(String content) {
        this.content = content;
        return this;
    }

    public JobDetailBuilder addContactPersons(List<ContactPersonSpan> contactPersonSpans) {
        if (contactPersons == null) {
            contactPersons = new ArrayList<>();
        }

        for (ContactPersonSpan contactPersonSpan : contactPersonSpans) {
            ContactPerson contactPerson = convert(contactPersonSpan);
            this.contactPersons.add(contactPerson);
        }
        return this;
    }

    public JobDetailBuilder setApplicationMail(String applicationMail) {
        this.applicationMail = applicationMail;
        return this;
    }

    public JobDetailBuilder setHomepages(List<String> homepages) {
        this.homepages = homepages;
        return this;
    }

    public JobDetailBuilder addAddresses(List<AddressSpan> addressSpans) {
        if (addresses == null) {
            addresses = new ArrayList<>();
        }

        for (AddressSpan addressSpan : addressSpans) {
            Address address = convert(addressSpan);
            addresses.add(address);
        }

        return this;
    }

    public JobDetail build() {
        JobDetail jobDetail = new JobDetail();
        jobDetail.setId(id);
        jobDetail.setContent(content);
        jobDetail.setHomepages(homepages);
        jobDetail.setApplicationMail(applicationMail);
        jobDetail.setReceivedDate(receivedDate);
        jobDetail.setJobId(jobId);
        jobDetail.setLink(link);
        jobDetail.setName(name);
        jobDetail.setContactPersons(contactPersons);
        jobDetail.setAddresses(addresses);
        return jobDetail;
    }
}


