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

package com.intellijob.dto;

import com.intellijob.domain.Job;

import java.util.Date;

/**
 * Data transfer object represents domain object <code>JobHtml</code>
 */
public class ResponseJobData extends ResponseData {

    private String jobId;

    /**
     * Received date.
     */
    private Date receivedDate;

    /**
     * Link to job content.
     */
    private String link;

    /**
     * Job content.
     */
    private String content;


    public ResponseJobData() {
    }

    public ResponseJobData(Job job) {
        this.jobId = job.getId();
        this.receivedDate = job.getReceivedDate();
        this.link = job.getJobLink().getHref();
        this.content = job.getContent();
    }

    /**
     * Returns job id.
     *
     * @return job id.
     */
    public String getJobId() {
        return jobId;
    }

    /**
     * Sets job id.
     *
     * @param jobId job id.
     */
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    /**
     * Returns received date.
     * <p>
     * Date of link received date.
     *
     * @return date.
     */
    public Date getReceivedDate() {
        return receivedDate;
    }

    /**
     * Sets received date.
     *
     * @param receivedDate date.
     */
    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    /**
     * Returns link to job.
     *
     * @return link to job.
     */
    public String getLink() {
        return link;
    }

    /**
     * Sets Link to Job.
     *
     * @param link link to job.
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Returns job content.
     *
     * @return job content.
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
}
