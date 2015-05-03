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
import com.intellijob.mail.exception.PermissionDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


/**
 * Mail Web-Services.
 *
 * Created by Sergej Meister on 4/26/15.
 */
@RestController
public class MailServices extends BaseServices {

    private final static Logger LOG = LoggerFactory.getLogger(MailServices.class);

    @Autowired
    private MailController mailController;

    @RequestMapping(value = "/mail/search", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMailSearchData searchMail(@RequestBody RequestMailData requestMailData) throws Exception {
        MailReceiver mailReceiver = mailController.getReceiver(requestMailData);
        int messageCount =  mailReceiver.getMessageCount("INBOX");
        System.out.println("Total Messages:- " + messageCount);

        return new ResponseMailSearchData(messageCount + " mails founded.");
    }

    @ExceptionHandler(PermissionDeniedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public @ResponseBody ResponseError handleException(PermissionDeniedException pde) {
        LOG.error(pde.getMailError().getMessage(), pde);
        return handleException(HttpStatus.UNAUTHORIZED,pde);
    }

    @ExceptionHandler(BaseMailException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody ResponseError handleException(BaseMailException bme) {
        LOG.error(bme.getMailError().getMessage(), bme);
        return handleException(HttpStatus.BAD_REQUEST,bme);
    }
}
