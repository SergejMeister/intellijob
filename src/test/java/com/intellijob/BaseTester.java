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

package com.intellijob;

import com.intellijob.domain.Profile;
import com.intellijob.domain.User;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestApplicationConfig.class})
@WebAppConfiguration
@ComponentScan
@Configuration
public abstract class BaseTester {

    protected static final String USER_PROFILE_DEFAULT_FIRSTNAME = "TestFirstName";
    protected static final String USER_PROFILE_DEFAULT_SECONDNAME = "TestSecondName";
    protected static final String USER_PROFILE_DEFAULT_SEX = "M";
    protected final static String DEFAULT_ENCODING = "UTF-8";
    /**
     * Constants.
     */
    protected final static Boolean RUNNING_LIVE = TestApplicationConfig.LIVE_MONGODB;
    protected final static Logger LOG = LoggerFactory.getLogger(BaseTester.class);

    protected User initDefaultUser() {
        Profile profile = new Profile();
        profile.setFirstName(USER_PROFILE_DEFAULT_FIRSTNAME);
        profile.setSecondName(USER_PROFILE_DEFAULT_SECONDNAME);
        profile.setSex(USER_PROFILE_DEFAULT_SEX);

        User defaultUser = new User();
        defaultUser.setProfile(profile);
        return defaultUser;
    }
}
