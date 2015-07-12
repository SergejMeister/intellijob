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

package com.intellijob.controllers;

import com.intellijob.domain.Job;
import com.intellijob.domain.JobDetail;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * This is a controller interface for job detail operations.
 */
public interface JobDetailController {

    /**
     * Save job details.
     *
     * @param jobDetail job detail to save.
     *
     * @return jobDetail document with id.
     */
    JobDetail save(JobDetail jobDetail);

    /**
     * Extract job details from job content.
     * <p>
     * Convert html content to plain text and extract information from job content.
     * This method use addons for openNlp to extract information.
     *
     * @param job job with content in html form.
     *
     * @return document job details.
     */
    JobDetail extractJobDetail(Job job);

    /**
     * Extract job details from job content and save in db.
     * <p>
     * execute <code>extractJobDetail(job)</code>
     * execute <code>save(jobDetail)</code>
     *
     * @param job job with content in html form.
     *
     * @return jobDetail document with id.
     */
    JobDetail extractJobDetailAndSave(Job job);

    /**
     * Extract job details from job content and save in db.
     * <p>
     * execute <code>extractJobDetail(job)</code>
     * execute <code>save(jobDetail)</code>
     *
     *@param jobs list of affected jobs.
     *
     * @return jobDetail document with id.
     */
    List<JobDetail> extractJobDetailAndSave(List<Job> jobs);


    /**
     * Returns all jobDetails.
     *
     * @return list of JobDetails.
     */
    List<JobDetail> findAll();

    /**
     * Returns page of jobDetail.
     *
     * @param pageIndex page index.
     * @param limit     limit items per page.
     *
     * @return page of jobDetail.
     */
    Page<JobDetail> findPage(int pageIndex, int limit);
}
