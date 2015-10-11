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

package com.intellijob.repository;

import com.intellijob.BaseTester;
import com.intellijob.domain.Profile;
import com.intellijob.domain.User;
import com.intellijob.exceptions.NotMailSyncException;
import com.intellijob.repository.user.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class UserRepositoryTest extends BaseTester {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSave() throws NotMailSyncException {
        Date date = new Date();
        Profile profile = new Profile();
        profile.setFirstName("Sergej");
        profile.setSecondName("Meister");
        profile.setLastMailSyncDate(date);

        User user = new User();
        user.setProfile(profile);

        User saveResult = userRepository.save(user);
        Assert.assertNotNull("Id should be not null!", saveResult.getId());

        User findResult = userRepository.findOne(saveResult.getId());
        Assert.assertEquals("Sergej", findResult.getProfile().getFirstName());
        Assert.assertEquals("Meister", findResult.getProfile().getSecondName());
        Assert.assertEquals(date, findResult.getProfile().getLastMailSyncDate());
    }
}