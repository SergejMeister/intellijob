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

package com.intellijob.mail.components.beans;

import com.intellijob.mail.components.MailReceiver;
import com.intellijob.mail.dto.ReceiverConnectionData;
import com.intellijob.mail.enums.MailError;
import com.intellijob.mail.exception.BaseMailException;
import com.intellijob.mail.exception.PermissionDeniedException;
import com.intellijob.mail.models.MailModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.mail.AuthenticationFailedException;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.FromStringTerm;
import javax.mail.search.OrTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents mail receiver to get message from mail box.
 * <p>
 * Connect to mail box, execute request, close connection.
 * To connect to the mail box, you have to init this component with connection data.
 */
@Component
public class MailReceiverBean implements MailReceiver {

    private final static Logger LOG = LoggerFactory.getLogger(MailReceiverBean.class);

    private Session session;
    private Store store;
    private String mailHost;
    private String username;
    private String password;

    /**
     * Init mail connection data.
     *
     * @param receiverConnectionData connection data.
     */
    public void init(ReceiverConnectionData receiverConnectionData) {
        this.session = receiverConnectionData.getSession();
        this.store = receiverConnectionData.getStore();
        this.mailHost = receiverConnectionData.getMailHost();
        this.username = receiverConnectionData.getUsername();
        this.password = receiverConnectionData.getPassword();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMessageCount(String folderName) throws BaseMailException {
        try {
            store.connect(mailHost, username, password);
            Folder folder = store.getFolder(folderName);
            folder.open(Folder.READ_ONLY);
            int result = folder.getMessageCount();

            // Close folder and close store.
            folder.close(false);
            store.close();
            return result;
        } catch (AuthenticationFailedException afe) {
            throw new PermissionDeniedException(afe);
        } catch (MessagingException e) {
            LOG.error(e.getLocalizedMessage(), e);
            throw new BaseMailException(MailError.BAD_REQUEST);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMessageCount() throws BaseMailException {
        try {
            store.connect(mailHost, username, password);
            Folder[] folders = store.getDefaultFolder().list();
            int totalCount = 0;
            for (Folder folder : folders) {
                if (isMessageFolder(folder.getType())) {
                    folder.open(Folder.READ_ONLY);
                    int result = folder.getMessageCount();
                    System.out.println(folder.getFullName() + ": " + result);
                    totalCount = totalCount + result;

                    // Close folder
                    folder.close(false);
                }
            }
            store.close();
            return totalCount;
        } catch (AuthenticationFailedException afe) {
            throw new PermissionDeniedException(afe);
        } catch (MessagingException e) {
            LOG.error(e.getLocalizedMessage(), e);
            throw new BaseMailException(MailError.BAD_REQUEST);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MailModel> getMessages(String folderName) throws BaseMailException {
        List<MailModel> result = new ArrayList<>();
        try {
            store.connect(mailHost, username, password);
            Folder folder = store.getFolder(folderName);
            folder.open(Folder.READ_ONLY);
            for (Message message : folder.getMessages()) {
                result.add(new MailModel(message));
            }

            // Close folder and close store.
            folder.close(false);
            store.close();
            return result;
        } catch (AuthenticationFailedException afe) {
            throw new PermissionDeniedException(afe);
        } catch (MessagingException | IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            throw new BaseMailException(MailError.BAD_REQUEST);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MailModel> getAllMessages() throws BaseMailException {
        List<MailModel> result = new ArrayList<>();
        try {
            store.connect(mailHost, username, password);
            Folder[] folders = store.getDefaultFolder().list();
            for (Folder folder : folders) {
                if (isMessageFolder(folder.getType())) {
                    folder.open(Folder.READ_ONLY);
                    for (Message message : folder.getMessages()) {
                        result.add(new MailModel(message));
                    }

                    // Close folder
                    folder.close(false);
                }
            }
            store.close();
            return result;
        } catch (AuthenticationFailedException afe) {
            throw new PermissionDeniedException(afe);
        } catch (MessagingException | IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            throw new BaseMailException(MailError.BAD_REQUEST);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MailModel> searchByFromTerm(String from) throws BaseMailException {
        List<String> froms = new ArrayList<>();
        froms.add(from);
        return searchByFromTerm(froms, Boolean.FALSE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MailModel> searchByFromTerm(List<String> froms, Boolean or) throws BaseMailException {
        SearchTerm searchTerm = createSearchTermForFroms(froms, or);
        return search(searchTerm);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MailModel> searchByFromTermAndDate(String from, Date date) throws BaseMailException {
        List<String> froms = new ArrayList<>();
        froms.add(from);
        return searchByFromTermAndDate(froms, Boolean.FALSE, date);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MailModel> searchByFromTermAndDate(List<String> froms, Boolean or, Date date) throws BaseMailException {
        SearchTerm searchTerm = createSearchTermForFromsAndDate(froms, or, date);
        return search(searchTerm);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MailModel> search(SearchTerm searchTerm) throws BaseMailException {
        List<MailModel> result = new ArrayList<>();
        try {
            store.connect(mailHost, username, password);
            Folder[] folders = store.getDefaultFolder().list();
            for (Folder folder : folders) {
                if (isMessageFolder(folder.getType())) {
                    folder.open(Folder.READ_ONLY);
                    for (Message message : folder.search(searchTerm)) {
                        result.add(new MailModel(message));
                    }

                    // Close folder
                    folder.close(false);
                }
            }
            store.close();
            return result;
        } catch (AuthenticationFailedException afe) {
            throw new PermissionDeniedException(afe);
        } catch (MessagingException | IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            throw new BaseMailException(MailError.BAD_REQUEST);
        }
    }

    private SearchTerm createSearchTermForFromsAndDate(List<String> froms, Boolean or, Date date) {
        SearchTerm fromTerm = createSearchTermForFroms(froms, or);
        SearchTerm dateTerm = new ReceivedDateTerm(ComparisonTerm.GT, date);
        return new AndTerm(fromTerm, dateTerm);
    }

    private SearchTerm createSearchTermForFroms(List<String> froms, Boolean or) {
        if (froms.size() == 1) {
            return new FromStringTerm(froms.get(0));
        }

        SearchTerm[] allFroms = new FromStringTerm[froms.size()];
        for (int i = 0; i < froms.size(); i++) {
            allFroms[i] = new FromStringTerm(froms.get(i));
        }

        if (or) {
            return new OrTerm(allFroms);
        }
        return new AndTerm(allFroms);
    }


    /**
     * Can contain messages.
     * <p>
     * Type 1 - can contain messages.
     * Type 2 - can contain folders.
     * Type 3 - can contain both.
     *
     * @param folderType folder type (1,2,3)
     *
     * @return true, if 1 or 3.
     */
    private Boolean isMessageFolder(int folderType) {
        //if ((folderType & Folder.HOLDS_FOLDERS) == 2) {
        if (folderType == Folder.HOLDS_FOLDERS) {
            //can only contain other folders!
            return Boolean.FALSE;
        }
        //1 or 3: can contain messages.
        return Boolean.TRUE;
    }
}
