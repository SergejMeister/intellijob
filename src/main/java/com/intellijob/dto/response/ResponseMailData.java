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

import com.intellijob.domain.Mail;
import com.intellijob.mail.models.MailModel;

import java.util.Date;

/**
 * Response mail data to transfer model object <code>Mail</code>.
 */
public class ResponseMailData extends ResponseData {

    private String id;
    private String sentAddress;
    private String subject;
    private Date sentDate;
    private Date receivedDate;
    private String contentType;
    private String content;


    /**
     * Constructor for mailModel.
     *
     * @param mailModel model object <code>mailModel</code>.
     */
    public ResponseMailData(MailModel mailModel) {
        this.sentAddress = mailModel.getFrom().toString();
        this.subject = mailModel.getSubject();
        //DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMANY);
        this.sentDate = mailModel.getSentDate();
        this.receivedDate = mailModel.getReceivedDate();
        this.contentType = mailModel.getContentType();
        this.content = mailModel.getContent();
    }

    /**
     * Constructor for domain object mail.
     *
     * @param mailDomain mail.
     */
    public ResponseMailData(Mail mailDomain) {
        this.id = mailDomain.getId();
        this.sentAddress = mailDomain.getSentAddress();
        this.subject = mailDomain.getSubject();
        this.sentDate = mailDomain.getSentDate();
        this.receivedDate = mailDomain.getReceivedDate();
        this.contentType = mailDomain.getContentType();
        this.content = mailDomain.getContent();
    }

    public ResponseMailData(String mailId) {
        this.id = mailId;
    }

    public String getSentAddress() {
        return sentAddress;
    }

    public void setSentAddress(String sentAddress) {
        this.sentAddress = sentAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
