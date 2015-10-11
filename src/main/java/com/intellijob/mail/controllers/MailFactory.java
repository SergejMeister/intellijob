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

package com.intellijob.mail.controllers;

import com.intellijob.mail.components.MailReceiver;
import com.intellijob.mail.components.MailSender;
import com.intellijob.mail.dto.RequestMailData;
import com.intellijob.mail.exception.BaseMailException;

/**
 * Interface mail facade.
 */
public interface MailFactory {

    /**
     * Returns mail receiver.
     *
     * @param requestMailData connection data.
     *
     * @return receiver.
     */
    MailReceiver getReceiver(RequestMailData requestMailData) throws BaseMailException;

    /**
     * Returns mail sender.
     *
     * @param requestMailData connection data.
     *
     * @return sender.
     */
    MailSender getSender(RequestMailData requestMailData) throws BaseMailException;
}
