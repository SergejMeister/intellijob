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

package com.intellijob.controllers.impl;

import com.intellijob.controllers.JobController;
import com.intellijob.domain.Job;
import com.intellijob.domain.JobLink;
import com.intellijob.repository.JobLinkRepository;
import com.intellijob.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This is a implementation of interface <code>JobController</code>.
 */
@Controller
public class JobControllerImpl implements JobController {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobLinkRepository jobLinkRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Job save(Job job) {
        jobRepository.save(job);
        return job;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Job createJobAndMarkLinkAsDownloaded(JobLink jobLink, String jobContent) {
        //create and save job
        Job job = init(jobLink, jobContent);
        jobRepository.save(job);

        //mark jobLink as downloaded.
        jobLink.setDownloaded(Boolean.TRUE);
        jobLinkRepository.save(jobLink);

        return job;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Job init(JobLink jobLink, String jobContent) {
        Job job = new Job();
        job.setReceivedDate(jobLink.getReceivedDate());
        job.setContent(jobContent);
        job.setJobLink(jobLink);
        return job;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Job> createJobAndMarkLinkAsDownloaded(Map<JobLink, String> jobLinksWithJobContent) {
        List<Job> jobsToSave = new ArrayList<>();
        List<JobLink> jobLinksToSave = new ArrayList<>();
        for (Map.Entry<JobLink, String> entry : jobLinksWithJobContent.entrySet()) {
            JobLink jobLink = entry.getKey();
            jobLink.setDownloaded(Boolean.TRUE);

            String jobContent = entry.getValue();
            Job job = init(jobLink, jobContent);
            jobsToSave.add(job);

            jobLinksToSave.add(jobLink);
        }

        jobRepository.save(jobsToSave);
        jobLinkRepository.save(jobLinksToSave);

        return jobsToSave;
    }
}
