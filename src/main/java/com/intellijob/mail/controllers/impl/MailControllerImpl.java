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

package com.intellijob.mail.controllers.impl;

import com.intellijob.mail.components.MailReceiver;
import com.intellijob.mail.components.MailSender;
import com.intellijob.mail.components.beans.MailReceiverBean;
import com.intellijob.mail.controllers.MailController;
import com.intellijob.mail.dto.ReceiverConnectionData;
import com.intellijob.mail.dto.RequestMailData;
import com.intellijob.mail.enums.MailAccount;
import com.intellijob.mail.enums.MailConnectionType;
import com.intellijob.mail.enums.MailError;
import com.intellijob.mail.exception.BaseMailException;
import com.intellijob.mail.exception.NotSupportedConnectionType;
import com.intellijob.mail.exception.NotSupportedMailAccount;
import com.intellijob.mail.exception.SettingsLoadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Controller
public class MailControllerImpl implements MailController {

    private final static Logger LOG = LoggerFactory.getLogger(MailControllerImpl.class);

    private final static String MAIL_IMAPS_HOST_KEY="mail.imaps.host";
    private final static String MAIL_POP3_USER_KEY="mail.pop3.user";
    private final static String IMAPS_KEY="imaps";

    @Autowired
    private MailReceiverBean mailReceiverBean;

    /**
     * {@inheritDoc}
     */
    @Override
    public MailReceiver getReceiver(RequestMailData requestMailData) throws BaseMailException {

        MailConnectionType mailConnectionType = MailConnectionType.getMailConnectionType(requestMailData.getConnectionType());
        requestMailData.setConnectionType(mailConnectionType.toString().toLowerCase());
        switch(mailConnectionType) {
            case POP3: return createPopReceiver(requestMailData);
            case IMAP: return createImapReceiver(requestMailData);
            default: throw new NotSupportedConnectionType();
        }
    }

    private MailReceiver createPopReceiver(RequestMailData requestMailData) throws BaseMailException {
        //
        // Setup properties for the mail session.
        //
        Properties popProperties = getMailProperties(requestMailData.getMailAccount(),requestMailData.getConnectionType());
        popProperties.put(MAIL_POP3_USER_KEY, requestMailData.getUsername());
        Session session = Session.getDefaultInstance(popProperties, null);

        //TODO implementation is not finished!
        throw new NotImplementedException();
    }


    private Properties getMailProperties(String mailAccountAsString,String connectionTypeAsString) throws BaseMailException {
        String filePropertyName ;
        try {
            MailAccount mailAccount = MailAccount.valueOf(mailAccountAsString.toUpperCase());
            filePropertyName = mailAccount.getPropertyName(connectionTypeAsString);
        } catch(IllegalArgumentException iae) {
            LOG.warn("Mail account (" + mailAccountAsString + ") is not supported!", iae);
            throw new NotSupportedMailAccount();
        }

        Properties properties = new Properties();
        try {
            //LOG.info("Path: " + Thread.currentThread().getContextClassLoader().getResource(filePropertyName).getPath());
            InputStream inputStream = getClass().getResourceAsStream(filePropertyName);
            properties.load(inputStream);
        } catch (IOException e) {
            LOG.error("Property (" + getClass().getResource(filePropertyName).getPath() + ") could not be loaded successful!", e);
            throw new SettingsLoadException();
        }

        return properties;
    }

    private MailReceiver createImapReceiver(RequestMailData requestMailData) throws BaseMailException {
        //
        // Setup properties for the mail session.
        //
        Properties imapProperties = getMailProperties(requestMailData.getMailAccount(),requestMailData.getConnectionType());

        Session session = Session.getDefaultInstance(imapProperties, null);
        try {
            Store store = session.getStore(IMAPS_KEY);
            ReceiverConnectionData receiverConnectionData = new ReceiverConnectionData();
            receiverConnectionData.setSession(session);
            receiverConnectionData.setStore(store);
            receiverConnectionData.setMailHost(imapProperties.getProperty(MAIL_IMAPS_HOST_KEY));
            receiverConnectionData.setUsername(requestMailData.getUsername());
            receiverConnectionData.setPassword(requestMailData.getPassword());

            mailReceiverBean.init(receiverConnectionData);
            return mailReceiverBean ;
        } catch (MessagingException me) {
            throw new BaseMailException(MailError.BAD_SETTINGS, me);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MailSender getSender(RequestMailData connectionData) {
        throw new NotImplementedException();
    }
}
