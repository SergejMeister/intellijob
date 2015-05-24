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

import com.intellijob.controllers.JobLinkController;
import com.intellijob.controllers.MailController;
import com.intellijob.domain.JobLink;
import com.intellijob.domain.Mail;
import com.intellijob.models.HtmlLink;
import com.intellijob.repository.JobLinkRepository;
import com.intellijob.utility.HtmlLinkParseFilter;
import com.intellijob.utility.HtmlLinkParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is a implementation of interface <code>JobLinkController</code>.
 */
@Controller
public class JobLinkControllerImpl implements JobLinkController {

    //TODO: It's not really good and should be improved.
    //ja.cfm in url means in stepstone mail job agent!
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
        //create html parse filter.
        HtmlLinkParseFilter htmlLinkParseFilter = new HtmlLinkParseFilter();
        htmlLinkParseFilter.setNullableText(Boolean.FALSE);
        htmlLinkParseFilter.setLinkMatchers(JOB_LINK_MATCHERS);

        //Find jobLinks in mails content.
        List<JobLink> jobLinksToSave = new ArrayList<>();
        for (Mail mail : mails) {
            List<HtmlLink> htmlLinks = HtmlLinkParser.parse(mail.getContent(), htmlLinkParseFilter);
            List<JobLink> loopResultOfJobLinks = convertHtmlLinkToJobLink(mail, htmlLinks);
            jobLinksToSave.addAll(loopResultOfJobLinks);
        }

        //save all job links in db.
        return jobLinkRepository.save(jobLinksToSave);
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
        return jobLink;
    }

}
