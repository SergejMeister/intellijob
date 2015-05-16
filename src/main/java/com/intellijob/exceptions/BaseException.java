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
 * Base application exception.
 */
public abstract class BaseException extends Exception {

    protected Error error;

    public BaseException(BaseException be) {
        super(be.getError().getMessage());
        this.error = be.getError();
    }

    public BaseException(Error error) {
        super(error.getMessage());
        this.error = error;
    }

    public BaseException(Error error, Throwable cause) {
        super(error.getMessage(), cause);
        this.error = error;
    }

    /**
     * Returns error.
     *
     * @return error.
     */
    public Error getError() {
        return error;
    }
}
