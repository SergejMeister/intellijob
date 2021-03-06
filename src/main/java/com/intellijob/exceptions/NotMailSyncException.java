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

package com.intellijob.exceptions;

import com.intellijob.enums.Error;

/**
 * Exception, if last mail sync date exception is null or not exist!
 * <p>
 * Default Error Code - 5000002 <code>Error.NOT_LAST_MAIL_SYNC_DATE</code>
 */
public class NotMailSyncException extends BaseException {

    /**
     * Default constructor.
     */
    public NotMailSyncException() {
        super(Error.NOT_LAST_MAIL_SYNC_DATE);
    }
}
