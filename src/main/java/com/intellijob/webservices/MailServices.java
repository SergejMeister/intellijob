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

import com.intellijob.mail.components.MailReceiver;
import com.intellijob.mail.controllers.MailController;
import com.intellijob.mail.dto.RequestMailData;
import com.intellijob.mail.dto.ResponseError;
import com.intellijob.mail.dto.ResponseMailSearchData;
import com.intellijob.mail.exception.BaseMailException;
import com.intellijob.mail.exception.NotSupportedMailAccount;
import com.intellijob.mail.exception.PermissionDeniedException;
import com.intellijob.mail.models.Mail;
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
import java.util.List;
import java.util.Set;


/**
 * Mail Web-Services.
 */
@RestController
public class MailServices extends BaseServices {

    public static final String ENDPOINT = "/mail";

    public static final String URL_MAIL_SEARCH = ENDPOINT + "/search";

    private final static Logger LOG = LoggerFactory.getLogger(MailServices.class);

    @Autowired
    private MailController mailController;

    /**
     * Request search mails in mail box.
     * <p>
     * When connection type is null, than default connection type imap.
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
        MailReceiver mailReceiver = mailController.getReceiver(requestMailData);
        List<String> froms = Arrays.asList("info@jobagent.stepstone.de", "jagent@route.monster.com");
        //Set<Mail> inboxMails = mailReceiver.searchByFromTermAndDate(froms, Boolean.TRUE, new Date(2015,5,12));
        Set<Mail> inboxMails = mailReceiver.searchByFromTerm(froms, Boolean.TRUE);
        logInfoMails(inboxMails);

        return new ResponseMailSearchData(inboxMails.size() + " mails founded.");
    }

    private void logInfoMails(Set<Mail> mails) {
        LOG.info("Total Messages:- " + mails.size());
        for (Mail mail : mails) {
            LOG.info("------------------------------------------------------------------------------------------");
            LOG.info("From: " + mail.getFrom());
            LOG.info("ContentType: " + mail.getContentType());
            LOG.info("Content: " + mail.getContent());
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
