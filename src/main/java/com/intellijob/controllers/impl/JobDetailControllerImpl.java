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


import com.civis.utils.html.parser.HtmlParseFilter;
import com.civis.utils.html.parser.HtmlParser;
import com.civis.utils.opennlp.models.ModelFactory;
import com.civis.utils.opennlp.models.address.AddressSpan;
import com.civis.utils.opennlp.models.contactperson.ContactPersonSpan;
import com.intellijob.controllers.JobController;
import com.intellijob.controllers.JobDetailController;
import com.intellijob.domain.Job;
import com.intellijob.domain.JobDetail;
import com.intellijob.domain.builder.JobDetailBuilder;
import com.intellijob.elasticsearch.domain.EsJobDetail;
import com.intellijob.elasticsearch.repository.EsJobDetailRepository;
import com.intellijob.exceptions.BaseException;
import com.intellijob.exceptions.DocumentNotFoundException;
import com.intellijob.repository.JobDetailRepository;
import com.intellijob.repository.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is a implementation of interface <code>JobDetailController</code>.
 */
@Controller
public class JobDetailControllerImpl implements JobDetailController {

    private static final Logger LOG = LoggerFactory.getLogger(JobDetailControllerImpl.class);

    @Autowired
    private JobDetailRepository jobDetailRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobController jobController;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private EsJobDetailRepository esJobDetailRepository;

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
        String plainText = new HtmlParser(htmlContent).toPlainText().getContent();

        //Find contact persons in text
        List<ContactPersonSpan> contactPersonSpans = ModelFactory.getContactPersonFinder().find(plainText);

        //find addresses in text
        List<AddressSpan> addressSpans = ModelFactory.getAddressFinder().find(plainText);

        // find mails in text
        String mails = new HtmlParser(htmlContent).parse().getMail();

        // find home pages
        HtmlParseFilter htmlParseFilter = new HtmlParseFilter();
        htmlParseFilter.setNullableText(Boolean.FALSE);
        htmlParseFilter.setIgnore(
                Arrays.asList("www.tecoloco.com", "www.irishjobs.ie", "www.estascontratado.com", "www.myjob.mu",
                        "www.caribbeanjobs.com", "www.nijobs.com", "www.jobs.lu", "www.pnet.co.za"));
        List<String> foundedHomepages = new HtmlParser(htmlContent, htmlParseFilter).toPlainText().parse().getUrls();


        return new JobDetailBuilder(job).setApplicationMail(mails).setHomepages(
                foundedHomepages).addContactPersons(contactPersonSpans).addAddresses(addressSpans).build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobDetail extractJobDetailAndSave(Job job) {
        JobDetail jobDetail = extractJobDetail(job);
        JobDetail persistedJobDetail = save(jobDetail);
        jobController.setExtractedFlag(job, Boolean.TRUE);
        return persistedJobDetail;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<JobDetail> extractJobDetailAndSave(List<Job> jobs) {
        List<JobDetail> result = new ArrayList<>();
        for (Job job : jobs) {
            JobDetail jobDetail = extractJobDetail(job);
            try {
                jobDetailRepository.save(jobDetail);
                result.add(jobDetail);
            } catch (DuplicateKeyException dke) {
                LOG.debug("Duplicated Link Exception. Should not be happen! Link: {}", dke.getMessage());
            }
        }
        jobController.setExtractedFlag(jobs, Boolean.TRUE);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<JobDetail> extractJobs() {
        List<Job> jobsToExtract = jobRepository.findByExtracted(Boolean.FALSE);
        return extractJobDetailAndSave(jobsToExtract);
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
    public JobDetail findById(String jobDetailId) throws BaseException {
        JobDetail foundedJobDetail = jobDetailRepository.findOne(jobDetailId);
        if (foundedJobDetail == null) {
            throw new DocumentNotFoundException();
        }

        return foundedJobDetail;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobDetail findAndConvertContentToText(String jobDetailId) throws BaseException {
        JobDetail foundedJobDetail = findById(jobDetailId);
        String htmlContent = foundedJobDetail.getContent();
        String plainText = new HtmlParser(htmlContent).toPlainText().getContent();
        foundedJobDetail.setContent(plainText);
        return foundedJobDetail;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobDetail deleteById(String jobDetailId) throws BaseException {
        JobDetail jobDetailToDelete = findById(jobDetailId);
        jobDetailRepository.delete(jobDetailToDelete);
        return jobDetailToDelete;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createElasticserchIndexes() {
        List<JobDetail> jobDetails = jobDetailRepository.findAll();
        for (JobDetail jobDetail : jobDetails) {
            EsJobDetail esJobDetail = new EsJobDetail();
            esJobDetail.setId(jobDetail.getId());
            esJobDetail.setName(jobDetail.getName());
            esJobDetail.setJobId(jobDetail.getJobId());
            esJobDetail.setAddresses(jobDetail.getAddresses());
            esJobDetail.setApplicationMail(jobDetail.getApplicationMail());
            esJobDetail.setContactPersons(jobDetail.getContactPersons());
            String htmlContent = jobDetail.getContent();
            String plainText = new HtmlParser(htmlContent).toPlainText().getContent();
            esJobDetail.setContent(plainText);
            esJobDetail.setHomepages(jobDetail.getHomepages());
            esJobDetail.setLink(jobDetail.getLink());

            esJobDetailRepository.index(esJobDetail);
        }
    }
}
