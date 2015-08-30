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

import com.civis.utils.html.models.HtmlLink;
import com.civis.utils.html.parser.HtmlParseFilter;
import com.civis.utils.html.parser.HtmlParser;
import com.intellijob.controllers.JobLinkController;
import com.intellijob.controllers.MailController;
import com.intellijob.domain.JobLink;
import com.intellijob.domain.Mail;
import com.intellijob.exceptions.BaseException;
import com.intellijob.exceptions.DocumentNotFoundException;
import com.intellijob.repository.JobLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This is a implementation of interface <code>JobLinkController</code>.
 */
@Controller
public class JobLinkControllerImpl implements JobLinkController {

    //TODO: It's not really good and should be improved.
    //ja.cfm in url means a job mail from stepstone search agent!
    public final static List<String> JOB_LINK_MATCHERS =
            Arrays.asList("stellenanzeige.monster.de", "www.stepstone.de/ja.cfm");

    @Autowired
    private MailController mailController;

    @Autowired
    private JobLinkRepository jobLinkRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<JobLink> findInMailsAndSave() {
        List<Mail> mails = mailController.findAll();
        return findInMailsAndSave(mails);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<JobLink> findInMailsAndSave(List<Mail> mails) {
        //extract JobLinks from mail
        List<JobLink> newJobLinks = extractJobLinks(mails);

        //check in db for new href
        List<JobLink> jobLinksToSave = filterUniqueJobLinks(newJobLinks);

        //save all job links in db.
        return jobLinkRepository.save(jobLinksToSave);
    }

    private List<JobLink> filterUniqueJobLinks(List<JobLink> jobLinksToFilter) {

        //filter for current affected jobLinks
        Set<String> hrefs = new HashSet<>();
        List<JobLink> listOfUniqueJobLinks = new ArrayList<>();
        for(JobLink jobLink : jobLinksToFilter) {
            if(!hrefs.contains(jobLink.getHref())){
                listOfUniqueJobLinks.add(jobLink);
                hrefs.add(jobLink.getHref());
            }
        }

        //filter for persisted JobLinks.
        List<JobLink> storedJobLinks = jobLinkRepository.findByHrefIn(hrefs);
        if(storedJobLinks.isEmpty()){
            return listOfUniqueJobLinks ;
        }

        List<JobLink> result = new ArrayList<>();
        for(JobLink jobLink : listOfUniqueJobLinks) {
            if (hrefNotExistIn(jobLink.getHref(), storedJobLinks)) {
                result.add(jobLink);
            }
        }

        return result;
    }

    /**
     * Find all unique links in mail content and convert in to <code>JobLink</code>.
     */
    private List<JobLink> extractJobLinks(List<Mail> mails) {
        List<JobLink> result = new ArrayList<>();

        //create html parseLink filter.
        HtmlParseFilter htmlParseFilter = new HtmlParseFilter();
        htmlParseFilter.setNullableText(Boolean.FALSE);
        htmlParseFilter.setLinkMatcherList(JOB_LINK_MATCHERS);
        for (Mail mail : mails) {
            List<HtmlLink> htmlLinks = new HtmlParser(mail.getContent(), htmlParseFilter).parse().getLinks();
            List<JobLink> mailJobLinks = convertHtmlLinkToJobLink(mail, htmlLinks);
            result.addAll(mailJobLinks);
        }
        return result;
    }

    private boolean hrefNotExistIn(String href, List<JobLink> storedJobLinks) {
        for(JobLink jobLink : storedJobLinks){
            if(jobLink.getHref().equals(href)){
                return Boolean.FALSE;
            }
        }

        return Boolean.TRUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<JobLink> convertHtmlLinkToJobLink(Mail mail, List<HtmlLink> htmlLinks) {
        List<JobLink> result = new ArrayList<>();
        for (HtmlLink htmlLink : htmlLinks) {
            JobLink jobLink = convertHtmlLinkToJobLink(mail, htmlLink);
            result.add(jobLink);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobLink convertHtmlLinkToJobLink(Mail mail, HtmlLink htmlLink) {
        JobLink jobLink = new JobLink();
        jobLink.setMail(mail);
        jobLink.setAtag(htmlLink.getAtag());
        jobLink.setHref(htmlLink.getHref());
        jobLink.setValue(htmlLink.getValue());
        jobLink.setReceivedDate(mail.getReceivedDate());
        jobLink.setDownloaded(Boolean.FALSE);
        return jobLink;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<JobLink> findAll() {
        return jobLinkRepository.findAll(new Sort(Sort.Direction.DESC, "receivedDate"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<JobLink> findAll(int pageIndex, int limit) {
        PageRequest request = new PageRequest(pageIndex, limit, new Sort(Sort.Direction.DESC, "receivedDate"));
        return jobLinkRepository.findAll(request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobLink findById(String jobLinkId) throws BaseException {
        JobLink jobLink = jobLinkRepository.findOne(jobLinkId);
        if (jobLink != null) {
            return jobLink;
        }

        throw new DocumentNotFoundException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobLink save(JobLink jobLink) {
        jobLinkRepository.save(jobLink);
        return jobLink;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<JobLink> findToDownload() {
        return jobLinkRepository.findByDownloaded(Boolean.FALSE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobLink removeJobLink(JobLink jobLink) {
        jobLinkRepository.delete(jobLink);

        //TODO check mail
        //Has this mail other job links, than ok, else delete mail too.
        return jobLink;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobLink deleteById(String jobLinkId) throws BaseException {
        JobLink foundedJobLink = findById(jobLinkId);
        jobLinkRepository.delete(foundedJobLink);
        return foundedJobLink;
    }


}
