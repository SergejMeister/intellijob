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


import com.intellijob.controllers.JobDetailController;
import com.intellijob.domain.JobDetail;
import com.intellijob.dto.ResponseJobDetailData;
import com.intellijob.dto.ResponseJobDetailTableData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * JobDetail-Services.
 * <p>
 * Handle all request with endpoints <code>/jobs**</code>
 */
@RestController
public class JobDetailServices {


    @Autowired
    private JobDetailController jobDetailController;

    /**
     * Returns all job details.
     * <p>
     * New GET-request <code>url</code>
     * Save html response into jobs collection.
     *
     * @return job data.
     */
    @RequestMapping(value = Endpoints.JOBDETAILS, method = RequestMethod.GET)
    public @ResponseBody ResponseJobDetailTableData getAllJobDetails() {
        List<JobDetail> jobDetails = jobDetailController.findAll();
        //returns without job content, only metadata.
        return new ResponseJobDetailTableData(jobDetails, Boolean.FALSE);
    }

    /**
     * Request Get all job detail with paging.
     *
     * @return data transfer object <code>ResponseJobDetailTableData.java</code>
     */
    @RequestMapping(value = Endpoints.JOBDETAILS_PAGING, method = RequestMethod.GET)
    public @ResponseBody ResponseJobDetailTableData getJobs(@PathVariable int pageIndex, @PathVariable int limit) {
        Page<JobDetail> jobDetailsPage = jobDetailController.findPage(pageIndex, limit);
        return new ResponseJobDetailTableData(jobDetailsPage, Boolean.FALSE);
    }

    /**
     * Request Get all job detail with paging.
     *
     * @return data transfer object <code>ResponseJobDetailTableData.java</code>
     */
    @RequestMapping(value = Endpoints.JOBDETAILS_BY_ID, method = RequestMethod.GET)
    public @ResponseBody ResponseJobDetailData getJobs(@PathVariable String jobDetailId) throws Exception {
        JobDetail jobDetail = jobDetailController.findAndConvertContentToText(jobDetailId);
        return new ResponseJobDetailData(jobDetail, Boolean.TRUE);
    }
}
