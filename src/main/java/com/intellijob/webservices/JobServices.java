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

package com.intellijob.webservices;

import com.intellijob.controllers.JobController;
import com.intellijob.controllers.JobDetailController;
import com.intellijob.domain.Job;
import com.intellijob.domain.JobDetail;
import com.intellijob.dto.response.ResponseError;
import com.intellijob.dto.response.ResponseJobData;
import com.intellijob.dto.response.ResponseJobDetailData;
import com.intellijob.dto.response.ResponseJobTableData;
import com.intellijob.exceptions.JobLinkNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Job-Services.
 * <p>
 * Handle all request with endpoints <code>/jobs**</code>
 */
@RestController
public class JobServices extends BaseServices {

    private static final Logger LOG = LoggerFactory.getLogger(JobServices.class);

    @Autowired
    private JobController jobController;

    @Autowired
    private JobDetailController jobDetailController;


    /**
     * Returns job data for specified url.
     * <p>
     * New GET-request <code>url</code>
     * Save html response into jobs collection.
     *
     * @return job data.
     */
    @RequestMapping(value = Endpoints.JOBS, method = RequestMethod.GET)
    public ResponseJobTableData getAllJobs() {
        List<Job> jobs = jobController.findAll();
        //returns without job content, only metadata.
        return new ResponseJobTableData(jobs, Boolean.FALSE);
    }

    /**
     * Request Get all jobs with paging.
     *
     * @return data transfer object <code>ResponseJobTableData.java</code>
     */
    @RequestMapping(value = Endpoints.JOBS_PAGING, method = RequestMethod.GET)
    public @ResponseBody ResponseJobTableData getJobs(@PathVariable int pageIndex, @PathVariable int limit) {
        Page<Job> jobPage = jobController.findPage(pageIndex, limit);
        return new ResponseJobTableData(jobPage, Boolean.FALSE);
    }

    /**
     * Request Get all jobs with paging.
     *
     * @return data transfer object <code>ResponseJobTableData.java</code>
     */
    @RequestMapping(value = Endpoints.JOBS_BY_ID, method = RequestMethod.GET)
    public ResponseJobData getJobContent(@PathVariable String jobId) throws Exception {
        Job job = jobController.findById(jobId);
        return new ResponseJobData(job, Boolean.TRUE);
    }

    /**
     * Request to extract job details from job html content.
     * <p>
     * find job for specified jobId.
     * Extract job details from html context.
     * Sets extractedFlag of job documents to true. (This means extracted!)
     *
     * @return data transfer object <code>ResponseJobTableData.java</code>
     */
    @RequestMapping(value = Endpoints.JOBS_BY_ID, method = RequestMethod.PUT)
    public ResponseJobDetailData extractJobContent(@PathVariable String jobId) throws Exception {
        Job job = jobController.findById(jobId);
        JobDetail extractedJobDetails = jobDetailController.extractJobDetailAndSave(job);
        return new ResponseJobDetailData(extractedJobDetails);
    }

    /**
     * Request to extract html content from all jobs.
     */
    @RequestMapping(value = Endpoints.JOBS, method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void extractAllJobContents() throws Exception {
        jobDetailController.extractJobs();
    }

    /**
     * Request to extract html content from all jobs.
     */
    @RequestMapping(value = Endpoints.JOBS, method = RequestMethod.PATCH)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateExtractFlag(@RequestParam(value = "extracted") Boolean value) throws Exception {
        if (value != null) {
            List<Job> allJobs = jobController.findAll();
            jobController.setExtractedFlag (allJobs, value);
        }
    }

    /**
     * Convert document not found exceptions to http status not found.
     *
     * @param e document not found exception.
     *
     * @return data transfer object <code>ResponseError.class</code> with status 404.
     */
    @ExceptionHandler(JobLinkNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public @ResponseBody ResponseError handleException(JobLinkNotFoundException e) {
        LOG.error(e.getError().getMessage(), e);
        return handleException(HttpStatus.NOT_FOUND, e);
    }

    /**
     * Request to delete a job by specified id.
     *
     * @return data transfer object <code>ResponseJobData</code>
     */
    @RequestMapping(value = Endpoints.JOBS_BY_ID, method = RequestMethod.DELETE)
    public ResponseJobData deleteJob(@PathVariable String jobId) throws Exception {
        jobController.deleteById(jobId);
        return new ResponseJobData(jobId);
    }
}
