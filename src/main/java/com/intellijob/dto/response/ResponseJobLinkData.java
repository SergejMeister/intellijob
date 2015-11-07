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

import com.intellijob.domain.JobLink;

import java.util.Date;

/**
 * Data transfer object represents domain object <code>JobLink</code>
 */
public class ResponseJobLinkData extends ResponseData {

    /**
     * JobLink id.
     */
    private String jobLinkId;

    /**
     * This is href link in html a tag.
     * <p>
     * Example: <a href="testhref">Demolabel</a>.
     * href=testhref.
     */
    private String link;

    /**
     * This is value in html a tag.
     * <p>
     * Example: <a href="testhref">DemoValue</a>.
     * value=DemoValue.
     */
    private String value;

    /**
     * By mail is from address.
     */
    private String source;

    /**
     * Receive date.
     */
    private Date receivedDate;

    /**
     * Download flag.
     */
    private Boolean downloaded;


    public ResponseJobLinkData(JobLink jobLink) {
        this.jobLinkId = jobLink.getId();
        this.link = jobLink.getHref();
        this.value = jobLink.getValue();
        this.receivedDate = jobLink.getMail().getReceivedDate();
        this.source = jobLink.getMail().getSentAddress();
        this.downloaded = jobLink.isDownloaded();
    }

    public ResponseJobLinkData(String jobLinkId) {
        this.jobLinkId = jobLinkId;
    }

    /**
     * Returns id of jobLink.
     *
     * @return jobLink id.
     */
    public String getJobLinkId() {
        return jobLinkId;
    }

    /**
     * Sets jobLink id.
     *
     * @param jobLinkId jobLink id.
     */
    public void setJobLinkId(String jobLinkId) {
        this.jobLinkId = jobLinkId;
    }

    /**
     * Returns link.
     *
     * @return link.
     */
    public String getLink() {
        return link;
    }

    /**
     * Sets link.
     *
     * @param link link.
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Returns link value.
     *
     * @return link value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets link value.
     *
     * @param value link value.
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Returns source.
     * <p>
     * By mail source is from address.
     *
     * @return source.
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets source.
     *
     * @param source source.
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Receive date of link.
     *
     * @return receive date of link.
     */
    public Date getReceivedDate() {
        return receivedDate;
    }

    /**
     * Sets date.
     *
     * @param receivedDate date.
     */
    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    /**
     * Is downloaded!
     *
     * @return downloaded.
     */
    public Boolean getDownloaded() {
        return downloaded;
    }

    /**
     * Sets download flag.
     *
     * @param downloaded downloaded flag.
     */
    public void setDownloaded(Boolean downloaded) {
        this.downloaded = downloaded;
    }
}
