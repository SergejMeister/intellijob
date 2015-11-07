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

import com.intellijob.domain.Job;
import com.intellijob.domain.JobLink;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * Data transfer object represents domain object <code>Job</code>
 */
public class ResponseJobTableData extends ResponseTableData {

    private List<ResponseJobData> jobs;
    private List<ResponseJobLinkData> deletedJobLinks;

    /**
     * Empty Response constructor.
     */
    public ResponseJobTableData() {
        init();
    }

    public ResponseJobTableData(List<Job> listOfJob, Boolean hasContent) {
        init();
        for (Job job : listOfJob) {
            ResponseJobData responseJobData = new ResponseJobData(job, hasContent);
            jobs.add(responseJobData);
        }
    }

    public ResponseJobTableData(List<Job> listWithDomainObjects, List<JobLink> listOfJobLinks, Boolean hasContent) {
        init();
        for (Job job : listWithDomainObjects) {
            ResponseJobData responseJobData = new ResponseJobData(job, hasContent);
            jobs.add(responseJobData);
        }

        for (JobLink jobLink : listOfJobLinks) {
            ResponseJobLinkData responseJobLinkData = new ResponseJobLinkData(jobLink);
            deletedJobLinks.add(responseJobLinkData);
        }
    }

    public ResponseJobTableData(Page<Job> jobPage, Boolean hasContent) {
        super(jobPage);
        init();
        for (Job job : jobPage.getContent()) {
            ResponseJobData responseJobData = new ResponseJobData(job, hasContent);
            this.jobs.add(responseJobData);
        }
    }

    private void init() {
        this.jobs = new ArrayList<>();
        this.deletedJobLinks = new ArrayList<>();
    }

    public List<ResponseJobData> getJobs() {
        return jobs;
    }

    public void setJobs(List<ResponseJobData> jobs) {
        this.jobs = jobs;
    }

    public List<ResponseJobLinkData> getDeletedJobLinks() {
        return deletedJobLinks;
    }

    public void setDeletedJobLinks(List<ResponseJobLinkData> deletedJobLinks) {
        this.deletedJobLinks = deletedJobLinks;
    }
}
