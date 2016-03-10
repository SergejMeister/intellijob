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

import com.intellijob.controllers.ApplicationSettingsController;
import com.intellijob.controllers.SkillController;
import com.intellijob.controllers.UserController;
import com.intellijob.domain.User;
import com.intellijob.dto.SkillData;
import com.intellijob.dto.response.UserViewModel;
import com.intellijob.exceptions.UserNotFoundException;
import com.intellijob.models.SkillViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * View user Web-Services.
 * <p>
 * A service to generate an user web form <code>/views/users**</code>
 */
@RestController
public class ViewUserService extends BaseServices {

    private final static Logger LOG = LoggerFactory.getLogger(MailServices.class);

    @Autowired
    private UserController userController;

    @Autowired
    private SkillController skillController;

    @Autowired
    private ApplicationSettingsController applicationSettingsController;

    /**
     * Returns user data for specified userId.
     *
     * @return response user.
     */
    @RequestMapping(value = Endpoints.API_VIEWS_USERS_BY_ID, method = RequestMethod.GET)
    public UserViewModel getUserViewModel(@PathVariable String userId) throws Exception {
        SkillViewModel skillViewModel = skillController.getSkillViewModel();
        try {
            User user = userController.getUser(userId);
            return new UserViewModel(user, skillViewModel);
        } catch (UserNotFoundException usf) {
            LOG.info("ViewUserService.getUserViewModel: UserId is null! Create elasticSearch indexes.");
            applicationSettingsController.initApplication();
            return new UserViewModel(skillViewModel);
        }
    }

    /**
     * Returns user data for specified userId.
     *
     * @return response user.
     */
    @RequestMapping(value = Endpoints.API_VIEWS_SKILLS_KNOWLEDGES, method = RequestMethod.GET)
    public List<SkillData> getUserViewModel() throws Exception {
        return skillController.getKnowledges().stream().map(SkillData::new).collect(Collectors.toList());
    }
}
