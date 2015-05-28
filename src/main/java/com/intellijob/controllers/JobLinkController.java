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

import com.intellijob.domain.JobLink;
import com.intellijob.domain.Mail;
import com.intellijob.models.HtmlLink;

import java.util.List;

/**
 * This is a controller interface for job link operations.
 */
public interface JobLinkController {

    /**
     * This method search job links in all mails and save founded job links in db.
     * <p>
     * 1. Get all mails.
     * 2. Find all HtmlLinks in mail content.
     * 3. Convert this links to jobLinks.
     * 4. Save this jobLinks.
     *
     * @return stored job links.
     */
    List<JobLink> findInMailsAndSave();

    /**
     * This method search job links in given mails and save founded job links in db.
     * <p>
     * 1. Get all mails.
     * 2. Find all HtmlLinks in mail content.
     * 3. Convert this links to jobLinks.
     * 4. Save this jobLinks.
     *
     * @param mails mails with job links.
     *
     * @return stored job links.
     */
    List<JobLink> findInMailsAndSave(List<Mail> mails);

    /**
     * Convert html links to job links.
     * <p>
     * NOTE: Id is null!
     *
     * @param mail      mail, that include this htm links.
     * @param htmlLinks html links to convert.
     *
     * @return list of job links.
     */
    List<JobLink> convertHtmlLinkToJobLink(Mail mail, List<HtmlLink> htmlLinks);

    /**
     * Convert html link to job link.
     * <p>
     * NOTE: Id is null!
     *
     * @param mail     mail, that include this htm links.
     * @param htmlLink html link to convert.
     *
     * @return converted job link - id is null.
     */
    JobLink convertHtmlLinkToJobLink(Mail mail, HtmlLink htmlLink);

    /**
     * Returns all jobLinks order by received date..
     *
     * @return list of jobLinks
     */
    List<JobLink> findAll();
}
