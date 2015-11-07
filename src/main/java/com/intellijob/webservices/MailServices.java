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

import com.intellijob.controllers.JobLinkController;
import com.intellijob.controllers.MailController;
import com.intellijob.controllers.UserController;
import com.intellijob.domain.JobLink;
import com.intellijob.domain.Mail;
import com.intellijob.domain.User;
import com.intellijob.dto.response.ResponseError;
import com.intellijob.dto.response.ResponseMailData;
import com.intellijob.dto.response.ResponseMailTableData;
import com.intellijob.exceptions.NotMailSyncException;
import com.intellijob.exceptions.UserNotFoundException;
import com.intellijob.mail.components.MailReceiver;
import com.intellijob.mail.controllers.MailFactory;
import com.intellijob.mail.dto.RequestMailData;
import com.intellijob.mail.dto.ResponseMailSearchData;
import com.intellijob.mail.exception.BaseMailException;
import com.intellijob.mail.exception.NotSupportedMailAccount;
import com.intellijob.mail.exception.PermissionDeniedException;
import com.intellijob.mail.models.MailModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;


/**
 * Mail Web-Services.
 * <p>
 * Handle all request with endpoints <code>/mails**</code>
 */
@RestController
public class MailServices extends BaseServices {

    private final static Logger LOG = LoggerFactory.getLogger(MailServices.class);

    @Autowired
    private MailFactory mailFactory;

    @Autowired
    private MailController mailController;

    @Autowired
    private JobLinkController jobLinkController;


    @Autowired
    private UserController userController;

    /**
     * Request search mails in mail box.
     * <p>
     * When connection type is null, than default connection type imap.
     * When last mail sync date for this profile exist, than search mails than greater or equal last sync date.
     * When last mail sync date for this profile doesn't exist, than search all mails.
     * Save Mails in database.
     * Save current date <code>new Date()</code> as last mail sync date for this profile.
     *
     * @param requestMailData required attributes username, password, mail account.
     *
     * @return data transfer object <code>ResponseMailSearchData.java</code>
     * @throws Exception handle exceptions.
     */
    @RequestMapping(value = Endpoints.MAILS_SEARCH, method = RequestMethod.POST)
    public @ResponseBody ResponseMailSearchData searchMail(@RequestBody RequestMailData requestMailData)
            throws Exception {
        validate(requestMailData);
        MailReceiver mailReceiver = mailFactory.getReceiver(requestMailData);
        List<String> froms = Arrays.asList("info@jobagent.stepstone.de", "jagent@route.monster.com");
        List<MailModel> inboxMails;
        try {
            Date lastMailSyncDate = userController.getLastMailSyncDate();
            //search mails after lastMailSyncDate
            inboxMails = mailReceiver.searchByFromTermAndDate(froms, Boolean.TRUE, lastMailSyncDate);
        } catch (NotMailSyncException | UserNotFoundException multiException) {
            //search all mails
            inboxMails = mailReceiver.searchByFromTerm(froms, Boolean.TRUE);
        }

        List<Mail> savedMails = mailController.saveModel(inboxMails);

        User user = new User();
        try {
            user = userController.getUniqueUser();
        } catch (UserNotFoundException unfe) {
            LOG.warn("User not found, create new user with profile!");
        }
        userController.updateMailSyncDate(user);

        //search for job links and save.
        List<JobLink> jobLinks = jobLinkController.findInMailsAndSave(savedMails);

        return new ResponseMailSearchData(inboxMails.size() + " mails and " + jobLinks.size() + " job links founded.");
    }

    /**
     * Request Get mails.
     *
     * @return data transfer object <code>ResponseMailSearchData.java</code>
     * @throws Exception handle exceptions.
     */
    @RequestMapping(value = Endpoints.MAILS, method = RequestMethod.GET)
    public @ResponseBody ResponseMailTableData getMail() throws Exception {
        List<Mail> mails = mailController.findAll();
        return new ResponseMailTableData(mails);
    }

    /**
     * Request Get all mails with paging.
     *
     * @return data transfer object <code>ResponseJobTableData.java</code>
     */
    @RequestMapping(value = Endpoints.MAILS_PAGING, method = RequestMethod.GET)
    public @ResponseBody ResponseMailTableData getMails(@PathVariable int pageIndex, @PathVariable int limit) {
        Page<Mail> mailPage = mailController.findPage(pageIndex, limit);
        return new ResponseMailTableData(mailPage);
    }

    /**
     * Request Get mails.
     *
     * @return data transfer object <code>ResponseMailSearchData.java</code>
     * @throws Exception handle exceptions.
     */
    @RequestMapping(value = Endpoints.MAIL_BY_ID, method = RequestMethod.GET)
    public @ResponseBody ResponseMailData getMail(@PathVariable String mailId) throws Exception {
        Mail mail = mailController.findMail(mailId);
        return new ResponseMailData(mail);
    }

    /**
     * Request Get mails.
     *
     * @return data transfer object <code>ResponseMailSearchData.java</code>
     * @throws Exception handle exceptions.
     */
    @RequestMapping(value = Endpoints.MAIL_BY_ID, method = RequestMethod.DELETE)
    public @ResponseBody ResponseMailData deleteMail(@PathVariable String mailId) throws Exception {
        mailController.deleteById(mailId);
        return new ResponseMailData(mailId);
    }

    @SuppressWarnings("unused")
    private void logInfoMails(Set<MailModel> mails) {
        LOG.info("Total Messages:- " + mails.size());
        for (MailModel mailModel : mails) {
            LOG.info("------------------------------------------------------------------------------------------");
            LOG.info("From: " + mailModel.getFrom());
            LOG.info("ContentType: " + mailModel.getContentType());
            LOG.info("Content: " + mailModel.getContent());
            LOG.info("------------------------------------------------------------------------------------------");
        }
    }

    private void validate(RequestMailData requestMailData) throws BaseMailException {
        if (!StringUtils.hasLength(requestMailData.getMailAccount())) {
            throw new NotSupportedMailAccount();
        }
    }

    /**
     * Covert permission denied exceptions to http status unauthorized.
     *
     * @param pde permission denied exception.
     *
     * @return data transfer object <code>ResponseError.class</code> with status 401.
     */
    @ExceptionHandler(PermissionDeniedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public @ResponseBody ResponseError handleException(PermissionDeniedException pde) {
        LOG.warn(pde.getMailError().getMessage(), pde);
        return handleException(HttpStatus.UNAUTHORIZED, pde);
    }

    @ExceptionHandler(BaseMailException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody ResponseError handleException(BaseMailException bme) {
        LOG.error(bme.getMailError().getMessage(), bme);
        return handleException(HttpStatus.BAD_REQUEST, bme);
    }
}
