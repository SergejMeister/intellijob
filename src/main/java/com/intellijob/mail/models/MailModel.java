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

package com.intellijob.mail.models;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import java.io.IOException;
import java.util.Date;

/**
 * This is simple mail model.
 */
public class MailModel {

    public static final String TEXT_PLAIN = "text/plain";
    public static final String TEXT_HTML = "text/html";

    private Address from;
    private String subject;
    private Date sentDate;
    private Date receivedDate;
    private String contentType;
    private String content;


    public MailModel(Message message) throws MessagingException, IOException {
        Address[] addresses = message.getFrom();
        if (addresses.length > 0) {
            this.from = addresses[0];
        }
        this.subject = message.getSubject();
        this.sentDate = message.getSentDate();
        this.receivedDate = message.getReceivedDate();
        initContent(message);
    }

    private void initContent(Message message) throws MessagingException, IOException {
        Object o = message.getContent();
        if (o instanceof String) {
            this.contentType = message.getContentType();
            this.content = message.getContent().toString();
            return;
        } else if (o instanceof Multipart) {
            Multipart multiPart = (Multipart) o;
            int multiPartCount = multiPart.getCount();
            for (int i = 0; i < multiPartCount; i++) {
                BodyPart bodyPart = multiPart.getBodyPart(i);
                if (isHtmlContent(bodyPart.getContentType())) {
                    this.contentType = bodyPart.getContentType();
                    this.content = bodyPart.getContent().toString();
                    return;
                }
            }
        }
    }

    private Boolean isHtmlContent(String messageContentType) {
        String lowerContentType = messageContentType.toLowerCase();
        return lowerContentType.contains(TEXT_HTML);
    }

    @SuppressWarnings("unused")
    private Boolean isTextContent(String messageContentType) {
        String lowerContentType = messageContentType.toLowerCase();
        return lowerContentType.contains(TEXT_PLAIN);
    }

    public Address getFrom() {
        return from;
    }

    public void setFrom(Address from) {
        this.from = from;
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
}
