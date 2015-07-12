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

package com.intellijob.domain;

import civis.com.utils.opennlp.ContactPersonSpan;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;

/**
 * Model ContactPerson.
 */
@Document(collection = "contactPerson")
public class ContactPerson {

    public static final String SEX_PREFIX_MAN = "M";
    public static final String SEX_PREFIX_WOMEN = "W";
    public static final String SEX_PREFIX_UNKNOWN = "N";


    private String firstName;

    private String secondName;

    private String sex;


    /**
     * Default constructor
     */
    public ContactPerson() {
    }

    /**
     * Constructor with parameters.
     *
     * @param firstName  firstName.
     * @param secondName secondName.
     * @param sex        sex.
     */
    public ContactPerson(String firstName, String secondName, String sex) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.sex = sex;
    }

    public ContactPerson(ContactPersonSpan contactPersonSpan) {
        this.firstName = contactPersonSpan.getFirstName();
        this.secondName = contactPersonSpan.getSecondName();
        this.sex = contactPersonSpan.getSex();
    }

    /**
     * REturns firstname.
     *
     * @return firstname.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets firstname.
     *
     * @param firstName firstname.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns secondname.
     *
     * @return secondname.
     */
    public String getSecondName() {
        return secondName;
    }

    /**
     * Sets second name.
     *
     * @param secondName secondname.
     */
    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    /**
     * Returns sex.
     * <p>
     * <p>
     * M  - for man.
     * W  - for wife.
     * N  - for unknown.
     * </p>
     *
     * @return M, W, N.
     */
    public String getSex() {
        return sex;
    }

    /**
     * Sets sex.
     * <p>
     * Expected values:
     * <p>
     * M  - for man.
     * W  - for wife.
     * N  - for unknown.
     * </p>
     *
     * @param sex sex.
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * Returns firstName + " " + secondName;
     * <p>
     * If firstName contains null, than return only lastName.
     *
     * @return firstName + " " + secondName;
     */
    public String getFirstAndSecondName() {
        if (StringUtils.hasLength(firstName)) {
            return firstName + " " + secondName;
        }

        return secondName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ContactPerson{" +
                "sex='" + sex + '\'' +
                ",firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                '}';
    }


}
