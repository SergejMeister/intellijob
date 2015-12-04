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
import com.civis.utils.opennlp.models.ModelFacade;
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
import com.intellijob.elasticsearch.repository.EsJobDetailRepository;
import com.intellijob.exceptions.BaseException;
import com.intellijob.exceptions.DocumentNotFoundException;
import com.intellijob.repository.JobDetailRepository;
import com.intellijob.repository.JobRepository;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * This is a implementation of interface <code>JobDetailController</code>.
 */
@Controller
public class JobDetailControllerImpl implements JobDetailController {

    public static final String STEPSTONE = "stepstone";

    private static final String OR_SEPARATOR = ",";

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
        List<ContactPersonSpan> contactPersonSpans = ModelFacade.getContactPersonFinder().find(plainText);

        //find addresses in text
        List<AddressSpan> addressSpans = ModelFacade.getAddressFinder().find(plainText);

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

//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public Page<JobDetail> findPage(int pageIndex, int limit) {
//        PageRequest request = new PageRequest(pageIndex, limit, new Sort(Sort.Direction.DESC, "receivedDate"));
//        return jobDetailRepository.findAll(request);
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public Page<EsJobDetail> findAndSort(User user, int pageIndex, int limit) {
//        return findAndSort(user, user.getProfile().getSearchEngine(), pageIndex, limit);
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public Page<EsJobDetail> findAndSort(User user, String searchFilter, int pageIndex, int limit) {
//        if (searchFilter == null) {
//            return findAndSort(user, user.getProfile().getSearchEngine(), pageIndex, limit);
//        }
//        try {
//            SearchEngineEnum searchEngineEnum = SearchEngineEnum.valueOf(searchFilter.toUpperCase());
//            return findAndSort(user, searchEngineEnum, pageIndex, limit);
//        } catch (IllegalArgumentException iae) {
//            return findAndSort(user, user.getProfile().getSearchEngine(), pageIndex, limit);
//        }
//    }

//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public Page<EsJobDetail> findAndSort(User user, SearchEngineEnum searchEngine, int pageIndex, int limit) {
//        switch (searchEngine) {
//            case SIMPLE:
//                return findUsingSimpleSearchEngine(user, pageIndex, limit);
//            case COMPLEX:
//                return findUsingPersonalSearchEngine(user, pageIndex, limit);
//            default:
//                return findEsPage(user, pageIndex, limit);
//        }
//    }

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
     * Use elasticsearch to find jobDetail matches to the searchModel.
     *
     * @param searchModel search model.
     *
     * @return founded JobDetails.
     */
    private Page<EsJobDetail> findUsingPersonalSearchEngine(SearchModel searchModel) {
        //TODO complex search not implemented!
        return findEsPage(searchModel);
    }

    /**
     * Use elasticsearch to find jobDetail matches to user defined search data.
     *
     * @param searchModel search model.
     *
     * @return founded jobDetail.
     */
    private Page<EsJobDetail> findUsingSimpleSearchEngine(SearchModel searchModel) {
        PageRequest request = new PageRequest(searchModel.getOffset(), searchModel.getLimit(),
                new Sort(Sort.Direction.DESC, "receivedDate"));

        //TODO this is just examples, remove before release!
//        QueryBuilder builder_test1 = QueryBuilders.matchQuery("content", user.getSimpleSearchField());
//        SearchQuery searchQuery_test1 =
//                new NativeSearchQueryBuilder().withQuery(builder_test1).withPageable(request).build();
//        FacetedPage<EsJobDetail> result1 = elasticsearchTemplate.queryForPage(searchQuery_test1, EsJobDetail.class);
//
//        QueryBuilder builder_testPhrase1 = QueryBuilders.matchPhraseQuery("content", user.getSimpleSearchField());
//        SearchQuery searchQuery_testPhrase1 =
//                new NativeSearchQueryBuilder().withQuery(builder_testPhrase1).withPageable(request).build();
//        FacetedPage<EsJobDetail> resultPhrase1 = elasticsearchTemplate.queryForPage(searchQuery_test1, EsJobDetail.class);
//
//
//        QueryBuilder builder_testPhrasePrefix1 = QueryBuilders.matchPhrasePrefixQuery("content", user.getSimpleSearchField());
//        SearchQuery searchQuery_testPhrasePrefix1 =
//                new NativeSearchQueryBuilder().withQuery(builder_testPhrasePrefix1).withPageable(request).build();
//        FacetedPage<EsJobDetail> resultPhrasePrefix1 = elasticsearchTemplate.queryForPage(searchQuery_test1, EsJobDetail.class);


        String[] searchDataArray = searchModel.getSearchData().split(OR_SEPARATOR);
        QueryBuilder builder;
        if (searchDataArray.length == 1) {
            builder = QueryBuilders.matchQuery(Constants.DB_FIELD_CONTENT, searchModel.getSearchData().trim())
                    .operator(MatchQueryBuilder.Operator.AND);
        } else {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().minimumNumberShouldMatch(1);
            for (String searchData : searchDataArray) {
                StringTokenizer stringTokenizer = new StringTokenizer(searchData);
                if (stringTokenizer.countTokens() > 1) {
                    QueryBuilder matchQuery = QueryBuilders.matchQuery(Constants.DB_FIELD_CONTENT, searchData.trim())
                            .operator(MatchQueryBuilder.Operator.AND);
                    boolQueryBuilder.should(matchQuery);
                } else {
                    QueryBuilder matchQuery = QueryBuilders.matchQuery(Constants.DB_FIELD_CONTENT, searchData.trim());
                    boolQueryBuilder.should(matchQuery);
                }
            }
            builder = boolQueryBuilder;
        }

        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(builder).withPageable(request).build();
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
            esJobDetail.setHomepages(jobDetail.getHomepages());
            esJobDetail.setLink(jobDetail.getLink());
            esJobDetail.setReceivedDate(jobDetail.getReceivedDate());

            String plainText = htmlToPlaintText(jobDetail.getContent(), jobDetail.getLink());
            esJobDetail.setContent(plainText);

            esJobDetailRepository.index(esJobDetail);
        }
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
