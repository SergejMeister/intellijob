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

import com.intellijob.mail.enums.MailError;

import java.io.Serializable;

/**
 * Created by Sergej Meister on 4/30/15.
 */
public class ResponseMailSearchData implements Serializable {

    private String message;
    private MailError error;

    public ResponseMailSearchData() {
    }

    public ResponseMailSearchData(String message) {
        this.message=message;
    }

    public ResponseMailSearchData(MailError error) {
        this.error=error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MailError getError() {
        return error;
    }

    public void setError(MailError error) {
        this.error = error;
    }
}
