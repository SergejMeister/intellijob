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

package com.intellijob.mail.dto;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data transfer object for errors.
 *
 * @author Daniil Tomilow.
 */
public class ResponseError implements Serializable {

    private static final String API_URL = "not exist yet!";

    private final int status; // Http status
    private final int code;
    private final String message;
    private final String developerInfo;

    private final Map<String, List<String>> errors;

    /**
     * ResponseError constructor for binding errors.
     *
     * @param status  the status
     * @param code    the code
     * @param message the message
     * @param errors  the errors
     */
    public ResponseError(int status, int code, String message, Errors errors) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.developerInfo = "More info: " + API_URL + "/" + code;

        Map<String, List<String>> inputErrors = new HashMap<>();
        if (errors != null) {
            for (FieldError error : errors.getFieldErrors()) {
                List<String> messages = new ArrayList<>();
                messages.add(error.getDefaultMessage());
                System.out.println(error.toString());
                inputErrors.put(error.getField(), messages);
            }
        }

        this.errors = inputErrors;
    }

    /**
     * ResponseError constructor for binding errors.
     *
     * @param status  the status
     * @param code    the code
     * @param message the message
     */
    public ResponseError(int status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.developerInfo = "More info: " + API_URL + "/" + code;
        this.errors = Collections.emptyMap();
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @return the developerInfo
     */
    public String getDeveloperInfo() {
        return developerInfo;
    }

    /**
     * @return the developerInfo
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return the errors
     */
    public Map<String, List<String>> getErrors() {
        return errors;
    }
}
