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

import com.civis.utils.html.models.HtmlLink;
import com.intellijob.domain.JobLink;
import com.intellijob.domain.Mail;
import com.intellijob.exceptions.BaseException;
import org.springframework.data.domain.Page;

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

    /**
     * Returns all job links with paging.
     *
     * @param pageIndex page index.
     * @param limit     size per page
     *
     * @return page of job links.
     */
    Page<JobLink> findAll(int pageIndex, int limit);

    /**
     * Returns jobLink by specified id.
     *
     * @param jobLinkId id of jobLink.
     *
     * @return jobLink.
     * @throws BaseException if no document founded.
     */
    JobLink findById(String jobLinkId) throws BaseException;

    /**
     * Returns jobLink by specified id.
     *
     * @param jobLink jobLink to save.
     *
     * @return the same jobLink.
     */
    JobLink save(JobLink jobLink);

    /**
     * Returns jobLinks with downloaded = false.
     *
     * @return list of job links.
     */
    List<JobLink> findToDownload();

    /**
     * Remove this jobLink.
     *
     * @param jobLink remove this jobLink.
     */
    JobLink removeJobLink(JobLink jobLink);

    /**
     * Delete a jobLink by specified id.
     *
     * @param jobLinkId id of JobLink to delete.
     *
     * @return deleted JobLink
     * @throws BaseException exception, if no jobLink found for specified id.
     */
    JobLink deleteById(String jobLinkId) throws BaseException;
}
