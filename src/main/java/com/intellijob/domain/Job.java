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
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * This Domain object represent job links contains in mail context.
 */
@Document(collection = "jobs")
public class Job {

    /**
     * Object id.
     */
    @Id
    private String id;


    /**
     * Received date.
     */
    @Indexed
    private Date receivedDate;

    /**
     * Reference to jobLink.
     */
    @DBRef
    private JobLink jobLink;

    /**
     * Job content.
     */
    private String content;


    /**
     * Is content analysed and extracted.
     */
    private Boolean extracted;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public JobLink getJobLink() {
        return jobLink;
    }

    public void setJobLink(JobLink jobLink) {
        this.jobLink = jobLink;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean isExtracted() {
        return extracted;
    }

    public void setExtracted(Boolean extracted) {
        this.extracted = extracted;
    }
}
