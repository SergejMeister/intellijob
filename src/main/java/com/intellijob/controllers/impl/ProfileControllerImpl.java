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

import com.intellijob.controllers.ProfileController;
import com.intellijob.domain.Profile;
import com.intellijob.exceptions.NotMailSyncException;
import com.intellijob.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;

/**
 * Represents profile controllers.
 */
@Controller
public class ProfileControllerImpl implements ProfileController {

    @Autowired
    private ProfileRepository profileRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Profile save(Profile profile) {
        return profileRepository.save(profile);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Profile simpleSave(Date lastMailSyncDate) {
        List<Profile> profiles = profileRepository.findAll();
        if (profiles == null || profiles.isEmpty()) {
            Profile newProfile = new Profile();
            newProfile.setLastMailSyncDate(lastMailSyncDate);
            return save(newProfile);
        } else {
            int firstProfile = 0;
            Profile profileToUpdate = profiles.get(firstProfile);
            profileToUpdate.setLastMailSyncDate(lastMailSyncDate);
            return save(profileToUpdate);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getLastMailSyncDate() throws NotMailSyncException {
        List<Profile> profiles = profileRepository.findAll();
        if (profiles == null || profiles.isEmpty()) {
            throw new NotMailSyncException();
        }

        int firstProfile = 0;
        Profile profileToUpdate = profiles.get(firstProfile);
        if (profileToUpdate.getLastMailSyncDate() == null) {
            throw new NotMailSyncException();
        }

        return profileToUpdate.getLastMailSyncDate();
    }
}
