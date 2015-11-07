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

package com.intellijob.dto.response;

import com.intellijob.domain.Profile;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Data transfer object represents domain object <code>Profile</code>
 */
public class ResponseProfileData extends ResponseData {

    private String firstName;

    private String secondName;

    private String sex;

    private String fullName;

    private Date lastMailSyncDate;


    public ResponseProfileData() {
    }

    public ResponseProfileData(String firstName, String secondName, String sex) {
        setFirstName(firstName);
        setSecondName(secondName);
        setSex(sex);
        setFullName(createFullName(firstName, secondName));
    }

    public ResponseProfileData(Profile profile) {
        this(profile.getFirstName(), profile.getSecondName(), profile.getSex());
        setLastMailSyncDate(profile.getLastMailSyncDate());
    }

    private String createFullName(String firstName, String secondName) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.hasLength(firstName)) {
            sb.append(firstName);
        }
        if (StringUtils.hasLength(secondName) && StringUtils.hasLength(firstName)) {
            sb.append("-");
            sb.append(secondName);
        }
        if (StringUtils.hasLength(secondName) && !StringUtils.hasLength(firstName)) {
            sb.append(secondName);
        }

        return sb.toString();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getLastMailSyncDate() {
        return lastMailSyncDate;
    }

    public void setLastMailSyncDate(Date lastMailSyncDate) {
        this.lastMailSyncDate = lastMailSyncDate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
