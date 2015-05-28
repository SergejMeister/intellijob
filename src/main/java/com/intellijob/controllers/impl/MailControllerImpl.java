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

package com.intellijob.controllers.impl;

import com.intellijob.controllers.MailController;
import com.intellijob.domain.Mail;
import com.intellijob.exceptions.UniqueDomainException;
import com.intellijob.mail.models.MailModel;
import com.intellijob.repository.MailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents mail controllers.
 */
@Controller
public class MailControllerImpl implements MailController {

    @Autowired
    private MailRepository mailRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Mail saveModel(MailModel mailModel) throws UniqueDomainException {
        Mail mail = convertMailModelToMail(mailModel);
        return mailRepository.save(mail);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Mail> saveModel(List<MailModel> mailModels) {
        List<Mail> mailsToSave = convertMailModelToMail(mailModels);
        return mailRepository.save(mailsToSave);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mail findMail(String mailId) {
        return mailRepository.findOne(mailId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Mail> findAll() {
        return mailRepository.findAll(new Sort(Sort.Direction.DESC, "receivedDate"));
    }

    private List<Mail> convertMailModelToMail(List<MailModel> mailModels) {
        List<Mail> result = new ArrayList<>();
        for (MailModel mailModel : mailModels) {
            Mail mail = convertMailModelToMail(mailModel);
            result.add(mail);
        }

        return result;
    }

    private Mail convertMailModelToMail(MailModel mailModel) {
        Mail mail = new Mail();
        mail.setSentAddress(mailModel.getFrom().toString());
        mail.setContentType(mailModel.getContentType());
        mail.setSubject(mailModel.getSubject());
        mail.setSentDate(mailModel.getSentDate());
        mail.setReceivedDate(mailModel.getReceivedDate());
        mail.setContent(mailModel.getContent());
        return mail;
    }
}
