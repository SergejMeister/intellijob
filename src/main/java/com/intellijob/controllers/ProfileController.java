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
import com.intellijob.exceptions.NotMailSyncException;

import java.util.Date;

/**
 * Interface profile controller.
 * <p>
 * This is a layer between services and dao.
 * The domain object profile is not finished and include right now only one attribute - last mail sync date.
 */
public interface ProfileController {

    /**
     * Create or update profile.
     *
     * @param profile profile to save.
     *
     * @return profile with id.
     */
    Profile save(Profile profile);

    /**
     * Create or update profile with last mail sync date.
     * <p>
     * This is a simple method to store last mail sync date for one user.
     * Get all profiles:
     * if null or empty , than create profile with given last mail sync date.
     * if profile not empty, than get first profile (should be only one profile :) )
     * and update with new date.
     * <p>
     * NOTE: use this method, while user management not implemented.
     *
     * @param lastMailSyncDate last mail sync date.
     *
     * @return profile with id.
     */
    Profile simpleSave(Date lastMailSyncDate);

    /**
     * Returns last mail sync date.
     *
     * @return last mail sync date.
     * @throws NotMailSyncException exception last mail sync date is null.
     */
    Date getLastMailSyncDate() throws NotMailSyncException;
}
