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

/**
 * Supported mail accounts.
 *
 * @version 1.0
 */
public enum MailAccount {

    GMAIL("gmail"),
    RAMBLER("rambler");


    /**
     * Represents the config property file.
     * <p>
     * Example: gmail_imap.properties; rambler_imap.properties.
     */
    private final String propertyName;


    private MailAccount(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyName(String connectionType) {
        return propertyName + "_" + connectionType + ".properties";
    }
}
