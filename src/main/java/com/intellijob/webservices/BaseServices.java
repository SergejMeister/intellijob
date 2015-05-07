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

package com.intellijob.webservices;

import com.intellijob.mail.dto.ResponseError;
import com.intellijob.mail.exception.BaseMailException;
import org.springframework.http.HttpStatus;

/**
 * Base service class.
 */
public abstract class BaseServices {

    protected ResponseError handleException(HttpStatus status, BaseMailException bme) {
        return new ResponseError(status.value(), bme.getMailError().getCode(), bme.getMailError().getMessage());
    }
}
