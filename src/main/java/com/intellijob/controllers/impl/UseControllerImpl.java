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

package com.intellijob.controllers.impl;

import com.intellijob.controllers.UserController;
import com.intellijob.domain.Profile;
import com.intellijob.domain.User;
import com.intellijob.exceptions.NotMailSyncException;
import com.intellijob.exceptions.UserNotFoundException;
import com.intellijob.repository.user.UserProfileRepository;
import com.intellijob.repository.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;


/**
 * Represents profile controllers.
 */
@Controller
public class UseControllerImpl implements UserController {

    private final static Logger LOG = LoggerFactory.getLogger(UseControllerImpl.class);

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Profile updateMailSyncDate(User user) {
        Profile profile = user.getProfile();
        if (profile == null) {
            profile = new Profile();
        }

        profile.setLastMailSyncDate(new Date());
        user.setProfile(profile);

        User savedUser = userRepository.save(user);
        return savedUser.getProfile();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getLastMailSyncDate(String userId) throws UserNotFoundException, NotMailSyncException {
        User user = userProfileRepository.findByUserId(userId);
        if (user == null) {
            throw new UserNotFoundException();
        }

        return getProfile(user).getLastMailSyncDate();
    }

    private Profile getProfile(User user) throws NotMailSyncException {
        Profile profile = user.getProfile();
        if (profile == null || profile.getLastMailSyncDate() == null) {
            throw new NotMailSyncException();
        }

        return profile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getLastMailSyncDate() throws UserNotFoundException, NotMailSyncException {
        User user = getUniqueUser();
        return getProfile(user).getLastMailSyncDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUniqueUser() throws UserNotFoundException {
        List<User> allUsers = userRepository.findAll();
        if (allUsers == null || allUsers.isEmpty() || allUsers.size() > 1) {
            throw new UserNotFoundException();
        }

        int firstUserIndex = 0;
        return allUsers.get(firstUserIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUser(String userId) throws UserNotFoundException {
        User user = userRepository.findById(userId);
        if (user == null) {
            LOG.error("No user for id: " + userId);
            throw new UserNotFoundException();
        }
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User save(User user) {
        User createUser = userRepository.save(user);
        return createUser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteUser(String userId) {
        userRepository.delete(userId);
    }

}
