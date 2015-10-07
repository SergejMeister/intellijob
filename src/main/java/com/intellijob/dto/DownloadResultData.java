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
 * Data transfer object to represent download result.
 */
public class DownloadResultData extends ResponseData {

    private int downloadedCount;

    private int notFoundedCount;

    public DownloadResultData() {
        this(0, 0);
    }

    public DownloadResultData(int downloadedCount, int notFoundedCount) {
        setDownloadedCount(downloadedCount);
        setNotFoundedCount(notFoundedCount);
    }

    public void add(DownloadResultData downloadResultData) {
        this.downloadedCount = this.downloadedCount + downloadResultData.getDownloadedCount();
        this.notFoundedCount = this.notFoundedCount + downloadResultData.getNotFoundedCount();
    }

    public int getDownloadedCount() {
        return downloadedCount;
    }

    public void setDownloadedCount(int downloadedCount) {
        this.downloadedCount = downloadedCount;
    }

    public int getNotFoundedCount() {
        return notFoundedCount;
    }

    public void setNotFoundedCount(int notFoundedCount) {
        this.notFoundedCount = notFoundedCount;
    }
}
