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
import com.intellijob.controllers.JobLinkController;
import com.intellijob.domain.Job;
import com.intellijob.domain.JobDetail;
import com.intellijob.domain.JobLink;
import com.intellijob.dto.response.DownloadResultData;
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
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Job-Services.
 * <p>
 * Handle all request with endpoints <code>/jobs**</code>
 */
@RestController
public class JobServices extends BaseServices {

    public static final Integer DOWNLOAD_AND_SAVE_COUNTER = 10;
    private static final Logger LOG = LoggerFactory.getLogger(JobServices.class);

    @Autowired
    private JobLinkController jobLinkController;

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
    public @ResponseBody ResponseJobTableData getAllJobs() {
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
    public @ResponseBody ResponseJobData getJobContent(@PathVariable String jobId) throws Exception {
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
    @RequestMapping(value = Endpoints.JOBS_BY_ID_EXTRACT, method = RequestMethod.POST)
    public @ResponseBody ResponseJobDetailData extractJobContent(@PathVariable String jobId) throws Exception {
        Job job = jobController.findById(jobId);
        JobDetail extractedJobDetails = jobDetailController.extractJobDetailAndSave(job);
        return new ResponseJobDetailData(extractedJobDetails);
    }


    /**
     * Returns job data for specified link defined in jobLink object.
     * <p>
     * New GET-request <code>jobLink.getHref()</code>
     * Save html response into jobs collection.
     * <p>
     * //@param jobLinkId object id of jobLink.
     *
     * @return job data.
     */
    @RequestMapping(value = Endpoints.JOBS_BY_JOBLINK_ID_DOWNLOAD, method = RequestMethod.POST)
    public @ResponseBody ResponseJobData downloadByJobLinkId(@PathVariable String jobLinkId) throws Exception {
        JobLink jobLink = jobLinkController.findById(jobLinkId);

        //Get-JOB-Content
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        try {
            String jobContent = restTemplate.getForObject(jobLink.getHref(), String.class);
            //Create new job and mark jobLink as downloaded=true.
            Job newJob = jobController.createJobAndMarkLinkAsDownloaded(jobLink, jobContent);
            return new ResponseJobData(newJob);
        } catch (HttpClientErrorException httpExc) {
            handleHttpClientErrorException(httpExc, jobLink);
            throw new JobLinkNotFoundException();
        }
    }

    private void handleHttpClientErrorException(HttpClientErrorException httpExc, JobLink jobLink) {
        if (httpExc.getStatusCode().equals(HttpStatus.NOT_FOUND) || httpExc.getStatusCode().equals(HttpStatus.GONE)) {
            //This job link is not available any more!
            jobLinkController.removeJobLink(jobLink);
        } else {
            LOG.error(
                    "Not handle HttpClientException with status: " + httpExc.getStatusCode() + " for JobLink with id" +
                            jobLink.getId());
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
     * Returns job data for specified link defined in jobLink object.
     * <p>
     * New GET-request <code>jobLink.getHref()</code>
     * Save html response into jobs collection.
     *
     * @return job data.
     */
    @RequestMapping(value = Endpoints.JOBS_DOWNLOAD, method = RequestMethod.POST)
    public @ResponseBody DownloadResultData downloadAll() throws Exception {
        List<JobLink> jobLinksToDownlaod = jobLinkController.findToDownload();

        RestTemplate restTemplate = new RestTemplate();
        List<JobLink> jobLinksPaging = new ArrayList<>();
        DownloadResultData downloadResultData = new DownloadResultData();
        LOG.debug("Download {} jobLinks.", jobLinksToDownlaod.size());
        for (JobLink jobLink : jobLinksToDownlaod) {
            if (jobLinksToDownlaod.size() < DOWNLOAD_AND_SAVE_COUNTER) {
                return downloadByPaging(restTemplate, jobLinksToDownlaod);
            }

            if (jobLinksPaging.size() == DOWNLOAD_AND_SAVE_COUNTER) {
                DownloadResultData downloadResultDataTemp = downloadByPaging(restTemplate, jobLinksPaging);
                downloadResultData.add(downloadResultDataTemp);
                jobLinksPaging.clear();
            } else {
                jobLinksPaging.add(jobLink);
            }
        }
        return downloadResultData;
    }

    private DownloadResultData downloadByPaging(RestTemplate restTemplate, List<JobLink> jobLinks) {
        LOG.info("Download an save page of JobLinks. Default paging is {}.", DOWNLOAD_AND_SAVE_COUNTER.toString());
        int notFoundedCount = 0;
        Map<JobLink, String> jobLinksWithJobContent = new HashMap<>();
        for (JobLink jobLink : jobLinks) {
            try {
                String jobContent = restTemplate.getForObject(jobLink.getHref(), String.class);
                jobLinksWithJobContent.put(jobLink, jobContent);
            } catch (HttpClientErrorException httpExc) {
                notFoundedCount++;
                handleHttpClientErrorException(httpExc, jobLink);
            }
        }

        List<Job> newJobs = jobController.createJobAndMarkLinkAsDownloaded(jobLinksWithJobContent);
        int successfullyDownloaded = newJobs.size();

        return new DownloadResultData(successfullyDownloaded, notFoundedCount);
    }

    /**
     * Request to delete a job by specified id.
     *
     * @return data transfer object <code>ResponseJobData</code>
     */
    @RequestMapping(value = Endpoints.JOBS_BY_ID, method = RequestMethod.DELETE)
    public @ResponseBody ResponseJobData deleteJob(@PathVariable String jobId) throws Exception {
        jobController.deleteById(jobId);
        return new ResponseJobData(jobId);
    }
}
