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

/**
 * All endpoint constants.
 */
public class Endpoints {

    public static final String API = "/api";
    public static final String MAILS = API + "/mails";
    public static final String MAILS_PAGING = MAILS + "/{pageIndex}/{limit}";
    public static final String MAIL_BY_ID = MAILS + "/{mailId}";
    public static final String MAILS_SEARCH = MAILS + "/search";
    public static final String JOBLINKS = API + "/joblinks";
    public static final String JOBLINKS_PAGING = JOBLINKS + "/{pageIndex}/{limit}";
    public static final String JOBS = API + "/jobs";
    public static final String JOBS_PAGING = JOBS + "/{pageIndex}/{limit}";
    public static final String JOBS_DOWNLOAD = JOBS + "/download";
    public static final String JOBS_BY_JOBLINK_ID_DOWNLOAD = JOBS + "/{jobLinkId}/download";
    public static final String JOBS_BY_ID = JOBS + "/{jobId}";
}
