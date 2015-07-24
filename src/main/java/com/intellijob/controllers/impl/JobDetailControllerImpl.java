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

import civis.com.utils.opennlp.ContactPersonFinder;
import civis.com.utils.opennlp.ContactPersonSpan;
import civis.com.utils.opennlp.ModelFactory;
import com.intellijob.controllers.JobDetailController;
import com.intellijob.domain.Job;
import com.intellijob.domain.JobDetail;
import com.intellijob.exceptions.DocumentNotFoundException;
import com.intellijob.models.HtmlLink;
import com.intellijob.repository.JobDetailRepository;
import com.intellijob.utility.HtmlParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This is a implementation of interface <code>JobDetailController</code>.
 */
@Controller
public class JobDetailControllerImpl implements JobDetailController {

    private static final Logger LOG = LoggerFactory.getLogger(JobDetailControllerImpl.class);

    @Autowired
    private JobDetailRepository jobDetailRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public JobDetail save(JobDetail jobDetail) {
        jobDetailRepository.save(jobDetail);
        return jobDetail;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobDetail extractJobDetail(Job job) {
        String htmlContent = job.getContent();
        String plainText = HtmlParser.parseText(htmlContent);
        ContactPersonFinder contactPersonFinder = ModelFactory.getContactPersonFinder();
        List<ContactPersonSpan> contactPersonSpans = contactPersonFinder.find(plainText);
        JobDetail jobDetail = new JobDetail(job, contactPersonSpans);

        // find mails
        String mails = HtmlParser.parseEmail(plainText);
        jobDetail.setApplicationMail(mails);

        // find home pages
        List<String> foundedHomepages = HtmlParser.extractUrls(plainText);
        jobDetail.setHomepages(foundedHomepages);

        return jobDetail;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobDetail extractJobDetailAndSave(Job job) {
        JobDetail jobDetail = extractJobDetail(job);
        return save(jobDetail);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<JobDetail> extractJobDetailAndSave(List<Job> jobs) {
        List<JobDetail> toSave = new ArrayList<>();
        Set<String> links = new HashSet<>();
        for (Job job : jobs) {
            if (job.getJobLink() != null) {
                if (links.contains(job.getJobLink().getHref())) {
                    //TODO remove this check after bug fix with duplicated links!
                    LOG.warn(
                            "Duplicated link exception. After bug fix with duplicated links, this exception should be removed!");
                } else {
                    JobDetail jobDetail = extractJobDetail(job);
                    toSave.add(jobDetail);
                    //TODO remove link unique check - after bug fix with duplicated links!
                    links.add(job.getJobLink().getHref());
                }
            } else {
                LOG.error("JobLink is null. How can it be true? JobId: " + job.getId());
            }
        }
        return jobDetailRepository.save(toSave);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<JobDetail> findAll() {
        return jobDetailRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<JobDetail> findPage(int pageIndex, int limit) {
        PageRequest request = new PageRequest(pageIndex, limit, new Sort(Sort.Direction.DESC, "receivedDate"));
        return jobDetailRepository.findAll(request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobDetail findById(String jobDetailId) throws DocumentNotFoundException {
        JobDetail foundedJobDetail = jobDetailRepository.findOne(jobDetailId);
        if(foundedJobDetail == null){
            throw new DocumentNotFoundException();
        }

        return foundedJobDetail;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobDetail findAndConvertContentToText(String jobDetailId) throws DocumentNotFoundException {
        JobDetail foundedJobDetail = findById(jobDetailId);
        String htmlContent = foundedJobDetail.getContent();
        String plainText = HtmlParser.parseText(htmlContent);
        foundedJobDetail.setContent(plainText);
        return foundedJobDetail;
    }


}
