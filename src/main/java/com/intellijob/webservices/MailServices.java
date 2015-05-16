/*
 * Copyright 2015 Sergej Meister
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intellijob.webservices;

import com.intellijob.controllers.MailController;
import com.intellijob.controllers.ProfileController;
import com.intellijob.dto.ResponseError;
import com.intellijob.dto.ResponseMailListData;
import com.intellijob.exceptions.NotMailSyncException;
import com.intellijob.mail.components.MailReceiver;
import com.intellijob.mail.controllers.MailFacade;
import com.intellijob.mail.dto.RequestMailData;
import com.intellijob.mail.dto.ResponseMailSearchData;
import com.intellijob.mail.exception.BaseMailException;
import com.intellijob.mail.exception.NotSupportedMailAccount;
import com.intellijob.mail.exception.PermissionDeniedException;
import com.intellijob.mail.models.MailModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
 */
@RestController
public class MailServices extends BaseServices {

    public static final String ENDPOINT = "/mails";

    public static final String URL_MAIL_SEARCH = ENDPOINT + "/search";

    private final static Logger LOG = LoggerFactory.getLogger(MailServices.class);

    @Autowired
    private MailFacade mailFacade;

    @Autowired
    private MailController mailController;


    @Autowired
    private ProfileController profileController;

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
    @RequestMapping(value = URL_MAIL_SEARCH, method = RequestMethod.POST)
    public @ResponseBody ResponseMailSearchData searchMail(@RequestBody RequestMailData requestMailData)
            throws Exception {
        validate(requestMailData);
        MailReceiver mailReceiver = mailFacade.getReceiver(requestMailData);
        List<String> froms = Arrays.asList("info@jobagent.stepstone.de", "jagent@route.monster.com");
        List<MailModel> inboxMails;
        Date newLastMailSyncDate = new Date();
        try {
            Date lastMailSyncDate = profileController.getLastMailSyncDate();
            inboxMails = mailReceiver.searchByFromTermAndDate(froms, Boolean.TRUE, lastMailSyncDate);
        } catch (NotMailSyncException nmse) {
            inboxMails = mailReceiver.searchByFromTerm(froms, Boolean.TRUE);
        }

        mailController.saveModel(inboxMails);
        profileController.simpleSave(newLastMailSyncDate);
        return new ResponseMailSearchData(inboxMails.size() + " mails founded.");
    }

    /**
     * Request Get mails.
     *
     * @return data transfer object <code>ResponseMailSearchData.java</code>
     * @throws Exception handle exceptions.
     */
    @RequestMapping(value = ENDPOINT, method = RequestMethod.GET)
    public @ResponseBody ResponseMailListData getMail() throws Exception {
        return new ResponseMailListData();
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
