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

import com.intellijob.domain.Address;
import com.intellijob.domain.ContactPerson;
import com.intellijob.domain.JobDetail;
import com.intellijob.elasticsearch.domain.EsJobDetail;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * Data transfer object represents domain object <code>JobDetail</code>
 */
public class ResponseJobDetailData extends ResponseData {

    public static final String BLANK_CHAR = " ";

    private String jobDetailId;

    /**
     * Job name.
     */
    private String name;

    /**
     * Received date.
     */
    private Date receivedDate;

    /**
     * Link to job content.
     */
    private String link;

    /**
     * Contact person.
     * <p>
     * If more than one, than separate by comma ( , ) .
     * Example: Kristina Oss, Stefan Mann.
     */
    private String contactPerson;

    /**
     * Organisation
     */
    private String organisation;

    /**
     * Address of organisation.
     * <p>
     * If more than one, than separate by comma ( , ) .
     * Example: Reamurstr. 13 12207 Berlin, Brandenburger Tor 1 12345 Berlin.
     */
    private String address;

    /**
     * All jobs requirements separate by comma (,).
     */
    private String requirements;

    /**
     * Homepage of organisation.
     * <p>
     * More than one, than separate by comma.
     */
    private String homepage;

    /**
     * More than one, than separate by comma.
     */
    private String applicationMail;

    /**
     * Some jobs have a application web form.
     * This value to show the link to application web form.
     */
    private String linkToApplicationWebForm;

    /**
     * Application period.
     */
    private String applicationPeriod;

    /**
     * Job content.
     */
    private String content;

    /**
     * Job Id.
     */
    private String jobId;

    /**
     * Read flag.
     */
    private boolean read;

    /**
     * Favorite flag.
     */
    private boolean favorite;

    public ResponseJobDetailData(String jobDetailId) {
        setJobDetailId(jobDetailId);
    }

    public ResponseJobDetailData(JobDetail jobDetail) {
        setJobDetailId(jobDetail.getId());
        setJobId(jobDetail.getJobId());
        setName(jobDetail.getName());
        setReceivedDate(jobDetail.getReceivedDate());
        setLink(jobDetail.getLink());
        setApplicationMail(jobDetail.getApplicationMail());
        setRead(jobDetail.isRead());
        setFavorite(jobDetail.isFavorite());
        this.contactPerson = initContactPersons(jobDetail.getContactPersons());
        this.address = initAddresses(jobDetail.getAddresses());
        this.homepage = initHomePage(jobDetail.getHomepages());
    }

    public ResponseJobDetailData(JobDetail jobDetail, Boolean hasContent) {
        this(jobDetail);
        if (hasContent) {
            this.content = jobDetail.getContent();
        }
    }

    public ResponseJobDetailData(EsJobDetail jobDetail, Boolean hasContent) {
        setJobDetailId(jobDetail.getId());
        setJobId(jobDetail.getJobId());
        setName(jobDetail.getName());
        setReceivedDate(jobDetail.getReceivedDate());
        setLink(jobDetail.getLink());
        setApplicationMail(jobDetail.getApplicationMail());
        setRead(jobDetail.getRead());
        setFavorite(jobDetail.getFavorite());
        this.contactPerson = initContactPersons(jobDetail.getContactPersons());
        this.address = initAddresses(jobDetail.getAddresses());
        this.homepage = initHomePage(jobDetail.getHomepages());
        if (hasContent) {
            this.content = jobDetail.getContent();
        }
    }

    private String initContactPersons(List<ContactPerson> contactPersons) {
        StringBuilder sb = new StringBuilder();
        for (ContactPerson contactPerson : contactPersons) {
            if (StringUtils.hasLength(contactPerson.getSex())) {
                if (ContactPerson.SEX_PREFIX_MAN.equals(contactPerson.getSex())) {
                    sb.append("Herr ");
                } else if (ContactPerson.SEX_PREFIX_WOMEN.equals(contactPerson.getSex())) {
                    sb.append("Frau ");
                } else {
                    //don't append for unknown :)
                }
            }
            sb.append(contactPerson.getFirstAndSecondName());
            sb.append(",");
            sb.append(BLANK_CHAR);
        }
        //remove last comma and blank.
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }

        return sb.toString();
    }

    private String initAddresses(List<Address> addresses) {
        StringBuilder sb = new StringBuilder();
        for (Address address : addresses) {
            sb.append(address.getAll());
            sb.append(",");
            sb.append(BLANK_CHAR);
        }
        //remove last comma and blank.
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }

        return sb.toString();
    }

    private String initHomePage(List<String> homepages) {
        StringBuilder homePageBuilder = new StringBuilder();
        for (String homepage : homepages) {
            homePageBuilder.append(homepage);
            homePageBuilder.append(",");
            homePageBuilder.append(BLANK_CHAR);
        }
        //remove last comma and blank.
        if (homePageBuilder.length() > 0) {
            homePageBuilder.setLength(homePageBuilder.length() - 2);
        }

        return homePageBuilder.toString();
    }

    /**
     * Returns id of jobDetail.
     *
     * @return id of jobDetail.
     */
    public String getJobDetailId() {
        return jobDetailId;
    }

    public void setJobDetailId(String jobDetailId) {
        this.jobDetailId = jobDetailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
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

    public String getLinkToApplicationWebForm() {
        return linkToApplicationWebForm;
    }

    public void setLinkToApplicationWebForm(String linkToApplicationWebForm) {
        this.linkToApplicationWebForm = linkToApplicationWebForm;
    }

    public String getApplicationPeriod() {
        return applicationPeriod;
    }

    public void setApplicationPeriod(String applicationPeriod) {
        this.applicationPeriod = applicationPeriod;
    }

    /**
     * Returns job content.
     * <p>
     * <p>
     * NOTE: Content can be in html of plain text!
     * </p>
     *
     * @return job content.
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets job content.
     * <p>
     * NOTE: Content can be in html of plain text!
     * </p>
     *
     * @param content content.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Returns jobId of this details.
     *
     * @return job id.
     */
    public String getJobId() {
        return jobId;
    }

    /**
     * Sets jobId.
     *
     * @param jobId jobId.
     */
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
