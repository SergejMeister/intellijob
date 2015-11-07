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

package com.intellijob.webservices;

import com.intellijob.dto.response.ResponseError;
import com.intellijob.exceptions.BaseException;
import com.intellijob.exceptions.DocumentNotFoundException;
import com.intellijob.mail.exception.BaseMailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Base service class.
 */
public abstract class BaseServices {

    private final static Logger LOG = LoggerFactory.getLogger(MailServices.class);

    protected ResponseError handleException(HttpStatus status, BaseMailException bme) {
        return new ResponseError(status.value(), bme.getMailError().getCode(), bme.getMailError().getMessage());
    }

    protected ResponseError handleException(HttpStatus status, BaseException be) {
        return new ResponseError(status.value(), be.getError().getCode(), be.getError().getMessage());
    }

    /**
     * Convert document not found exceptions to http status not found.
     *
     * @param e document not found exception.
     *
     * @return data transfer object <code>ResponseError.class</code> with status 404.
     */
    @ExceptionHandler(DocumentNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public
    @ResponseBody
    ResponseError handleException(DocumentNotFoundException e) {
        LOG.error(e.getError().getMessage(), e);
        return handleException(HttpStatus.NOT_FOUND, e);
    }
}
