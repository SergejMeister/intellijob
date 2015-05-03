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

import javax.mail.Message;

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
     * @return mails size.
     * @throws BaseMailException authentication exception, or other mail config exception.
     */
    int getMessageCount(String folderName) throws BaseMailException ;

    /**
     * Returns messages from mail box.
     *
     * @param folderName folder name.
     * @return mails mails.
     */
    Message[] getMessages(String folderName) throws BaseMailException ;
}
