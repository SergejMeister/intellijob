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
import com.intellijob.elasticsearch.SearchModel;
import com.intellijob.elasticsearch.SearchModelBuilder;
import com.intellijob.elasticsearch.domain.EsJobDetail;
import com.intellijob.elasticsearch.domain.EsUserSkills;
import com.intellijob.elasticsearch.repository.EsUserSkillsRepository;
import com.intellijob.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * View model to represent job detail page.
 */
@RestController
public class ViewJobDetailServices extends BaseServices {

    private final static Logger LOG = LoggerFactory.getLogger(ViewJobDetailServices.class);

    @Autowired
    private UserController userController;

    @Autowired
    private JobDetailController jobDetailController;

    @Autowired
    private EsUserSkillsRepository esUserSkillsRepository;

    /**
     * Returns user data for specified userId.
     *
     * @return response user.
     */
    @RequestMapping(value = Endpoints.API_VIEWS_JOBDETAILS, method = RequestMethod.GET)
    public
    @ResponseBody
    JobDetailViewModel getJobDetailViewModel() {
        User user = new User();
        List<EsUserSkills> esUserSkills = new ArrayList<>();
        try {
            user = userController.getUniqueUser();
            esUserSkills = esUserSkillsRepository.findByUserId(user.getId());
        } catch (UserNotFoundException e) {
            LOG.warn("ViewJobDetailServices.getJobDetailViewModel: User not found. So return all jobDetails, without any searchFilter!");
        }
        SearchModel searchModel = new SearchModelBuilder(user).build();
        Page<EsJobDetail> jobDetailsPage = jobDetailController.findAndSort(searchModel);
        return new JobDetailViewModel(user, esUserSkills, jobDetailsPage, Boolean.FALSE);
    }
}
