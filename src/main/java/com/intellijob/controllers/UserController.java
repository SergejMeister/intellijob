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

package com.intellijob.controllers;

import com.intellijob.domain.Profile;
import com.intellijob.domain.User;
import com.intellijob.elasticsearch.domain.EsUserSkills;
import com.intellijob.exceptions.NotMailSyncException;
import com.intellijob.exceptions.UserNotFoundException;

import java.util.Date;
import java.util.List;

/**
 * Interface profile controller.
 * <p>
 * This is a layer between services and dao.
 * The domain object profile is not finished and include right now only one attribute - last mail sync date.
 */
public interface UserController {

    /**
     * Create or update profile data lastMailSyncDate with current data.
     * <p>
     * profile.setLastMailSyncDate(new Date());
     *
     * @param user affected user
     *
     * @return updated profile.
     */
    Profile updateMailSyncDate(User user);

    /**
     * Returns last mail sync date.
     *
     * @param userId affected user id.
     *
     * @return last mail sync date.
     * @throws UserNotFoundException exception if no user found for specified userId.
     * @throws NotMailSyncException  exception last mail sync date is null.
     */
    Date getLastMailSyncDate(String userId) throws UserNotFoundException, NotMailSyncException;

    /**
     * Returns last mail sync date.
     * <p>
     * Find all users.
     * If no users found that trow UserNotFoundException.
     * If more than one users that trow UserNotFoundException.
     * If exact one user found, than get user profile.
     *
     * @return last mail sync date.
     * @throws UserNotFoundException exception if no user found for specified userId.
     * @throws NotMailSyncException  exception last mail sync date is null.
     */
    Date getLastMailSyncDate() throws UserNotFoundException, NotMailSyncException;

    /**
     * Returns an user.
     * <p>
     * The users collection should include always only one user, because the multi user is not implemented!
     * <p>
     * Find all users.
     * If no users found that trow UserNotFoundException.
     * If more than one users that trow UserNotFoundException.
     * If exact one user found, than get user.
     *
     * @return first founded user.
     * @throws UserNotFoundException exception if no user found for specified userId.
     */
    User getUniqueUser() throws UserNotFoundException;

    /**
     * Returns an user for specified id.
     *
     * @param userId user id
     *
     * @return user data.
     * @throws UserNotFoundException if user not found.
     */
    User getUser(String userId) throws UserNotFoundException;

    /**
     * Create or update a user.
     *
     * @param user user to create.
     *
     * @return created user with id.
     */
    User save(User user);

    /**
     * Delete user by id.
     *
     * @param userId user id.
     */
    void deleteUser(String userId);

    /**
     * Returns all user skills from Elasticsearch.
     *
     * @param userId user id.
     *
     * @return list of user skills.
     */
    List<EsUserSkills> getUserSkills(String userId);
}
