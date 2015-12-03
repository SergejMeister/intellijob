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

import com.intellijob.controllers.UserController;
import com.intellijob.domain.User;
import com.intellijob.dto.request.RequestUserData;
import com.intellijob.dto.response.ResponseUserData;
import com.intellijob.dto.response.UserViewModel;
import com.intellijob.exceptions.UserNotFoundException;
import com.intellijob.webservices.mappers.UserServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * User Web-Services.
 * <p>
 * Handle all request with endpoints <code>/users**</code>
 */
@RestController
public class UserServices extends BaseServices {

    @Autowired
    private UserController userController;

    /**
     * Returns unique user data.
     * <p>
     * New GET-request <code>url</code>
     * Save html response into jobs collection.
     *
     * @return job data.
     */
    @RequestMapping(value = Endpoints.USERS, method = RequestMethod.GET)
    public @ResponseBody ResponseUserData getUser() {
        try {
            User user = userController.getUniqueUser();
            return new ResponseUserData(user, false);
        } catch (UserNotFoundException e) {
            return new ResponseUserData();
        }
    }

    /**
     * Update user resource.
     *
     * @return response message.
     */
    @RequestMapping(value = Endpoints.USERS, method = RequestMethod.PUT)
    public @ResponseBody UserViewModel updateUser(@RequestBody RequestUserData userData) {
        User user = UserServiceMapper.mapTo(userData);
        userController.save(user);
        UserViewModel responseUserForm = new UserViewModel();
        responseUserForm.setMessage("User updated successfully!");
        return responseUserForm;
    }

    /**
     * Create a new user resource.
     *
     * @return response user data with id and message.
     */
    @RequestMapping(value = Endpoints.USERS, method = RequestMethod.POST)
    public @ResponseBody UserViewModel createUser(@RequestBody RequestUserData userData) {
        User user = UserServiceMapper.mapTo(userData);
        User createdUser = userController.save(user);
        UserViewModel responseUserForm = new UserViewModel(createdUser);
        responseUserForm.setMessage("New user Created successfully!");
        return responseUserForm;
    }

    /**
     * Delete an user resource for specified id.
     *
     * @param userId userId.
     *
     * @return response user data with id.
     */
    @RequestMapping(value = Endpoints.USERS, method = RequestMethod.DELETE)
    public @ResponseBody UserViewModel deleteUser(@RequestParam(value = "userId") String userId) {
        userController.deleteUser(userId);
        UserViewModel responseUserForm = new UserViewModel();
        responseUserForm.setMessage("User is successfully deleted!");
        return responseUserForm;
    }
}
