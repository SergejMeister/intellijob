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

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * This Domain object represent job links contains in mail context.
 */
@Document(collection = "joblinks")
public class JobLink {

    /**
     * Object id.
     */
    @Id
    private String id;

    /**
     *
     */
    @Reference
    private Mail mail;

    /**
     * This is full html <code>a</code> tag.
     * <p>
     * Example: <a href="testhref">Demolabel</a>.
     */
    private String atag;

    /**
     * This is href link in html a tag.
     * <p>
     * Example: <a href="testhref">Demolabel</a>.
     * href=testhref.
     */
    private String href;

    /**
     * This is value in html a tag.
     * <p>
     * Example: <a href="testhref">DemoValue</a>.
     * value=DemoValue.
     */
    private String value;


    public Mail getMail() {
        return mail;
    }

    public void setMail(Mail mail) {
        this.mail = mail;
    }

    public String getAtag() {
        return atag;
    }

    public void setAtag(String atag) {
        this.atag = atag;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobLink)) {
            return false;
        }

        JobLink jobLink = (JobLink) o;

        if (!id.equals(jobLink.id)) {
            return false;
        }
        if (!getAtag().equals(jobLink.getAtag())) {
            return false;
        }
        return getHref().equals(jobLink.getHref()) && getValue().equals(jobLink.getValue());
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + getAtag().hashCode();
        result = 31 * result + getHref().hashCode();
        result = 31 * result + getValue().hashCode();
        return result;
    }
}
