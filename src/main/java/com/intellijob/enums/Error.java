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

package com.intellijob.enums;

import java.io.Serializable;

/**
 * Enums to handle errors.
 */
public enum Error implements Serializable {

    /**
     * Bad request error code and message.
     */
    BAD_REQUEST(5000000, "Bad request!"),

    /**
     * result not unique error code and message.
     */
    NOT_UNIQUE_RESULT(5000001, "Result not unique!"),

    /**
     * Last date of mails synchronization is not founded.
     */
    NOT_LAST_MAIL_SYNC_DATE(5000002, "Last date of mails synchronization is not founded!"),

    /**
     * Error, if required param null or empty.
     */
    NOT_VALID_PARAM(5000003, "Param is null or empty!"),

    /**
     * Error code, if a document or domain object is not founded.
     */
    NOT_FOUNDED_DOCUMENT(5000004, "Document is not founded!"),

    /**
     * Error code, if a job link is not founded.
     */
    NOT_FOUNDED_LINK(5000005, "Job Link is not founded!");


    private String message;
    private Integer code;

    Error(Integer code, String message) {
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
