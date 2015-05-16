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

import java.io.Serializable;

/**
 * Enums to handle mail connection errors.
 */
public enum MailError implements Serializable {

    BAD_REQUEST(100400, "Bad Mail Request"),
    BAD_AUTHENTICATION(100200, "Access is denied. Incorrect username/password!"),
    NOT_SUPPORTED_CONNECTION_TYPE(100000, "This connection type is not supported!"),
    NOT_SUPPORTED_MAIL_ACCOUNT(100000, "This mail account is not supported!"),
    BAD_SETTINGS(100010, "Mail settings could not be loaded successful!");

    private int code;
    private String message;

    MailError(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * Returns error message.
     *
     * @return message as string.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets error message.
     *
     * @param message message.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Returns error code.
     *
     * @return error code.
     */
    public Integer getCode() {
        return code;
    }

    /**
     * Sets error code.
     *
     * @param code error code.
     */
    public void setCode(Integer code) {
        this.code = code;
    }
}
