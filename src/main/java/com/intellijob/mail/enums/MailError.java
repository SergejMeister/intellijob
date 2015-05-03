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

public enum MailError implements Serializable {

    BAD_REQUEST (100400, "Bad Request"),
    BAD_AUTHENTICATION(100200, "Access is denied. Incorrect username/password!"),
    NOT_SUPPORTED_CONNECTION_TYPE(100000, "This connection type is not supported!") ,
    NOT_SUPPORTED_MAIL_ACCOUNT(100000, "This mail account is not supported!") ,
    BAD_SETTINGS (100010, "Mail settings could not be loaded successful!") ;

    private int code;
    private String message;

    private MailError(Integer code, String message) {
        this.code=code;
        this.message=message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
