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

package com.intellijob.mail.components;

import com.intellijob.mail.exception.BaseMailException;
import com.intellijob.mail.models.Mail;

import java.util.List;
import java.util.Set;

/**
 * Represents controller to read messages from mail box.
 *
 * @author Sergej Meister
 */
public interface MailReceiver {

    /**
     * Returns messages size.
     *
     * @param folderName folder name.
     *
     * @return mails size.
     * @throws BaseMailException authentication exception, or other mail config exception.
     */
    int getMessageCount(String folderName) throws BaseMailException;

    /**
     * Returns mails size in all message folders.
     *
     * @return mails size.
     * @throws BaseMailException authentication exception, or other mail config exception.
     */
    int getMessageCount() throws BaseMailException;

    /**
     * Returns messages from mail box.
     *
     * @param folderName folder name.
     *
     * @return list of mails.
     */
    Set<Mail> getMessages(String folderName) throws BaseMailException;

    /**
     * Returns all messages from mail box.
     *
     * @return list of mails.
     */
    Set<Mail> getAllMessages() throws BaseMailException;


    /**
     * Search mails in all folders for messages matching the from criteria.
     *
     * @param from mail address in from.
     *
     * @return founded mails.
     * @throws BaseMailException exceptions <code>AuthenticationFailedException</code> or <code>MessagingException</code>
     */
    Set<Mail> searchByFromTerm(String from) throws BaseMailException;

    /**
     * Search mails in all folders for messages matching the from criteria.
     *
     * @param froms list of mail addresses in from.
     * @param or    If true, than <code>OrTerm</code> otherwise <code>AndTerm</code>
     *
     * @return founded mails.
     * @throws BaseMailException exceptions <code>AuthenticationFailedException</code> or <code>MessagingException</code>
     */
    Set<Mail> searchByFromTerm(List<String> froms, Boolean or) throws BaseMailException;


}
