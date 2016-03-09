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

import com.intellijob.BaseTester;
import com.intellijob.domain.Profile;
import com.intellijob.domain.User;
import com.intellijob.exceptions.NotMailSyncException;
import com.intellijob.exceptions.UserNotFoundException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class UserControllerTest extends BaseTester {

    @Autowired
    private UserController userController;

    @Test
    public void testUpdateMailSyncDateIfLastDateNull() {
        //init test data.
        User user = initDefaultUser();
        userRepository.save(user);

        //Do test
        Assert.assertNull("LastMailSyncDate should be null!", user.getProfile().getLastMailSyncDate());
        Profile profile = userController.updateMailSyncDate(user);
        Assert.assertNotNull("LastMailSyncDate should not be null!", profile.getLastMailSyncDate());

        User testUser = userRepository.findOne(user.getId());
        Assert.assertEquals(testUser.getProfile().getLastMailSyncDate(), profile.getLastMailSyncDate());

        //Rollback testUser
        userRepository.delete(user);
    }

    @Test
    public void testUpdateMailSyncDate() {
        //init test data.
        Date currentDate = new Date();
        User user = initDefaultUser();
        user.getProfile().setLastMailSyncDate(currentDate);
        userRepository.save(user);

        //Do test
        Profile profile = userController.updateMailSyncDate(user);
        Assert.assertTrue(profile.getLastMailSyncDate().getTime() > currentDate.getTime());

        User testUser = userRepository.findOne(user.getId());
        Assert.assertEquals(testUser.getProfile().getLastMailSyncDate(), profile.getLastMailSyncDate());

        //Rollback testUser
        userRepository.delete(user);
    }

    @Test
    public void testGetLastMailSyncDate() {
        //init test data.
        Date currentDate = new Date();
        User user = initDefaultUser();
        user.getProfile().setLastMailSyncDate(currentDate);
        userRepository.save(user);

        //do test
        try {
            Date testDate = userController.getLastMailSyncDate();
            Assert.assertEquals(currentDate, testDate);
        } catch (UserNotFoundException | NotMailSyncException multiException) {
            Assert.fail("Should be no exception!");
        } finally {
            //Rollback testUser
            userRepository.delete(user);
        }
    }

    @Test
    public void testGetLastMailSyncDateIfNull() {
        //init test data.
        User user = initDefaultUser();
        userRepository.save(user);

        //do test
        try {
            userController.getLastMailSyncDate();
            Assert.fail("Should be an exception!");
        } catch (NotMailSyncException | UserNotFoundException multiException) {
            Assert.assertNotNull(multiException);
            Assert.assertEquals(Integer.valueOf(5000002), multiException.getError().getCode());
            Assert.assertEquals("Last date of mails synchronization is not founded!",
                    multiException.getError().getMessage());
        } finally {
            //Rollback testUser
            userRepository.delete(user);
        }
    }

    @Test
    public void testGetLastMailSyncDateIfUserNotFound() {
        try {
            userController.getLastMailSyncDate();
            Assert.fail("Should be an exception!");
        } catch (NotMailSyncException | UserNotFoundException multiException) {
            Assert.assertNotNull(multiException);
            Assert.assertEquals(Integer.valueOf(5000006), multiException.getError().getCode());
            Assert.assertEquals("User can not be found!", multiException.getError().getMessage());
        }
    }

    @Test
    public void testGetUniqueUserForEmptyUserCollection() {

        try {
            userController.getUniqueUser();
            Assert.fail("Should be an exception!");
        } catch (UserNotFoundException unfe) {
            Assert.assertNotNull(unfe);
            Assert.assertEquals(Integer.valueOf(5000006), unfe.getError().getCode());
            Assert.assertEquals("User can not be found!", unfe.getError().getMessage());
        }
    }

    @Test
    public void testGetUniqueUserForMultiUserCollection() {
        //init test data -> create 3 users to check unique exceptions.
        List<User> testUsers = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            User user = initDefaultUser();
            testUsers.add(user);
        }
        userRepository.save(testUsers);

        try {
            userController.getUniqueUser();
            Assert.fail("Should be an exception!");
        } catch (UserNotFoundException unfe) {
            Assert.assertNotNull(unfe);
            Assert.assertEquals(Integer.valueOf(5000006), unfe.getError().getCode());
            Assert.assertEquals("User can not be found!", unfe.getError().getMessage());
        } finally {
            //Rollback testUser
            userRepository.delete(testUsers);
        }
    }

    @Test
    public void testGetUniqueUser() {
        //init test data.
        User user = initDefaultUser();
        userRepository.save(user);

        //run test
        try {
            User uniqueUser = userController.getUniqueUser();
            Assert.assertEquals(USER_PROFILE_DEFAULT_FIRSTNAME, uniqueUser.getProfile().getFirstName());
            Assert.assertEquals(USER_PROFILE_DEFAULT_SECONDNAME, uniqueUser.getProfile().getSecondName());
            Assert.assertEquals(USER_PROFILE_DEFAULT_SEX, uniqueUser.getProfile().getSex());
            Assert.assertNull("MailSyncDate should be null!", uniqueUser.getProfile().getLastMailSyncDate());
        } catch (UserNotFoundException unfe) {
            Assert.fail("Should be no exception!");
        } finally {
            //Rollback testUser
            userRepository.delete(user);
        }
    }

    @Test
    public void testLiveGetUniqueUser() throws Exception {
        if (!isProduction) {
            Assert.assertTrue("Don't run this test.", Boolean.TRUE);
            return;
        }

        User userMe = userController.getUniqueUser();
        Assert.assertEquals("Sergej", userMe.getProfile().getFirstName());
        Assert.assertEquals("Meister", userMe.getProfile().getSecondName());
        Assert.assertNotNull("LastMailSyncDate should not be null!", userMe.getProfile().getLastMailSyncDate());
    }

    @Test
    public void testLiveGetLastMailSyncDate() throws Exception {
        if (!isProduction) {
            Assert.assertTrue("Don't run this test.", Boolean.TRUE);
            return;
        }

        Date lastMailSyncDate = userController.getLastMailSyncDate();
        Assert.assertNotNull("LastMailSyncDate should not be null!", lastMailSyncDate);
    }
}