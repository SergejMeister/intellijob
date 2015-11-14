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

import com.intellijob.controllers.SkillController;
import com.intellijob.controllers.UserController;
import com.intellijob.domain.User;
import com.intellijob.dto.response.ResponseUserForm;
import com.intellijob.models.SkillViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * View user Web-Services.
 * <p>
 * A service to generate an user web form <code>/views/users**</code>
 */
@RestController
public class ViewUserService extends BaseServices {

    @Autowired
    private UserController userController;

    @Autowired
    private SkillController skillController;

    /**
     * Returns user data for specified userId.
     *
     * @return response user.
     */
    @RequestMapping(value = Endpoints.API_VIEWS_USERS_BY_ID, method = RequestMethod.GET)
    public @ResponseBody ResponseUserForm getUserForm(@PathVariable String userId) throws Exception {
        User user = userController.getUser(userId);

        SkillViewModel skillViewModel = skillController.getSkillViewModel();
        return new ResponseUserForm(user, skillViewModel);
    }

}
