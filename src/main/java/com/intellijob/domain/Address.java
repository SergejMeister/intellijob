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

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Document to represent address of organisation.
 */
@Document(collection = "address")
public class Address {

    public static final String CHAR_SEPARATOR_WHITESPACE = " ";

    private String street;
    private String streetNumber;
    private String city;
    private String zip;
    private String country;

    public Address() {
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Get all address data separate by whitespace.
     */
    public String getAll() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getStreet());
        stringBuilder.append(CHAR_SEPARATOR_WHITESPACE);
        stringBuilder.append(getStreetNumber());
        stringBuilder.append(CHAR_SEPARATOR_WHITESPACE);
        stringBuilder.append(getZip());
        stringBuilder.append(CHAR_SEPARATOR_WHITESPACE);
        stringBuilder.append(getCity());
        if (StringUtils.isNotBlank(getCountry())) {
            stringBuilder.append(CHAR_SEPARATOR_WHITESPACE);
            stringBuilder.append(country);
        }

        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", streetNumber='" + streetNumber + '\'' +
                ", city='" + city + '\'' +
                ", zip='" + zip + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
