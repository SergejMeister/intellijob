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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.mail.*;

@Component
public class MailReceiverBean implements MailReceiver {

    private final static Logger LOG = LoggerFactory.getLogger(MailReceiverBean.class);

    private Session session ;
    private Store store;
    private String mailHost;
    private String username;
    private String password;


    public MailReceiverBean()
    {
    }

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

            //
            // Close folder and close store.
            //
            folder.close(false);
            store.close();
            return result;
        } catch(AuthenticationFailedException afe){
            throw new PermissionDeniedException(afe);
        } catch (MessagingException e) {
            LOG.error(e.getLocalizedMessage(),e);
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
                    System.out.println(folder.getFullName() + ": " + folder.getMessageCount());
                    folder.open(Folder.READ_ONLY);
                    int result = folder.getMessageCount();
                    totalCount = totalCount + result;
                    //
                    // Close folder
                    //
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
     * Can contain messages.
     * <p>
     * Type 1 - can contain messages.
     * Type 2 - can contain folders.
     * Type 3 - can contain both.
     *
     * @param folderType folder type (1,2,3)
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Message[] getMessages(String folderName)
    {
        throw new NotImplementedException();
    }
}
