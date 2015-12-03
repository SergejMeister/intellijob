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
import com.intellijob.domain.User;
import com.intellijob.dto.response.JobDetailViewModel;
import com.intellijob.elasticsearch.domain.EsJobDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * View model to represent job detail page.
 */
@RestController
public class ViewJobDetailServices extends BaseServices {

    @Autowired
    private UserController userController;

    @Autowired
    private JobDetailController jobDetailController;

    /**
     * Returns user data for specified userId.
     *
     * @return response user.
     */
    @RequestMapping(value = Endpoints.API_VIEWS_JOBDETAILS, method = RequestMethod.GET)
    public @ResponseBody JobDetailViewModel getJobDetailViewModel() throws Exception {
        User user = userController.getUniqueUser();
        int pageIndex = 0;
        int limit = 50;
        Page<EsJobDetail> jobDetailsPage = jobDetailController.findAndSort(user, pageIndex, limit);
        return new JobDetailViewModel(user, jobDetailsPage, Boolean.FALSE);
    }
}
