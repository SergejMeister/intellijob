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

package com.intellijob.mail.enums;

import com.intellijob.mail.exception.NotSupportedConnectionType;
import org.springframework.util.StringUtils;

/**
 * Supported mail protocols: POP3, SMTP, IMAP.
 *
 * @author Sergej Meister
 */
public enum MailConnectionType {

    /**
     * POP3
     */
    POP3,

    /**
     * IMAP
     */
    IMAP,

    /**
     * SMTP
     */
    SMTP;


    public static MailConnectionType getMailConnectionType(String mailConnectionTypeAsString)
            throws NotSupportedConnectionType {
        if (StringUtils.hasLength(mailConnectionTypeAsString)) {
            try {
                MailConnectionType.valueOf(mailConnectionTypeAsString.toUpperCase());
            } catch (IllegalArgumentException iae) {
                throw new NotSupportedConnectionType();
            }
        }

        //default connection type imap
        return MailConnectionType.IMAP;
    }
}
