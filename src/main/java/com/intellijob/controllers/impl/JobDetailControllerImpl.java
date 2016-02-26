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
import com.intellijob.Constants;
import com.intellijob.controllers.JobController;
import com.intellijob.controllers.JobDetailController;
import com.intellijob.domain.Job;
import com.intellijob.domain.JobDetail;
import com.intellijob.domain.builder.JobDetailBuilder;
import com.intellijob.elasticsearch.SearchModel;
import com.intellijob.elasticsearch.domain.EsJobDetail;
import com.intellijob.elasticsearch.domain.EsUserSkills;
import com.intellijob.elasticsearch.repository.EsJobDetailRepository;
import com.intellijob.elasticsearch.repository.EsUserSkillsRepository;
import com.intellijob.elasticsearch.util.SearchQueryUtility;
import com.intellijob.exceptions.BaseException;
import com.intellijob.exceptions.DocumentNotFoundException;
import com.intellijob.repository.JobDetailRepository;
import com.intellijob.repository.JobRepository;
import com.intellijob.utility.HashUtility;
import com.intellijob.utility.ListUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * This is a implementation of interface <code>JobDetailController</code>.
 */
@Controller
public class JobDetailControllerImpl implements JobDetailController {

    public static final String STEPSTONE = "stepstone";

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


    @Autowired
    private EsUserSkillsRepository esUserSkillsRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public JobDetail save(JobDetail jobDetail) {
        jobDetailRepository.save(jobDetail);

        //always if you save the job create an index.
        createIndex(jobDetail);

        return jobDetail;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobDetail extractJobDetail(Job job) {
        String htmlContent = job.getContent();

        String plainText = htmlToPlaintText(htmlContent, job.getJobLink().getHref());

        //build hash of plain text from content
        String contentHash = HashUtility.hashAsString(plainText);

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

        return new JobDetailBuilder(job).setApplicationMail(mails).setHomepages(foundedHomepages)
                .addContactPersons(contactPersonSpans).addAddresses(addressSpans).setContentHash(contentHash).build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobDetail extractJobDetailAndSave(Job job) {
        JobDetail jobDetail = extractJobDetail(job);

        JobDetail similarityJob = jobDetailRepository.findByContentHash(jobDetail.getContentHash());
        if (similarityJob == null) {
            JobDetail persistedJobDetail = save(jobDetail);
            jobController.setExtractedFlag(job, Boolean.TRUE);
            return persistedJobDetail;
        } else {
            JobDetail mergedJobDetail = mergeJobDetails(jobDetail, similarityJob);
            save(mergedJobDetail);
            jobController.setExtractedFlag(job, Boolean.TRUE);
            return mergedJobDetail;
        }
    }

    private JobDetail mergeJobDetails(JobDetail newJobDetail, JobDetail oldJobDetail) {
        //set old id
        newJobDetail.setId(oldJobDetail.getId());
        newJobDetail.setFavorite(oldJobDetail.isFavorite());

        if (ListUtility.isBlank(newJobDetail.getAddresses())) {
            newJobDetail.setAddresses(oldJobDetail.getAddresses());
        }

        if (ListUtility.isBlank(newJobDetail.getContactPersons())) {
            newJobDetail.setContactPersons(oldJobDetail.getContactPersons());
        }

        if (ListUtility.isBlank(newJobDetail.getHomepages())) {
            newJobDetail.setHomepages(oldJobDetail.getHomepages());
        }

        if (newJobDetail.getApplicationMail() == null) {
            newJobDetail.setApplicationMail(oldJobDetail.getApplicationMail());
        }

        if (!isTimeToOverwrite(newJobDetail.getReceivedDate(), oldJobDetail.getReceivedDate())) {
            //don't overwrite, overwrite means marking as not read
            // if read is false, this jobDetail will be included into search engine.
            newJobDetail.setRead(oldJobDetail.isRead());
        }

        return newJobDetail;
    }

    /**
     * Check old date + 1 month is before new date.
     *
     * @param newReceivedDate new date.
     * @param oldReceivedDate old date.
     *
     * @return true if date interval more than one month.
     */
    private boolean isTimeToOverwrite(Date newReceivedDate, Date oldReceivedDate) {
        LocalDate oldDatePlusOneMonth =
                LocalDate.from(oldReceivedDate.toInstant().atZone(Constants.ZONE_ID_UTC).plusMonths(1));
        return oldDatePlusOneMonth.isBefore(LocalDate.from(newReceivedDate.toInstant().atZone(Constants.ZONE_ID_UTC)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<JobDetail> extractJobDetailAndSave(List<Job> jobs) {
        List<JobDetail> result = new ArrayList<>();
        for (Job job : jobs) {
            JobDetail jobDetail = extractJobDetail(job);
            JobDetail similarityJob = jobDetailRepository.findByContentHash(jobDetail.getContentHash());
            if (similarityJob == null) {
                try {
                    jobDetailRepository.save(jobDetail);
                    result.add(jobDetail);
                } catch (DuplicateKeyException dke) {
                    LOG.debug("Duplicated Key Exception. Should not be happen! Link: {}", dke.getMessage());
                }
                createIndex(jobDetail);
            } else {
                JobDetail mergedJobDetail = mergeJobDetails(jobDetail, similarityJob);
                jobDetailRepository.save(mergedJobDetail);
                result.add(mergedJobDetail);
                //Delete old index.
                esJobDetailRepository.delete(jobDetail.getId());
                createIndex(mergedJobDetail);
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
    public Page<EsJobDetail> findAndSort(SearchModel searchModel) {
        switch (searchModel.getSearchEngineEnum()) {
            case SIMPLE:
                return findUsingSimpleSearchEngine(searchModel);
            case COMPLEX:
                return findUsingPersonalSearchEngine(searchModel);
            default:
                return findEsPage(searchModel);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateReadState(List<String> ids, Boolean read) {
        Iterable<JobDetail> jobDetails = jobDetailRepository.findAll(ids);
        for (JobDetail jobDetail : jobDetails) {
            jobDetail.setRead(read);
        }
        jobDetailRepository.save(jobDetails);

        Iterable<EsJobDetail> indexes = esJobDetailRepository.findAll(ids);
        for (EsJobDetail index : indexes) {
            index.setRead(read);
        }
        esJobDetailRepository.save(indexes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateReadState(String id, Boolean read) {
        updateReadState(Arrays.asList(id), read);
    }

    /**
     * Use elasticsearch to find jobDetail matches to the searchModel.
     *
     * @param searchModel search model.
     *
     * @return founded JobDetails.
     */
    private Page<EsJobDetail> findUsingPersonalSearchEngine(SearchModel searchModel) {
        List<EsUserSkills> esUserSkills = esUserSkillsRepository.findByUserId(searchModel.getUser().getId());
        SearchQuery searchQuery = SearchQueryUtility
                .buildRatingQuery(esUserSkills, searchModel.getOffset(),
                        searchModel.getLimit());
        Page<EsJobDetail> result = elasticsearchTemplate.queryForPage(searchQuery, EsJobDetail.class);
        return result;
    }

    /**
     * Use elasticsearch to find jobDetail matches to user defined search data.
     *
     * @param searchModel search model.
     *
     * @return founded jobDetail.
     */
    private Page<EsJobDetail> findUsingSimpleSearchEngine(SearchModel searchModel) {
        PageRequest request = new PageRequest(searchModel.getOffset(), searchModel.getLimit());
        //PageRequest request = new PageRequest(searchModel.getOffset(), searchModel.getLimit(), new Sort(new Sort.Order(Sort.Direction.DESC,Constants.DB_FIELD_RECEIVED_DATE)));
        SearchQuery searchQuery = SearchQueryUtility.buildFullTextQuery(searchModel.getSearchData(), request);
        return elasticsearchTemplate.queryForPage(searchQuery, EsJobDetail.class);
    }

    /**
     * Use elasticsearch to find jobDetails with paging.
     *
     * @param searchModel SearchModel Default offset = 0 , limit = 50.
     *
     * @return Page of EsJobDetails
     */
    private Page<EsJobDetail> findEsPage(SearchModel searchModel) {
        PageRequest request =
                new PageRequest(searchModel.getOffset(), searchModel.getLimit(), new Sort(Sort.Direction.DESC,
                        Constants.DB_FIELD_RECEIVED_DATE));
        return esJobDetailRepository.findAll(request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobDetail findOne(String jobDetailId) throws BaseException {
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
        JobDetail foundedJobDetail = findOne(jobDetailId);
        String plainText = htmlToPlaintText(foundedJobDetail.getContent(), foundedJobDetail.getLink());
        //overwrite html content with plain text
        foundedJobDetail.setContent(plainText);
        return foundedJobDetail;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobDetail deleteById(String jobDetailId) throws BaseException {
        JobDetail jobDetailToDelete = findOne(jobDetailId);
        jobDetailRepository.delete(jobDetailToDelete);
        return jobDetailToDelete;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createIndexes() {
        List<JobDetail> jobDetails = jobDetailRepository.findAll();
        jobDetails.forEach(this::createIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EsJobDetail createIndex(JobDetail jobDetail) {
        if (esJobDetailRepository.exists(jobDetail.getId())) {
            esJobDetailRepository.delete(jobDetail.getId());
        }

        EsJobDetail esJobDetail = mapTo(jobDetail);
        return esJobDetailRepository.index(esJobDetail);
    }

    private EsJobDetail mapTo(JobDetail jobDetail) {
        EsJobDetail esJobDetail = new EsJobDetail();
        esJobDetail.setId(jobDetail.getId());
        esJobDetail.setName(jobDetail.getName());
        esJobDetail.setJobId(jobDetail.getJobId());
        esJobDetail.setAddresses(jobDetail.getAddresses());
        esJobDetail.setApplicationMail(jobDetail.getApplicationMail());
        esJobDetail.setContactPersons(jobDetail.getContactPersons());
        esJobDetail.setHomepages(jobDetail.getHomepages());
        esJobDetail.setLink(jobDetail.getLink());
        esJobDetail.setReceivedDate(jobDetail.getReceivedDate());
        esJobDetail.setContentHash(jobDetail.getContentHash());
        esJobDetail.setRead(jobDetail.isRead());
        esJobDetail.setFavorite(jobDetail.isFavorite());

        String plainText = htmlToPlaintText(jobDetail.getContent(), jobDetail.getLink());
        esJobDetail.setContent(plainText);

        return esJobDetail;
    }

    /**
     * Use HtmlParser to get plainText from a html content.
     * <p>
     * Note, that job content from stepstone is included in a frame.
     * That means firstly find first frame and then parse to plain text.
     *
     * @param htmlContent html content.
     * @param link        link to the job.
     *
     * @return job content as plain text.
     */
    private String htmlToPlaintText(String htmlContent, String link) {
        if (link.contains(STEPSTONE)) {
            //Job Html from stepstone is included in a frame.
            return new HtmlParser(htmlContent).findFirstFrame().toPlainText().getContent();
        }

        return new HtmlParser(htmlContent).toPlainText().getContent();
    }
}
