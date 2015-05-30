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
import com.intellijob.domain.JobLink;

import java.util.List;
import java.util.Map;

/**
 * This is a controller interface for job operations.
 */
public interface JobController {

    /**
     * Save job object.
     *
     * @param job job to save.
     *
     * @return the stored job object.
     */
    Job save(Job job);

    /**
     * Create new job object and mark job link as downloaded = true.
     *
     * @param jobLink    job link.
     * @param jobContent job content.
     *
     * @return new job object with id.
     */
    Job createJobAndMarkLinkAsDownloaded(JobLink jobLink, String jobContent);

    /**
     * Init job without id.
     *
     * @param jobLink    job link.
     * @param jobContent job content.
     *
     * @return new job object without id.
     */
    Job init(JobLink jobLink, String jobContent);

    /**
     * Create new job object and mark job link as downloaded = true.
     *
     * @param jobLinksWithJobContent map with jobLink as key and downloaded content.
     *
     * @return list of created jobs.
     */
    List<Job> createJobAndMarkLinkAsDownloaded(Map<JobLink, String> jobLinksWithJobContent);

    /**
     * Returns all jobs.
     *
     * @return lits of jobs.
     */
    List<Job> findAll();

}
