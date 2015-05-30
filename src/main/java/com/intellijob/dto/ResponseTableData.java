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

package com.intellijob.dto;

/**
 * Base Data transfer object for all tables data.
 */
public abstract class ResponseTableData extends ResponseData {

    protected long totalItemSize;
    protected int totalPages;

    public ResponseTableData() {
    }

    public ResponseTableData(long totalItemSize, int totalPages) {
        this.totalItemSize = totalItemSize;
        this.totalPages = totalPages;
    }

    /**
     * Returns total item size.
     *
     * @return total item size.
     */
    public long getTotalItemSize() {
        return totalItemSize;
    }

    /**
     * Sets total item size.
     *
     * @param totalItemSize total item size.
     */
    public void setTotalItemSize(long totalItemSize) {
        this.totalItemSize = totalItemSize;
    }

    /**
     * Returns total page size.
     *
     * @return total page size.
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * Sets total page size.
     *
     * @param totalPages total pages size.
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
