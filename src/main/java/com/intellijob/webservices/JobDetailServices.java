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
import com.intellijob.controllers.UserController;
import com.intellijob.domain.JobDetail;
import com.intellijob.domain.User;
import com.intellijob.dto.response.ResponseJobDetailData;
import com.intellijob.dto.response.ResponseJobDetailTableData;
import com.intellijob.elasticsearch.SearchModel;
import com.intellijob.elasticsearch.SearchModelBuilder;
import com.intellijob.elasticsearch.domain.EsJobDetail;
import com.intellijob.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * JobDetail-Services.
 * <p>
 * Handle all request with endpoints <code>/jobdetails**</code>
 */
@RestController
public class JobDetailServices extends BaseServices {

    @Autowired
    private UserController userController;

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
    public
    @ResponseBody
    ResponseJobDetailTableData getAllJobDetails() {
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
    public @ResponseBody ResponseJobDetailTableData getJobDetails(@PathVariable int offset, @PathVariable int limit,
                                             @RequestParam(value = "searchFilter", required = false) String searchFilter,
                                             @RequestParam(value = "searchData", required = false) String searchData)
            throws UserNotFoundException {
        User user = userController.getUniqueUser();

        SearchModelBuilder searchModelBuilder = new SearchModelBuilder(user).setOffset(offset).setLimit(limit);
        if (searchFilter != null) {
            searchModelBuilder.setSearchEngine(searchFilter);
        }
        if (searchData != null) {
            searchModelBuilder.setSearchData(searchData);
        }
        SearchModel searchModel = searchModelBuilder.build();

        Page<EsJobDetail> jobDetailsPage = jobDetailController.findAndSort(searchModel);
        return new ResponseJobDetailTableData(jobDetailsPage, Boolean.FALSE);
    }

    /**
     * Request Get all job detail with paging.
     *
     * @return data transfer object <code>ResponseJobDetailTableData.java</code>
     */
    @RequestMapping(value = Endpoints.JOBDETAILS_BY_ID, method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseJobDetailData getJobDetail(@PathVariable String jobDetailId) throws Exception {
        JobDetail jobDetail = jobDetailController.findAndConvertContentToText(jobDetailId);
        return new ResponseJobDetailData(jobDetail, Boolean.TRUE);
    }

    /**
     * Delete jobDetails by id.
     *
     * @return data transfer object <code>ResponseJobDetailData.java</code>
     */
    @RequestMapping(value = Endpoints.JOBDETAILS_BY_ID, method = RequestMethod.DELETE)
    public
    @ResponseBody
    ResponseJobDetailData deleteJobDetail(@PathVariable String jobDetailId) throws Exception {
        jobDetailController.deleteById(jobDetailId);
        return new ResponseJobDetailData(jobDetailId);
    }

    @RequestMapping(value = Endpoints.JOBDETAILS_BY_ID, method = RequestMethod.PUT)
    public ResponseEntity updateReadState(@PathVariable String jobDetailId,
                                          @RequestParam(value = "read") Boolean read) {
        jobDetailController.updateReadState(jobDetailId, read);
        return ResponseEntity.accepted().build();
    }

    @RequestMapping(value = Endpoints.JOBDETAILS, method = RequestMethod.PUT)
    public ResponseEntity updateReadState(@RequestParam(value = "ids") List<String> ids,
                                          @RequestParam(value = "read") Boolean read) {
        jobDetailController.updateReadState(ids, read);
        return ResponseEntity.accepted().build();
    }
}
