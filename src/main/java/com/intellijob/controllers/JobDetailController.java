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
import com.intellijob.elasticsearch.SearchModel;
import com.intellijob.elasticsearch.domain.EsJobDetail;
import com.intellijob.exceptions.BaseException;
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
     * @param jobs list of affected jobs.
     *
     * @return jobDetail document with id.
     */
    List<JobDetail> extractJobDetailAndSave(List<Job> jobs);

    /**
     * Find all not extracted jobs and extract this.
     * <p>
     * execute <code>findJobsByExtracted(False)</code>
     * execute <code>extractJobDetailAndSave(List)</code>
     *
     * @return jobDetail document with id.
     */
    List<JobDetail> extractJobs();


    /**
     * Returns all jobDetails.
     *
     * @return list of JobDetails.
     */
    List<JobDetail> findAll();

//    /**
//     * Returns page of jobDetail.
//     *
//     * @param pageIndex page index.
//     * @param limit     limit items per page.
//     *
//     * @return page of jobDetail.
//     */
//    Page<JobDetail> findPage(int pageIndex, int limit);

//    /**
//     * Returns page of jobDetail.
//     *
//     * @param pageIndex page index.
//     * @param limit     limit items per page.
//     *
//     * @return page of jobDetail.
//     */
//    Page<EsJobDetail> findAndSort(User user, int pageIndex, int limit);

//    /**
//     * Returns page of jobDetail.
//     *
//     * @param pageIndex page index.
//     * @param limit     limit items per page.
//     *
//     * @return page of jobDetail.
//     */
//    Page<EsJobDetail> findAndSort(User user, String searchFilter, int pageIndex, int limit);

//    /**
//     * Returns page of jobDetail.
//     *
//     * @param pageIndex page index.
//     * @param limit     limit items per page.
//     *
//     * @return page of jobDetail.
//     */
//    Page<EsJobDetail> findAndSort(User user, SearchEngineEnum searchEngine, int pageIndex, int limit);

    /**
     * Returns jobDetail for specified id.
     *
     * @param jobDetailId specified job detail id.
     *
     * @return founded jobDetail.
     */
    JobDetail findById(String jobDetailId) throws BaseException;

    /**
     * Find jobDetail for specified id and convert html content to plain text.
     *
     * @param jobDetailId specified job detail id.
     *
     * @return founded jobDetail with content as text.
     */
    JobDetail findAndConvertContentToText(String jobDetailId) throws BaseException;

    /**
     * Delete jobDetail for specified id.
     *
     * @param jobDetailId id of JobDetail to delete.
     *
     * @return deleted jobDetail.
     */
    JobDetail deleteById(String jobDetailId) throws BaseException;

    /**
     * Create indexes of all JobDetails.
     */
    void createElasticsearchIndexes();

    /**
     * Find and Sort all JobDetails for specified searchModel.
     *
     * @param searchModel searchModel.
     *
     * @return page of JobDetails.
     */
    Page<EsJobDetail> findAndSort(SearchModel searchModel);
}
