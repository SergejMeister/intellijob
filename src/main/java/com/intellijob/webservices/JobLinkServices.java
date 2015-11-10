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

import com.intellijob.controllers.JobLinkController;
import com.intellijob.domain.JobLink;
import com.intellijob.dto.response.ResponseJobLinkData;
import com.intellijob.dto.response.ResponseJobLinkTableData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Services to handle Endpoint /joblinks
 */
@RestController
public class JobLinkServices extends BaseServices {

    @Autowired
    private JobLinkController jobLinkController;

    /**
     * Request Get all jobLinks.
     *
     * @return data transfer object <code>ResponseJobLinkTableData.java</code>
     */
    @RequestMapping(value = Endpoints.JOBLINKS, method = RequestMethod.GET)
    public @ResponseBody ResponseJobLinkTableData getJobLinks() {
        List<JobLink> jobLinks = jobLinkController.findAll();
        return new ResponseJobLinkTableData(jobLinks);
    }

    /**
     * Request Get all jobLinks with paging.
     *
     * @return data transfer object <code>ResponseJobLinkTableData.java</code>
     */
    @RequestMapping(value = Endpoints.JOBLINKS_PAGING, method = RequestMethod.GET)
    public @ResponseBody ResponseJobLinkTableData getJobLinks(@PathVariable int pageIndex, @PathVariable int limit) {
        Page<JobLink> jobLinkPage = jobLinkController.findAll(pageIndex, limit);
        return new ResponseJobLinkTableData(jobLinkPage);
    }

    /**
     * Request to delete a jobLink by specified id.
     *
     * @return data transfer object <code>ResponseJobLinkData</code>
     */
    @RequestMapping(value = Endpoints.JOBLINKS_BY_ID, method = RequestMethod.DELETE)
    public @ResponseBody ResponseJobLinkData deleteJobLink(@PathVariable String jobLinkId) throws Exception {
        jobLinkController.deleteById(jobLinkId);
        return new ResponseJobLinkData(jobLinkId);
    }
}
